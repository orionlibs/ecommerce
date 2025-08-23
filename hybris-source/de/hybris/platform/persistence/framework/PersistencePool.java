package de.hybris.platform.persistence.framework;

import com.google.common.base.Preconditions;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.cache.RelationsCache;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.persistence.ItemPermissionFacade;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.persistence.property.PersistenceManager;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.collections.YFastFIFOMap;
import de.hybris.platform.util.jeeapi.YNoSuchEntityException;
import de.hybris.platform.util.jeeapi.YObjectNotFoundException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;

public class PersistencePool
{
    private static final Logger log = Logger.getLogger(PersistencePool.class);
    private static final String ENTITYPROXY_CACHE = "cache.persistenceproxy";
    private static final int DEFAULTSIZE_ENTITYPROXY_CACHE = 5000;
    private static final int LIMITSIZE_ENTITYPROXY_CACHE = 50000;
    private final Map<String, HomeProxy> homeProxies;
    private final Map<String, EntityInstance> entitySingletons;
    private final Map<PK, EntityProxy> entityProxyCache;
    private final AbstractTenant tenant;
    private final JDBCValueMappings jdbcValueMappings;
    private final List<PersistenceListener> listeners = new CopyOnWriteArrayList<>();
    private final Map<Object, EntityInstance> entityInstanceCache;
    private final SystemCriticalTypes systemCriticalTypes;


    public void registerPersistenceListener(PersistenceListener listener)
    {
        this.listeners.add(listener);
    }


    public void unregisterPersistenceListener(PersistenceListener listener)
    {
        this.listeners.remove(listener);
    }


    public void notifyEntityCreation(PK pk)
    {
        for(PersistenceListener l : this.listeners)
        {
            try
            {
                l.entityCreated(pk);
            }
            catch(Exception e)
            {
                System.err.println("error notifying entity creation : " + e.getMessage());
                e.printStackTrace(System.err);
            }
        }
    }


    public void registerHJMPListeners(InvalidationManager invManager)
    {
        InvalidationTopic topic = invManager.getOrCreateInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
        topic.addInvalidationListener((InvalidationListener)new EntityInvalidationListener(this));
        topic.addInvalidationListener(RelationsCache.getDefaultInstance().createInvalidationListener());
    }


    public PersistencePool(AbstractTenant system)
    {
        this.tenant = system;
        this.jdbcValueMappings = JDBCValueMappings.getInstance();
        this.systemCriticalTypes = instantiateSystemCriticalTypes(system);
        this.homeProxies = new ConcurrentHashMap<>(300, 0.75F, 4);
        this.entitySingletons = new ConcurrentHashMap<>(300, 0.75F, 4);
        int cacheSize = system.getConfig().getInt("cache.persistenceproxy", 5000);
        if(!system.getConfig().getBoolean(Config.Params.BYPASS_HYBRIS_RECOMMENDATIONS, false))
        {
            if(cacheSize > 50000)
            {
                log.warn("**********");
                log.warn("Value '" + cacheSize + "' for property 'cache.persistenceproxy' is bigger than maximal allowed value '50000'. For more information please contact the hybris support.");
                log.warn("Using maximal allowed value '50000'.");
                log.warn("**********");
                cacheSize = 50000;
            }
        }
        this.entityProxyCache = (Map<PK, EntityProxy>)new YFastFIFOMap(cacheSize);
        this.entityInstanceCache = (Map<Object, EntityInstance>)new YFastFIFOMap(cacheSize);
    }


    private SystemCriticalTypes instantiateSystemCriticalTypes(AbstractTenant tenant)
    {
        try
        {
            return SystemCriticalTypes.create(tenant);
        }
        catch(Exception e)
        {
            log.warn("Failed to obtain the system critical types for the tenant `" + tenant
                            .getTenantID() + "`. Using the default types.", e);
            return SystemCriticalTypes.defaultTypes();
        }
    }


    public void clearCache()
    {
        this.homeProxies.clear();
        this.entitySingletons.clear();
        this.entityProxyCache.clear();
        this.entityInstanceCache.clear();
    }


    EntityInstance getEntitySingleton(String jndiName)
    {
        EntityInstance entity = this.entitySingletons.get(jndiName);
        if(entity == null)
        {
            entity = createEntityInstance(jndiName);
            this.entitySingletons.put(jndiName, entity);
        }
        return entity;
    }


    EntityInstance getEntityInstance(String jndiName, PK pk)
    {
        return createEntityInstance(jndiName, pk);
    }


    public EntityInstance createEntityInstance(String jndiName, PK pk)
    {
        EntityInstance entity = createEntityInstance(jndiName);
        entity.getEntityContext().setPK(pk);
        return entity;
    }


