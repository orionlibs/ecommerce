package de.hybris.platform.core;

import com.google.common.base.Preconditions;
import de.hybris.platform.cluster.DefaultClusterNodeManagementService;
import de.hybris.platform.core.ssl.AdditionalTrustStore;
import de.hybris.platform.core.ssl.JerseyClientDefaultSslContextProvider;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.persistence.property.PersistenceManager;
import de.hybris.platform.servicelayer.model.DefaultNewModelContextFactory;
import de.hybris.platform.spring.ctx.TenantNameAwareContext;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.DateFormatUtilImpl;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.SingletonCreator;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.VelocityHelper;
import de.hybris.platform.util.WebSessionFunctions;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.logging.context.LoggingContextFactory;
import java.io.File;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.net.ssl.SSLContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class Registry
{
    public static final String SYSTEM_PROPERTY_FAILSAFE_ACTIVE = "activate.tenant.fallback";
    public static final boolean FAILSAVE_NOTENANTACTIVE = "true"
                    .equalsIgnoreCase(System.getProperty("activate.tenant.fallback"));

    static
    {
        ClassLoaderUtils.executeWithWebClassLoaderParentIfNeeded(() -> {
            preferredClusterID = -1;
            VelocityHelper.init();
            System.setProperty("model.context.factory.class", DefaultNewModelContextFactory.class.getName());
            System.setProperty("counter.pk.generator.class", DefaultPKCounterGenerator.class.getName());
            System.setProperty("standard.datarange.factory.class", DateFormatUtilImpl.class.getName());
            CoreAlgorithms.setLegacyConfigProvider((CoreAlgorithms.LegacyRoundingConfigProvider)new Object());
        });
    }

    private static final ConcurrentMap<Tenant, Queue<Init>> tenantInitializations = new ConcurrentHashMap<>(10, 0.75F, 4);
    private static final List<Init> savedInitializations = new CopyOnWriteArrayList<>();
    private static final List<TenantListener> tenantListeners = new CopyOnWriteArrayList<>();
    private static final List<TenantListener> publicTenantListeners = Collections.unmodifiableList(tenantListeners);
    private static final SingletonCreator _singletonCreator = new SingletonCreator();
    private static final ThreadLocal<Object> _currentTenant = new ThreadLocal();
    private static final boolean traceTenantSwitch = "true".equalsIgnoreCase(System.getProperty("log.tenant.switch"));
    private static final boolean addTenantActivationStack = (traceTenantSwitch && "true"
                    .equalsIgnoreCase(System.getProperty("log.tenant.switch.stack")));
    private static final HybrisContextHolder contextHolder = HybrisContextHolder.getInstance();
    private static final SlaveTenantsHolder slaveTenantsHolder = new SlaveTenantsHolder();
    private static volatile MasterTenant master;
    private static boolean standaloneMode = false;
    private static int preferredClusterID = -1;
    private static final Map<String, Exception> tenantCreationLog = new ConcurrentHashMap<>();
    private static final Logger log = Logger.getLogger(Registry.class.getName());


    public static Map<String, SlaveTenant> getSlaveTenants()
    {
        return slaveTenantsHolder.getOrCreate();
    }


    public static SlaveTenant getSlaveJunitTenant()
    {
        Map<String, SlaveTenant> slaveTenant = getSlaveTenants();
        SlaveTenant junitSlaveTenant = slaveTenant.get("junit");
        if(junitSlaveTenant != null)
        {
            return junitSlaveTenant;
        }
        throw new IllegalStateException("Could not find the junit slave tenant, please configure junit tenant in platform/tenant_{tenantID}.properties or config/local_tenant_{tenantID}.properties");
    }


    static void registerTenantInitializationTenant(String tenantID)
    {
        Exception old = null;
        if(tenantCreationLog != null)
        {
            old = tenantCreationLog.put(tenantID, new Exception());
        }
        if(old != null)
        {
            throw new RuntimeException("tenant " + tenantID
                            + " is being created twice! \nthe two stacktraces of where it was created follows. Root cause is mostly \nthat you are using static variables that references items. (for example if you\nare using 'public static Product BASE = ProductManager.getInstance().getProduct(....)'. \nYou should never use this static constructs. It will make our tenant and cache unusable.\n FIRST CREATION: "
                            +
                            Utilities.getStackTraceAsString(old) + "\n\n+SECOND CREATION FOLLOWS AS TRACE: ");
        }
        if(RedeployUtilities.isShutdownInProgress())
        {
            RuntimeException runtimeException = new RuntimeException("tenant " + tenantID
                            + " is being created during shutdown! Root cause is mostly \nthat you are using static variables that references items. (for example if you\nare using 'public static Product BASE = ProductManager.getInstance().getProduct(....)'. \nYou should never use this static constructs. It will make our tenant and cache unusable.");
            runtimeException.printStackTrace();
            throw runtimeException;
        }
    }


    static void unregisterShutdownTenant(String tenantID)
    {
        tenantCreationLog.remove(tenantID);
    }


    public static void destroyAndForceStartup()
    {
        AbstractTenant currentTenant = getCurrentTenantNoFallback();
        destroyAndForceStartupTenant(currentTenant);
    }


    private static void destroyAndForceStartupTenant(AbstractTenant curTenant)
    {
        synchronized(getMasterTenant())
        {
            try
            {
                curTenant.doShutDownDuringInitialization();
                curTenant.doStartUp();
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloSystemException(e);
            }
            startup();
        }
    }


    protected static Exception getCurrentTenantActivationTrace()
    {
        Object object = _currentTenant.get();
        return (object == null || object instanceof Tenant) ? null : ((TenantHolder)object).stack;
    }


    protected static void setCurrentTenantInternal(Tenant tenant)
    {
        if(addTenantActivationStack)
        {
            _currentTenant.set(new TenantHolder(tenant));
        }
        else
        {
            _currentTenant.set(tenant);
        }
        OperationInfo.updateThread(OperationInfo.builder().withTenant((tenant != null) ? tenant.getTenantID() : null).build());
        setLog4JMDC(tenant);
    }


    protected static Tenant getCurrentTenantInternal()
    {
        Object object = _currentTenant.get();
        return (object == null || object instanceof Tenant) ? (Tenant)object : ((TenantHolder)object).tenant;
    }


    public static void destroy(boolean all)
    {
        MasterTenant master = getMasterTenant();
        synchronized(master)
        {
            if(all)
            {
                master.doShutDown();
                contextHolder.destroy();
                tenantListeners.clear();
                setCurrentTenantInternal(null);
                tenantCreationLog.clear();
                if(_singletonCreator != null)
                {
                    _singletonCreator.destroy();
                }
                Log4JUtils.shutdown();
            }
            else
            {
                ((AbstractTenant)getCurrentTenant()).doShutDown();
            }
        }
    }


    private static Queue<Init> getTenantInitQueue(Tenant tenant)
    {
        Queue<Init> ret = tenantInitializations.get(tenant);
        if(ret == null)
        {
            synchronized(tenantInitializations)
            {
                ret = tenantInitializations.get(tenant);
                if(ret == null)
                {
                    ret = new ConcurrentLinkedQueue<>(savedInitializations);
                    tenantInitializations.put(tenant, ret);
                }
            }
        }
        return ret;
    }


    static boolean hasUninitializedInits(Tenant tenant)
    {
        return !getTenantInitQueue(tenant).isEmpty();
    }


    static void resetInitCounter(Tenant tenant)
    {
        tenantInitializations.remove(tenant);
    }


    static Init getNextInit(Tenant tenant)
    {
        return getTenantInitQueue(tenant).poll();
    }


    private static void registerInit(Init init)
    {
        synchronized(tenantInitializations)
        {
            savedInitializations.add(init);
            for(Map.Entry<Tenant, Queue<Init>> e : tenantInitializations.entrySet())
            {
                ((Queue<Init>)e.getValue()).add(init);
            }
        }
    }


    public static void startup()
    {
        if(RedeployUtilities.isShutdownInProgress())
        {
            return;
        }
        Thread thread = Thread.currentThread();
        ClassLoader contextClassLoader = thread.getContextClassLoader();
        try
        {
            Tenant tenant = getCurrentTenantNoFallback();
            if(tenant == null)
            {
                activateMasterTenant();
            }
        }
        finally
        {
            thread.setContextClassLoader(contextClassLoader);
        }
    }


    public static <T> T getNonTenantSingleton(SingletonCreator.Creator<T> creator)
    {
        return (T)_singletonCreator.getSingleton(creator);
    }


    public static <T> T getSingleton(Class<T> clazz)
    {
        return (T)getCurrentTenant().getSingletonCreator().getSingleton(clazz);
    }


    public static <T> T getSingleton(SingletonCreator.Creator<T> creator)
    {
        return (T)getCurrentTenant().getSingletonCreator().getSingleton(creator);
    }


    public static <T> T replaceSingleton(SingletonCreator.Creator<T> creator)
    {
        return (T)getCurrentTenant().getSingletonCreator().replaceSingleton(creator);
    }


    public static <T> T runAsTenant(Tenant tenant, Callable<T> callable) throws Exception
    {
        Tenant prev = null;
        boolean prevForceMaster = false;
        boolean prevSlaveDataSourceMode = false;
        String prevAlternativeDataSource = null;
        if(hasCurrentTenant() && (prev = getCurrentTenantNoFallback()) != tenant)
        {
            if(prev.isAlternativeMasterDataSource())
            {
                prevAlternativeDataSource = prev.getDataSource().getID();
            }
            else if(prev.isSlaveDataSource())
            {
                prevSlaveDataSourceMode = true;
            }
            if(prev.isForceMaster())
            {
                prevForceMaster = true;
            }
            unsetCurrentTenant();
        }
        try
        {
            setCurrentTenant(tenant);
            return callable.call();
        }
        finally
        {
            if(prev != null)
            {
                if(prev != tenant)
                {
                    unsetCurrentTenant();
                    if(prev instanceof SlaveTenant)
                    {
                        setCurrentTenant(prev);
                    }
                    else
                    {
                        activateMasterTenantAndFailIfAlreadySet();
                    }
                    if(prevSlaveDataSourceMode)
                    {
                        prev.activateSlaveDataSource();
                    }
                    else if(prevAlternativeDataSource != null)
                    {
                        prev.activateAlternativeMasterDataSource(prevAlternativeDataSource);
                    }
                    if(prevForceMaster)
                    {
                        prev.forceMasterDataSource();
                    }
                }
            }
            else
            {
                unsetCurrentTenant();
            }
        }
    }


    public static void setCurrentTenant(Tenant tenant)
    {
        Tenant prev = getCurrentTenantInternal();
        if(prev != tenant && (prev == null || !prev.equals(tenant)))
        {
            boolean isOk = false;
            try
            {
                handleTenantSwitch(prev, tenant);
                setCurrentTenantInternal(tenant);
                activateTenant(tenant);
                isOk = true;
            }
            finally
            {
                if(!isOk)
                {
                    setCurrentTenantInternal(prev);
                }
            }
        }
    }


    private static void handleTenantSwitch(Tenant prev, Tenant next)
    {
        if(prev != null)
        {
            if(traceTenantSwitch)
            {
                String msg = "switching from active tenant " + prev.getTenantID() + " to " + next.getTenantID() + " without previous one being deactivated via Registry.unsetCurrentTenant() - set system property 'log.tenant.switch' to false to avoid this message";
                if(addTenantActivationStack)
                {
                    Exception firstTenantActivationTrace = getCurrentTenantActivationTrace();
                    log.warn(msg, new RuntimeException("second tenant activation trace", firstTenantActivationTrace));
                }
                else
                {
                    log.warn(msg);
                }
            }
            deactivateTenant(prev, true);
        }
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static Tenant activateMasterTenantAndFailIfAlreadySet()
    {
        return activateMasterTenant();
    }


    public static Tenant activateMasterTenant()
    {
        Tenant prev = getCurrentTenantInternal();
        MasterTenant ret = getMasterTenant();
        setCurrentTenant((Tenant)ret);
        return prev;
    }


    public static Tenant activateMasterTenantForInit()
    {
        Tenant prev = getCurrentTenantInternal();
        MasterTenant ret = getMasterTenant();
        ret.setSystemInit(Boolean.TRUE);
        setCurrentTenant((Tenant)ret);
        return prev;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static Tenant activateMasterTenant(REPLACE replace)
    {
        return activateMasterTenant();
    }


    protected static boolean assureTenantStarted(AbstractTenant sys)
    {
        Preconditions.checkArgument((sys != null));
        if(sys.getState() != AbstractTenant.State.STARTED)
        {
            try
            {
                sys.doStartUp();
            }
            catch(ConsistencyCheckException e)
            {
                log.error("cannot activate tenant " + sys + " due to " + e);
                return false;
            }
            if(sys instanceof SlaveTenant && (!((SlaveTenant)sys).isActive() || !((SlaveTenant)sys).isValid()))
            {
                sys.shutDown();
                log.error("cannot activate tenant " + sys + " since it is not active or invalid");
                return false;
            }
        }
        else if(sys.isConnectionPoolCheckEnabled() && sys.cannotConnect())
        {
            log.error("cannot activate tenant " + sys + " since its database connection is currently lost");
            return false;
        }
        sys.executeInitsIfNecessary();
        if(sys.getApplicationContext() == null)
        {
            throw new IllegalStateException("Can not activate tenant " + sys + " since it has no application context created ");
        }
        return true;
    }


    public static void setCurrentTenantByID(String tenantID)
    {
        Tenant tenant = getTenantByID(tenantID);
        if(tenant == null)
        {
            throw new IllegalArgumentException("no tenant '" + tenantID + "'");
        }
        setCurrentTenant(tenant);
    }


    public static void unsetCurrentTenant()
    {
        Tenant tenant = getCurrentTenantInternal();
        if(tenant != null)
        {
            deactivateTenant(tenant, false);
            setCurrentTenantInternal(null);
        }
    }


    private static void activateTenant(Tenant tenant)
    {
        if(!assureTenantStarted((AbstractTenant)tenant))
        {
            throw new IllegalStateException("could not switch tenant to " + tenant);
        }
        if(((AbstractTenant)tenant).connectionHasBeenBroken())
        {
            log.warn("Activating tenant " + tenant.getTenantID() + " after connection had been broken. In a clustered environment this node may be out of sync with the rest of the cluster in case of complete network outage!");
            ((AbstractTenant)tenant).clearConnectionHasBeenBroken();
        }
        tenant.deactivateAlternativeDataSource();
    }


    private static void deactivateTenant(Tenant tenant, boolean forSwitchingToAnotherTenant)
    {
        if(forSwitchingToAnotherTenant)
        {
            if(Transaction.current().isRunning())
            {
                throw new IllegalStateException("could not switch tenant to " + tenant + " while inside transaction ");
            }
        }
        tenant.deactivateAlternativeDataSource();
    }


    public static boolean hasCurrentTenant()
    {
        return (getCurrentTenantInternal() != null);
    }


    public static boolean isCurrentTenant(Tenant tenant)
    {
        return (tenant != null && tenant.equals(getCurrentTenantInternal()));
    }


    public static <T extends Tenant> T getCurrentTenantNoFallback()
    {
        Tenant ret = getCurrentTenantInternal();
        if(ret != null)
        {
            ((AbstractTenant)ret).executeInitsIfNecessary();
        }
        return (T)ret;
    }


    public static boolean isCurrentTenantStarted()
    {
        Tenant tenant = getCurrentTenantInternal();
        if(tenant == null)
        {
            return false;
        }
        return (AbstractTenant.State.STARTED == ((AbstractTenant)tenant).getState());
    }


    public static <T extends Tenant> T getCurrentTenant()
    {
        Tenant ret = getCurrentTenantInternal();
        if(FAILSAVE_NOTENANTACTIVE)
        {
            if(ret == null)
            {
                activateMasterTenant();
                return (T)getCurrentTenantInternal();
            }
            ((AbstractTenant)ret).executeInitsIfNecessary();
            return (T)ret;
        }
        if(ret == null)
        {
            throw new IllegalStateException("no tenant active. if you do not want to use tenants, call Registry.activateMasterTenant() to assure the Master tenant is active.");
        }
        ((AbstractTenant)ret).executeInitsIfNecessary();
        return (T)ret;
    }


    public static <T extends Tenant> T getTenantByID(String id)
    {
        if("master".equalsIgnoreCase(id))
        {
            return (T)getMasterTenant();
        }
        return (T)getSlaveTenants().get(id);
    }


    public static PersistenceManager getPersistenceManager()
    {
        return getCurrentTenant().getPersistenceManager();
    }


    static void setLog4JMDC(Tenant tenant)
    {
        if(tenant == null || tenant instanceof MasterTenant)
        {
            LoggingContextFactory.getLoggingContextHandler().put("Tenant", "");
        }
        else
        {
            String tenantID = (tenant.getTenantID() == null) ? "" : tenant.getTenantID();
            LoggingContextFactory.getLoggingContextHandler().put("Tenant", "(" + tenantID + ") ");
        }
    }


    private static final Object MASTERTENANTLOCK = new Object();


    public static MasterTenant getMasterTenant()
    {
        if(master == null)
        {
            synchronized(MASTERTENANTLOCK)
            {
                if(master == null)
                {
                    master = new MasterTenant();
                    initAdditionalTrustStoreIfConfigured(master.getConfig());
                }
            }
        }
        return master;
    }


    private static void initAdditionalTrustStoreIfConfigured(ConfigIntf config)
    {
        String trustStore = config.getString("additional.javax.net.ssl.trustStore", "");
        if(StringUtils.isBlank(trustStore))
        {
            log.info("No additional Trust Store set in the hybris properties.");
            return;
        }
        log.info("Using additional Trust Store set in the hybris properties.");
        String configuredPassword = config.getString("additional.javax.net.ssl.trustStorePassword", "");
        String trustStorePassword = StringUtils.isBlank(configuredPassword) ? null : configuredPassword;
        try
        {
            AdditionalTrustStore additionalTrustStore = AdditionalTrustStore.createFromFile(new File(trustStore), trustStorePassword);
            SSLContext mergedSSLContext = additionalTrustStore.mergeWithTheDefaultSSLContext();
            SSLContext.setDefault(mergedSSLContext);
            JerseyClientDefaultSslContextProvider.useJvmDefaultSslContext();
            log.info("Additional Trust Store has been merged with the default SSLContext.");
        }
        catch(GeneralSecurityException | java.io.IOException e)
        {
            log.warn("Failed to use additional Trust Store. Will continue using the default one.", e);
        }
    }


    public static final int getClusterID()
    {
        return DefaultClusterNodeManagementService.getInstance().getClusterID();
    }


    public static final Collection<String> getClusterGroups()
    {
        return DefaultClusterNodeManagementService.getInstance().getClusterGroups();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static void setPreferredClusterID(int clusterid)
    {
        if(master == null)
        {
            if(clusterid == 15)
            {
                activateStandaloneMode();
            }
            preferredClusterID = clusterid;
        }
        else if(log.isDebugEnabled())
        {
            log.debug("trying to call setPreferredClusterID(" + clusterid + ") but Mastertenant already started. Current clusterid is: " +
                            getMasterTenant().getClusterID());
        }
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static int getPreferredClusterID()
    {
        return preferredClusterID;
    }


    public static void activateStandaloneMode()
    {
        if(master == null)
        {
            standaloneMode = true;
        }
        else if(log.isDebugEnabled())
        {
            log.debug("trying to call activateStandaloneMode() but master tenant already started.");
        }
    }


    public static boolean isStandaloneMode()
    {
        return standaloneMode;
    }


    @Deprecated(since = "5.0", forRemoval = true)
    public static ApplicationContext getGlobalApplicationContext()
    {
        return getCoreApplicationContext();
    }


    public static ApplicationContext getApplicationContext()
    {
        startup();
        ApplicationContext context = getWebApplicationContextIfExists();
        if(context == null)
        {
            context = getCoreApplicationContext();
        }
        return context;
    }


    public static ServletContext getServletContextIfExists()
    {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(requestAttributes instanceof ServletRequestAttributes)
        {
            return ((ServletRequestAttributes)requestAttributes).getRequest().getServletContext();
        }
        return null;
    }


    private static ApplicationContext getWebApplicationContextIfExists()
    {
        ServletContext servletContext = null;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(requestAttributes instanceof ServletRequestAttributes)
        {
            servletContext = ((ServletRequestAttributes)requestAttributes).getRequest().getServletContext();
        }
        if(servletContext == null)
        {
            HttpServletRequest servletRequest = WebSessionFunctions.getCurrentHttpServletRequest();
            if(servletRequest != null)
            {
                servletContext = servletRequest.getServletContext();
            }
        }
        if(servletContext != null)
        {
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            if(webApplicationContext == null)
            {
                if(log.isDebugEnabled())
                {
                    log.debug("No web spring configuration for webapp " + servletContext.getServletContextName() + " found, using only core configuration.");
                }
            }
            else
            {
                if(webApplicationContext instanceof TenantNameAwareContext)
                {
                    TenantNameAwareContext tenantAwareContext = (TenantNameAwareContext)webApplicationContext;
                    if(!StringUtils.equalsIgnoreCase(tenantAwareContext.getTenantName(),
                                    getCurrentTenantNoFallback().getTenantID()))
                    {
                        throw new IllegalStateException("###############################################################\nGiven current tenant is " +
                                        getCurrentTenantNoFallback() + " but returned servlet context related web context (" + tenantAwareContext + ") is attached to other tenant " + tenantAwareContext
                                        .getTenantName() + "\n###############################################################");
                    }
                }
                return (ApplicationContext)webApplicationContext;
            }
        }
        return null;
    }


    public static ApplicationContext getCoreApplicationContext()
    {
        AbstractTenant currentTenant = getCurrentTenantNoFallback();
        if(currentTenant == null)
        {
            throw new IllegalStateException("No tenant assigned for current thread ");
        }
        return (ApplicationContext)currentTenant.getApplicationContext();
    }


    public static ApplicationContext getSingletonGlobalApplicationContext()
    {
        return (ApplicationContext)contextHolder.getGlobalInstance();
    }


    public static void registerTenantListener(TenantListener listener)
    {
        if(tenantListeners.contains(listener))
        {
            System.err.println("Tenant listener " + listener + " already registered!");
            Thread.dumpStack();
        }
        else
        {
            tenantListeners.add(listener);
        }
    }


    public static void unregisterTenantListener(TenantListener listener)
    {
        tenantListeners.remove(listener);
    }


    public static List<TenantListener> getTenantListeners()
    {
        return publicTenantListeners;
    }
}