    EntityInstance createEntityInstance(String jndiName)
    {
        try
        {
            ItemDeployment id = this.tenant.getPersistenceManager().getItemDeployment(jndiName);
            if(id == null)
            {
                throw new RuntimeException("no deployment found for " + jndiName);
            }
            Preconditions.checkArgument(id.getName().equalsIgnoreCase(jndiName), "jndi name mismatch " + jndiName + "<>" + id
                            .getName());
            Class<EntityInstance> ejbclass = id.getConcreteEJBImplementationClass();
            EntityInstance entity = ejbclass.newInstance();
            EntityInstanceContextImpl ctx = new EntityInstanceContextImpl(this, id);
            entity.setEntityContext((EntityInstanceContext)ctx);
            return entity;
        }
        catch(RuntimeException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    public EntityProxy findEntityByPK(String jndiName, PK pk) throws YObjectNotFoundException
    {
        try
        {
            Transaction curTx = Transaction.current();
            if(curTx.isRunning())
            {
                EntityInstance entity = curTx.getOrLoadTxBoundEntityInstance(this, jndiName, pk);
            }
            else
            {
                EntityInstance entity = createEntityInstance(jndiName, pk);
                entity.ejbFindByPrimaryKey(pk);
            }
            return getOrCreateUninitializedEntityProxy(jndiName, pk);
        }
        catch(YNoSuchEntityException e)
        {
            throw new YObjectNotFoundException(e.getMessage());
        }
    }


    public EntityProxy getOrCreateUninitializedEntityProxy(String jndiName, PK pk)
    {
        EntityProxy proxy = getEntityProxy(pk);
        if(proxy == null)
        {
            Class<?> clForClassLoader, remIntfs[];
            ItemDeployment depl = this.tenant.getPersistenceManager().getItemDeployment(jndiName);
            if(depl != null)
            {
                clForClassLoader = depl.getRemoteInterface();
                remIntfs = new Class[] {clForClassLoader, ItemPermissionFacade.class};
            }
            else
            {
                clForClassLoader = getEntitySingleton(jndiName).getClass();
                remIntfs = Utilities.getAllInterfaces(clForClassLoader);
            }
            RemoteInvocationHandler remoteInvocationHandler = new RemoteInvocationHandler(this, jndiName, pk);
            proxy = (EntityProxy)Proxy.newProxyInstance(clForClassLoader.getClassLoader(), remIntfs, (InvocationHandler)remoteInvocationHandler);
            this.entityProxyCache.put(pk, proxy);
        }
        return proxy;
    }


    public EntityProxy getEntityProxy(PK pk)
    {
        return this.entityProxyCache.get(pk);
    }


    public void removeEntityProxy(PK pk)
    {
        this.entityProxyCache.remove(pk);
    }


    public HomeProxy getHomeProxy(int tc)
    {
        return getHomeProxy(this.tenant.getPersistenceManager().getJNDIName(tc));
    }


    public HomeProxy getHomeProxy(String jndiName)
    {
        HomeProxy ret = this.homeProxies.get(jndiName);
        if(ret == null)
        {
            ret = createHomeProxy(jndiName);
            this.homeProxies.put(jndiName, ret);
        }
        return ret;
    }


    private HomeProxy createHomeProxy(String jndiName)
    {
        ItemDeployment id = this.tenant.getPersistenceManager().getItemDeployment(jndiName);
        if(id == null)
        {
            throw new IllegalStateException("there is no deployment '" + jndiName + "' - cannot create home");
        }
        Class<? extends HomeProxy> homeinterface = id.getHomeInterface();
        if(homeinterface == null)
        {
            throw new IllegalStateException("there is no home interface for deployment '" + jndiName + "' - cannot create home");
        }
        HomeInvocationHandler handler = new HomeInvocationHandler(this, jndiName);
        handler.setInvokeAsSystemOperation(isSystemCriticalType(id.getTypeCode()));
        HomeProxy home = (HomeProxy)Proxy.newProxyInstance(homeinterface.getClassLoader(), new Class[] {homeinterface, ItemPermissionFacade.class}, (InvocationHandler)handler);
        return home;
    }


    public boolean isSystemCriticalType(int typeCode)
    {
        return this.systemCriticalTypes.isSystemCritical(typeCode);
    }


    public Cache getCache()
    {
        return this.tenant.getCache();
    }


    public AbstractTenant getTenant()
    {
        return this.tenant;
    }


    public PersistenceManager getPersistenceManager()
    {
        return this.tenant.getPersistenceManager();
    }


    public String getDatabase()
    {
        return getDataSource().getDatabaseName();
    }


    public HybrisDataSource getDataSource()
    {
        return this.tenant.getDataSource();
    }


    public JDBCValueMappings getJDBCValueMappings()
    {
        return this.jdbcValueMappings;
    }


    private static final String[] REGULAR_TABLES = new String[] {"TABLE"};


    public void verifyTableExistenceIfNeeded(Connection connection, ItemDeployment depl) throws SQLException
    {
        if(!Config.DatabaseName.POSTGRESQL.getName().equals(getDatabase()) ||
                        !depl.getName().equals(this.tenant.getPersistenceManager().getJNDIName(55)))
        {
            return;
        }
        ResultSet rs = connection.getMetaData().getTables(null, null, depl.getDatabaseTableName(), REGULAR_TABLES);
        try
        {
            if(!rs.next())
            {
                throw new SQLException("Can't find table " + depl.getDatabaseTableName() + " for tenant " + getTenant());
            }
            if(rs != null)
            {
                rs.close();
            }
        }
        catch(Throwable throwable)
        {
            if(rs != null)
            {
                try
                {
                    rs.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }
}
