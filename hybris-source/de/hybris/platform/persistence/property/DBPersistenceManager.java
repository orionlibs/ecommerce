package de.hybris.platform.persistence.property;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.JaloOnlyItem;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.ViewType;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.hjmp.HJMPFindInvalidationListener;
import de.hybris.platform.persistence.property.internal.DeploymentInfoProvider;
import de.hybris.platform.persistence.property.internal.LoadDeploymentInfoResult;
import de.hybris.platform.persistence.property.loader.BatchTypeInfoMapLoader;
import de.hybris.platform.persistence.type.AttributeDescriptorRemote;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.persistence.type.HierarchieTypeRemote;
import de.hybris.platform.persistence.type.TypeManagerEJB;
import de.hybris.platform.spring.CGLibUtils;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.EJBTools;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.typesystem.PlatformStringUtils;
import de.hybris.platform.util.typesystem.YDeploymentJDBC;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

public class DBPersistenceManager implements PersistenceManager
{
    private static final Logger LOG = Logger.getLogger(DBPersistenceManager.class.getName());
    public static final String TYPE_INFO_MAP_BATCH_MODE = "typeinfomap.loading.batch.mode";
    public static final String DUMP_FILE_PARAM = "persistence.dumpfile";
    public static final String LOG_CLEAR_PARAM = "persistence.log.clear";
    public static final String DISALLOW_CLEAR_PARAM = "persistence.disallow.clear";
    private volatile LoadStatus status = LoadStatus.NOT_LOADED;
    private final AtomicReference<PersistenceInfoCache> cacheVariable = new AtomicReference<>(null);
    private volatile Map<String, ItemDeployment> deploymentInfos;
    private Map<Integer, String> typeCode2JndiMappings;
    private Map<String, String> javaDefMapping;
    private final AbstractTenant tenant;
    private final boolean logClear;
    private final boolean disallowClear;
    private final ThreadLocal<Set<Object>> loopDetectionSet = (ThreadLocal<Set<Object>>)new Object(this);


    public DBPersistenceManager(AbstractTenant tenant, InvalidationManager invManager)
    {
        this.tenant = tenant;
        this.logClear = tenant.getConfig().getBoolean("persistence.log.clear", false);
        this.disallowClear = tenant.getConfig().getBoolean("persistence.disallow.clear", false);
    }


    public String getSQLTypeDef(Class javaClass, String ownTypeDef)
    {
        loadAllDeployments();
        if(ownTypeDef != null)
        {
            String mapped = this.javaDefMapping.get(ownTypeDef);
            return (mapped != null) ? mapped : ownTypeDef;
        }
        String typeDef = this.javaDefMapping.get(JDBCValueMappings.convertPrimitiveTypes(javaClass).getName());
        return (typeDef == null) ? this.javaDefMapping.get(Serializable.class.getName()) : typeDef;
    }


    protected final PersistenceInfoCache cache()
    {
        PersistenceInfoCache ret = this.cacheVariable.get();
        if(ret == null)
        {
            ret = createInfoCache();
            if(!this.cacheVariable.compareAndSet(null, ret))
            {
                ret = this.cacheVariable.get();
            }
        }
        return ret;
    }


    private PersistenceInfoCache createInfoCache()
    {
        long systemInitUpdateTimestamp = this.tenant.getSystemEJB().getMetaInformationManager().getSystemInitUpdateTimestamp();
        return new PersistenceInfoCache(this.tenant.getTenantID(), systemInitUpdateTimestamp);
    }


    public final TypeInfoMap getPersistenceInfo(String code)
    {
        return getPersistenceInfoInternal(code);
    }


    public final TypeInfoMap getPersistenceInfo(PK typePK)
    {
        return getPersistenceInfoInternal(typePK);
    }


    private final TypeInfoMap getPersistenceInfoInternal(Object typePKorCode)
    {
        if(isDestroyed())
        {
            throw new IllegalStateException("Persistence manager " + this + " is destroyed - cannot get info for " + typePKorCode);
        }
        if(typePKorCode == null)
        {
            LOG.error("typePK or Code was NULL in getPersistenceInfoInternal()");
            return TypeInfoMap.EMPTY_INFOMAP;
        }
        boolean isCode = !(typePKorCode instanceof PK);
        if(!isLoaded() || (!isCode && PK.NULL_PK.equals(typePKorCode)))
        {
            return TypeInfoMap.EMPTY_INFOMAP;
        }
        TypeInfoMap info = getCachedInfo(typePKorCode);
        if(info == null)
        {
            if(this.logClear)
            {
                LOG.warn("missing schema info for type pk/code:" + typePKorCode + " - trying to load..");
            }
            info = loadAndCacheInfo(typePKorCode, isCode);
        }
        return (info != null) ? info : TypeInfoMap.EMPTY_INFOMAP;
    }


    private TypeInfoMap loadAndCacheInfo(Object typePKorCode, boolean isCode)
    {
        TypeInfoMap info = null;
        try
        {
            if(markCurrentlyLoading(typePKorCode))
            {
                ComposedTypeRemote type = isCode ? this.tenant.getSystemEJB().getTypeManager().getComposedType((String)typePKorCode) : (ComposedTypeRemote)EJBTools.instantiatePK((PK)typePKorCode);
                info = loadInfo(type, this.tenant.getSystemEJB().getTypeManager());
                if(info != null && !info.isEmpty())
                {
                    cacheInfo(info);
                }
            }
            else if(LOG.isDebugEnabled())
            {
                LOG.debug("detected loop for type " + typePKorCode + " - ignored");
            }
        }
        catch(EJBItemNotFoundException e)
        {
            throw new IllegalArgumentException("type code '" + typePKorCode + "' invalid", e);
        }
        catch(NullPointerException e)
        {
            throw new IllegalArgumentException("Nullpointer: type code '" + typePKorCode + "' invalid", e);
        }
        finally
        {
            finishedLoading(typePKorCode);
        }
        return info;
    }


    private boolean markCurrentlyLoading(Object typePKorCode)
    {
        return ((Set<Object>)this.loopDetectionSet.get()).add(typePKorCode);
    }


    private void finishedLoading(Object typePKorCode)
    {
        Set<Object> set = this.loopDetectionSet.get();
        set.remove(typePKorCode);
        if(set.isEmpty())
        {
            this.loopDetectionSet.remove();
        }
    }


    public PropertyTableDefinition getInitialPersistenceInfos(ComposedTypeRemote type, boolean reinit)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("getting  persistence info");
        }
        loadAllDeployments();
        Map<String, PropertyTableDefinition> tableMap = new HashMap<>();
        TypeManagerEJB tm = this.tenant.getSystemEJB().getTypeManager();
        TypeInfoMap info = createInfo(type, tm, tableMap, reinit);
        cacheInfo(info);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("finished initializing persistence info");
        }
        return tableMap.isEmpty() ? null : (PropertyTableDefinition)((Map.Entry)tableMap.entrySet().iterator().next()).getValue();
    }


    public final Collection<PropertyTableDefinition> createInitialPersistenceInfos(Collection types, boolean reinit)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("initializing persistence info");
        }
        clearTypeCache(LoadStatus.NOT_LOADED);
        loadAllDeployments();
        Map<String, PropertyTableDefinition> tableMap = new HashMap<>();
        TypeManagerEJB tm = this.tenant.getSystemEJB().getTypeManager();
        for(Iterator<ComposedTypeRemote> it = types.iterator(); it.hasNext(); )
        {
            ComposedTypeRemote type = it.next();
            TypeInfoMap info = createInfo(type, tm, tableMap, reinit);
            cacheInfo(info);
        }
        setLoaded();
        dumpPersistenceInfoCache(this.cacheVariable.get());
        if(LOG.isDebugEnabled())
        {
            LOG.debug("finished initializing persistence info");
        }
        return tableMap.values();
    }


    public String getDumpFileName()
    {
        String name = this.tenant.getConfig().getParameter("persistence.dumpfile");
        if(name != null && name.length() != 0)
        {
            name = name + "." + name;
        }
        return name;
    }


    protected PersistenceInfoCache restorePersistenceInfoCache()
    {
        String dumpFileName = getDumpFileName();
        if(dumpFileName == null || dumpFileName.length() == 0)
        {
            return null;
        }
        File dumpFile = new File(dumpFileName);
        if(!dumpFile.exists())
        {
            return null;
        }
        if(!dumpFile.canRead())
        {
            return null;
        }
        ObjectInputStream is = null;
        try
        {
            is = new ObjectInputStream(new FileInputStream(dumpFile));
            PersistenceInfoCache ret = (PersistenceInfoCache)is.readObject();
            long systemTs = this.tenant.getSystemEJB().getMetaInformationManager().getSystemInitUpdateTimestamp();
            if(ret.getTimeStamp() != systemTs)
            {
                LOG.warn("outdated persistence info cache " + ret + " , system timestamp is " + systemTs);
                ret = null;
            }
            else if(!this.tenant.getTenantID().equals(ret.getSystemID()))
            {
                LOG.error("wrong persistence info cache " + ret + " , current system id is " + this.tenant.getTenantID());
                ret = null;
            }
            else if(LOG.isInfoEnabled())
            {
                LOG.info("successfully restored persistence cache " + ret);
            }
            return ret;
        }
        catch(Exception e)
        {
            LOG.error("errors restoring dump file from: " + dumpFile + "\n Exception: " + e.getMessage(), e);
            return null;
        }
        finally
        {
            if(is != null)
            {
                try
                {
                    is.close();
                }
                catch(IOException iOException)
                {
                }
            }
        }
    }


    protected void dumpPersistenceInfoCache(PersistenceInfoCache pic)
    {
        String dumpFileName = getDumpFileName();
        if(dumpFileName == null || dumpFileName.length() == 0)
        {
            return;
        }
        File dumpFile = new File(dumpFileName);
        if(!dumpFile.getParentFile().canWrite())
        {
            LOG.info("not using persistence info cache, cannot write to " + dumpFile.getAbsolutePath() + ". this is not a problem for productive environments.");
        }
        else
        {
            ObjectOutputStream os = null;
            try
            {
                os = new ObjectOutputStream(new FileOutputStream(dumpFile));
                os.writeObject(pic);
                if(LOG.isInfoEnabled())
                {
                    LOG.info("successfully dumped persistence info cache.");
                }
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("successfully dumped persistence info cache [ts: " + pic.getTimeStamp() + "]");
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if(os != null)
                {
                    try
                    {
                        os.close();
                    }
                    catch(IOException e)
                    {
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug(e.getMessage());
                        }
                    }
                }
            }
        }
    }


    public final void reloadPersistenceInfos()
    {
        if(this.disallowClear)
        {
            LOG.error("reloading schema information is disallowed\n" + Utilities.getStackTraceAsString(new RuntimeException()));
        }
        else
        {
            clearTypeCache(LoadStatus.NOT_LOADED);
            loadPersistenceInfos();
        }
    }


    public final void loadPersistenceInfos()
    {
        if(this.status == LoadStatus.NOT_LOADED)
        {
            synchronized(this)
            {
                if(this.status == LoadStatus.NOT_LOADED)
                {
                    registerHJMPFinderListeners(this.tenant.getInvalidationManager());
                    if(Utilities.isSystemInitialized(this.tenant.getDataSource()))
                    {
                        this.status = LoadStatus.LOADING;
                        loadPersistenceInfosSafe();
                    }
                }
            }
        }
    }


    private void loadPersistenceInfosSafe()
    {
        long t1 = System.currentTimeMillis();
        Transaction tx = Transaction.current();
        try
        {
            tx.begin();
        }
        catch(Exception ex)
        {
            this.status = LoadStatus.NOT_LOADED;
        }
        if(isLoading())
        {
            try
            {
                tx.enableDelayedStore(false);
                loadPersistenceInfosInternalInTX();
            }
            catch(Exception e)
            {
                this.status = LoadStatus.NOT_LOADED;
            }
            finally
            {
                try
                {
                    tx.commit();
                }
                catch(Exception e)
                {
                    LOG.error("error committing: " + e.getMessage() + " tenant " + Registry.getCurrentTenantNoFallback(), e);
                }
            }
        }
        LOG.debug("initialized persistence info (" + System.currentTimeMillis() - t1 + " ms)");
        if(LOG.isDebugEnabled())
        {
            LOG.debug("configuration loaded");
        }
    }


    private void loadPersistenceInfosInternalInTX()
    {
        if(Registry.getCurrentTenant().getConfig().getBoolean("typeinfomap.loading.batch.mode", true))
        {
            batchLoadPersistenceInfosInternalInTX();
        }
        else
        {
            legacyLoadPersistenceInfosInternalInTX();
        }
    }


    private void batchLoadPersistenceInfosInternalInTX()
    {
        Tenant tenant = Registry.getCurrentTenant();
        BatchTypeInfoMapLoader loader = new BatchTypeInfoMapLoader(tenant);
        clearTypeCache(LoadStatus.NOT_LOADED);
        List<TypeInfoMap> typeInfoMaps = loader.loadAllTypes();
        LoadDeploymentInfoResult deploymentInfo = loader.getLoadDeploymentInfoResult();
        this.javaDefMapping = deploymentInfo.getJavaDefMapping();
        this.typeCode2JndiMappings = deploymentInfo.getTypeCode2JndiMappings();
        this.deploymentInfos = deploymentInfo.getDeploymentInfos();
        for(TypeInfoMap info : typeInfoMaps)
        {
            if(TypeInfoMap.EMPTY_INFOMAP.equals(info))
            {
                LOG.warn("problem getting TypeInfoMap for " + info.getCode() + ". Possible reason: The extension which contains this type is not in your (local)extensions.xml.");
            }
            cacheInfo(info);
        }
        setLoaded();
    }


    public void legacyLoadPersistenceInfosInternalInTX()
    {
        SystemEJB systemEJB = this.tenant.getSystemEJB();
        PersistenceInfoCache pic = restorePersistenceInfoCache();
        if(pic != null)
        {
            this.cacheVariable.set(pic);
            setLoaded();
        }
        else
        {
            clearTypeCache(LoadStatus.NOT_LOADED);
            loadAllDeployments();
            boolean success = false;
            try
            {
                loadAllTypes(systemEJB);
                setLoaded();
                success = true;
            }
            finally
            {
                if(success)
                {
                    dumpPersistenceInfoCache(this.cacheVariable.get());
                }
            }
        }
    }


    private void loadAllTypes(SystemEJB systemEJB)
    {
        Collection types = systemEJB.getTypeManager().getAllItemTypes();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("loading persistence info for " + types.size() + " types");
        }
        for(Iterator<ComposedTypeRemote> it = types.iterator(); it.hasNext(); )
        {
            ComposedTypeRemote type = it.next();
            TypeInfoMap info = loadInfo(type, systemEJB.getTypeManager());
            if(info.equals(TypeInfoMap.EMPTY_INFOMAP))
            {
                LOG.warn("problem getting TypeInfoMap for " + type.getCode() + ". Possible reason: The extension which contains this type is not in your (local)extensions.xml.");
            }
            cacheInfo(info);
        }
        setLoaded();
    }


    public boolean cachesInfoFor(PK typePK)
    {
        return cache().cachesInfoFor(typePK);
    }


    public boolean isLoaded()
    {
        return (this.status == LoadStatus.LOADED);
    }


    public boolean isDestroyed()
    {
        return (this.status == LoadStatus.DESTROYED);
    }


    public boolean isLoading()
    {
        return (this.status == LoadStatus.LOADING);
    }


    public Set<ItemDeployment> getAllDeployments()
    {
        loadAllDeployments();
        Set<ItemDeployment> ret = new HashSet<>(this.deploymentInfos.size());
        for(Map.Entry<String, ItemDeployment> entry : this.deploymentInfos.entrySet())
        {
            ret.add(entry.getValue());
        }
        return ret;
    }


    public ItemDeployment getItemDeployment(String beanOrJNDIName)
    {
        loadAllDeployments();
        return this.deploymentInfos.get(beanOrJNDIName);
    }


    private void loadAllDeployments()
    {
        if(this.deploymentInfos == null)
        {
            synchronized(this)
            {
                if(this.deploymentInfos == null)
                {
                    DeploymentInfoProvider provider = new DeploymentInfoProvider();
                    LoadDeploymentInfoResult loadDeploymentInfoResult = provider.loadAllDeploymentsForTenant((Tenant)this.tenant);
                    this.javaDefMapping = loadDeploymentInfoResult.getJavaDefMapping();
                    this.typeCode2JndiMappings = loadDeploymentInfoResult.getTypeCode2JndiMappings();
                    this.deploymentInfos = loadDeploymentInfoResult.getDeploymentInfos();
                }
            }
        }
    }


    public Map<Integer, ItemDeployment> getDuplicatedItemDeployments()
    {
        Map<Integer, ItemDeployment> duplicatedDeploymentsMap = new HashMap<>(16);
        loadAllDeployments();
        Map<String, ItemDeployment> nonAbstractDeployments = new HashMap<>();
        for(Iterator<ItemDeployment> it = this.deploymentInfos.values().iterator(); it.hasNext(); )
        {
            ItemDeployment info = it.next();
            if(!info.isAbstract())
            {
                ItemDeployment sec = nonAbstractDeployments.put(getTypeCodeDeploymentTableKey(info), info);
                if(sec != null)
                {
                    if(YDeploymentJDBC.existsDeployment(sec))
                    {
                        Integer typeCode = Integer.valueOf(sec.getTypeCode());
                        duplicatedDeploymentsMap.put(typeCode, sec);
                        continue;
                    }
                    if(YDeploymentJDBC.existsDeployment(info))
                    {
                        Integer typeCode = Integer.valueOf(info.getTypeCode());
                        duplicatedDeploymentsMap.put(typeCode, info);
                        continue;
                    }
                    LOG.warn("There has been found duplicated deployment for type code " + info.getTypeCode() + ",\nhowever non of them (orignal or duplicated) seem to be deployed on the DB,\nplease verify the items.xml for duplicated types.");
                }
            }
        }
        return duplicatedDeploymentsMap;
    }


    private String getTypeCodeDeploymentTableKey(ItemDeployment info)
    {
        Integer tc = Integer.valueOf(info.getTypeCode());
        return "" + tc + "_" + tc;
    }


    public ItemDeployment getItemDeployment(int tc)
    {
        String jndi = getJNDINamePrivate(tc);
        return (jndi != null) ? getItemDeployment(jndi) : null;
    }


    public Collection<ItemDeployment> getAllSubDeployments(ItemDeployment depl)
    {
        Collection<ItemDeployment> ret = new ArrayList();
        for(Iterator<Map.Entry> iter = this.deploymentInfos.entrySet().iterator(); iter.hasNext(); )
        {
            Map.Entry e = iter.next();
            ItemDeployment d = (ItemDeployment)e.getValue();
            if(d != depl && d.getSuperDeploymentName() != null && d.getSuperDeploymentName().equalsIgnoreCase(depl.getName()))
            {
                ret.add(d);
            }
        }
        return ret;
    }


    private String getJNDINamePrivate(int typeCode)
    {
        loadAllDeployments();
        return this.typeCode2JndiMappings.get(Integer.valueOf(typeCode));
    }


    public String getJNDIName(int typeCode)
    {
        loadAllDeployments();
        return this.typeCode2JndiMappings.get(Integer.valueOf(typeCode));
    }


    public String getJNDIName(String code)
    {
        TypeInfoMap info = getPersistenceInfo(code);
        return (info != null && !info.isEmpty()) ? getJNDIName(info.getItemTypeCode()) : null;
    }


    public PK getTypePK(String code)
    {
        TypeInfoMap info = getPersistenceInfo(code);
        return (info != null && !info.isEmpty()) ? info.getTypePK() : null;
    }


    public boolean isRootRelationType(String code)
    {
        if(isDestroyed())
        {
            throw new IllegalStateException("Persistence manager " + this + " is destroyed - cannot determine root relation type status for " + code);
        }
        return cache().isCachedRootRelationType(code);
    }


    private Collection<PK> getAllSubtypes(PK typePK)
    {
        Collection<PK> ret = null;
        Collection<PK> coll = getSubtypes(typePK);
        while(CollectionUtils.isNotEmpty(coll))
        {
            Collection<PK> newSubtypes = null;
            for(PK subtypePK : coll)
            {
                if(ret == null)
                {
                    ret = new HashSet<>();
                }
                ret.add(subtypePK);
                Collection<PK> subSubtypes = getSubtypes(subtypePK);
                if(CollectionUtils.isNotEmpty(subSubtypes))
                {
                    if(newSubtypes == null)
                    {
                        newSubtypes = new HashSet<>();
                    }
                    newSubtypes.addAll(subSubtypes);
                }
            }
            coll = newSubtypes;
        }
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    private Collection<PK> getSubtypes(PK typePK)
    {
        return cache().getCachedSubtypes(typePK);
    }


    public Set<PK> getExternalTableTypes(PK typePK)
    {
        if(isDestroyed())
        {
            throw new IllegalStateException("Persistence manager " + this + " is destroyed - cannot get external tables for " + typePK);
        }
        PersistenceInfoCache cache = cache();
        Set<PK> ret = cache.getCachedExternalTableTypes(typePK);
        if(ret == null)
        {
            ret = computeExternalTableTypes(typePK);
            cache.putExternalTableTypes(typePK, ret);
        }
        return ret;
    }


    private Set<PK> computeExternalTableTypes(PK typePK)
    {
        PersistenceInfoCache cache = cache();
        Set<PK> externalTypes = new HashSet<>();
        TypeInfoMap myInfo = getPersistenceInfo(typePK);
        if(myInfo != null)
        {
            if(myInfo.isEmpty())
            {
                return Collections.EMPTY_SET;
            }
            if(myInfo.isAbstract() && myInfo.getTableName(false) == null)
            {
                Collection<PK> subtypes = cache.getCachedSubtypes(typePK);
                if(CollectionUtils.isNotEmpty(subtypes))
                {
                    for(PK subPK : subtypes)
                    {
                        externalTypes.addAll(getExternalTableTypes(subPK));
                    }
                }
            }
            else
            {
                Set<String> tables = new HashSet<>();
                String table = myInfo.getItemTableName();
                tables.add(table);
                Collection<PK> subtypes = cache.getCachedSubtypes(typePK);
                while(CollectionUtils.isNotEmpty(subtypes))
                {
                    Collection<PK> nextSubTypes = new ArrayList<>();
                    for(PK subTypePK : subtypes)
                    {
                        TypeInfoMap subInfo = getPersistenceInfo(subTypePK);
                        if(subInfo != null)
                        {
                            if(!subInfo.isEmpty() && !subInfo.isAbstract())
                            {
                                String subTable = subInfo.getItemTableName();
                                if(!tables.contains(subTable))
                                {
                                    tables.add(subTable);
                                    externalTypes.add(subTypePK);
                                }
                            }
                            Collection<PK> subSubTypes = cache.getCachedSubtypes(subTypePK);
                            if(CollectionUtils.isNotEmpty(subSubTypes))
                            {
                                nextSubTypes.addAll(subSubTypes);
                            }
                        }
                    }
                    subtypes = nextSubTypes;
                }
            }
        }
        return externalTypes;
    }


    protected void registerHJMPFinderListeners(InvalidationManager invMan)
    {
        InvalidationTopic topic = invMan.getOrCreateInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
        topic.addInvalidationListener((InvalidationListener)new PersistenceManagerInvalidationListener(this));
        loadAllDeployments();
        for(ItemDeployment depl : getAllDeployments())
        {
            if(!depl.isAbstract())
            {
                registerHJMPFinderListeners(invMan, depl);
            }
        }
    }


    private void registerHJMPFinderListeners(InvalidationManager invMan, ItemDeployment depl)
    {
        int tc = depl.getTypeCode();
        String tcStr = PlatformStringUtils.valueOf(tc);
        InvalidationTopic topic = invMan.getOrCreateInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, tcStr});
        topic.addInvalidationListener((InvalidationListener)new HJMPFindInvalidationListener(tcStr));
    }


    private void setLoaded()
    {
        this.status = LoadStatus.LOADED;
    }


    private TypeInfoMap getCachedInfo(Object key)
    {
        if(key == null)
        {
            return TypeInfoMap.EMPTY_INFOMAP;
        }
        return cache().getCachedInfo(key);
    }


    public void clearComposedType(PK typePK, String code)
    {
        if(this.disallowClear)
        {
            LOG.error("Not clearing schema info for type pk:" + typePK + ", code:" + code + " since cache is locked!\n" +
                            Utilities.getStackTraceAsString(new RuntimeException()));
        }
        else
        {
            if(this.logClear)
            {
                LOG.warn("clearing schema info for type pk:" + typePK + ", code:" + code + "\n" +
                                Utilities.getStackTraceAsString(new RuntimeException()));
            }
            cache().clearCachedInfo(typePK, code);
        }
    }


    protected void clearTypeCache(LoadStatus newStatus)
    {
        if(this.logClear)
        {
            LOG.warn("Schema cache cleared!\n" + Utilities.getStackTraceAsString(new RuntimeException()));
        }
        this.status = LoadStatus.CLEARING;
        PersistenceInfoCache cache = this.cacheVariable.get();
        if(cache != null)
        {
            cache.clear();
        }
        this.loopDetectionSet.remove();
        this.typeCode2JndiMappings = null;
        this.javaDefMapping = null;
        this.deploymentInfos = null;
        this.status = newStatus;
    }


    public Set<Integer> getBeanTypeCodes(PK typePK)
    {
        if(isDestroyed())
        {
            throw new IllegalStateException("Persistence manager " + this + " is destroyed - cannot get bean type codes for " + typePK);
        }
        PersistenceInfoCache cache = cache();
        Set<Integer> ret = cache.getCachedBeanTypecodes(typePK);
        if(ret == null)
        {
            ret = calculateBeanTypeCodes(typePK);
            cache.putBeanTypeCodes(typePK, ret);
        }
        return ret;
    }


    private Set<Integer> calculateBeanTypeCodes(PK typePK)
    {
        Set<Integer> ret = new HashSet<>();
        TypeInfoMap info = getPersistenceInfo(typePK);
        if(info != null && !info.isAbstract() && !info.isEmpty())
        {
            ret.add(Integer.valueOf(info.getItemTypeCode()));
        }
        for(PK subtypePK : getAllSubtypes(typePK))
        {
            TypeInfoMap subtypeInfo = getPersistenceInfo(subtypePK);
            if(subtypeInfo != null && !subtypeInfo.isAbstract() && !subtypeInfo.isEmpty())
            {
                ret.add(Integer.valueOf(subtypeInfo.getItemTypeCode()));
            }
        }
        return ret;
    }


    private synchronized void cacheInfo(TypeInfoMap info)
    {
        cache().putCachedInfo(info);
    }


    private final TypeInfoMap loadInfo(ComposedTypeRemote type, TypeManagerEJB tm)
    {
        return createInfoImpl(type, tm, false, null, false);
    }


    private final TypeInfoMap createInfo(ComposedTypeRemote type, TypeManagerEJB tm, Map<String, PropertyTableDefinition> propertyTableMap, boolean reinit)
    {
        return createInfoImpl(type, tm, true, propertyTableMap, reinit);
    }


    private final TypeInfoMap createInfoImpl(ComposedTypeRemote type, TypeManagerEJB tm, boolean initial, Map<String, PropertyTableDefinition> tableMap, boolean reinit)
    {
        PK typePK = null;
        PK superTypePK = null;
        String typeCode = null;
        if(type == null)
        {
            LOG.warn("createInfoImpl: Cannot create TypeInfoMap, specified ComposedType is null!");
            return TypeInfoMap.EMPTY_INFOMAP;
        }
        try
        {
            PropertyTableDefinition tableDef;
            TypeInfoMap infoMap;
            String database = this.tenant.getDataSource().getDatabaseName();
            typePK = EJBTools.getPK((ItemRemote)type);
            superTypePK = type.getSuperTypeItemPK();
            typeCode = type.getCode();
            if(LOG.isDebugEnabled())
            {
                LOG.debug((initial ? "creating" : "loading") + " persistence info for type " + (initial ? "creating" : "loading") + " ( PK = " + type.getCode() + ")");
            }
            boolean isJaloOnly = isJaloOnly(type);
            ItemDeployment deployment = type.getDeployment();
            boolean hasOwnDeployment = (deployment != null);
            if(deployment != null && type.getSuperTypeItemPK() != null)
            {
                ItemDeployment superDepl = type.getSuperType().getDeployment();
                if(superDepl != null)
                {
                    hasOwnDeployment = (superDepl.getTypeCode() != deployment.getTypeCode());
                }
            }
            boolean isAbstract = (isJaloOnly || (type.isAbstract() && (!hasOwnDeployment || deployment.isAbstract())));
            boolean isAbstractWithoutConcreteSubTypes = (isAbstract && deployment != null && !hasConcreteSubtypeForDeployment(type, deployment.getTypeCode(), tm));
            boolean isRelationType = (!isAbstract && tm.isRelationType(type));
            boolean isViewType = isViewType(type);
            int modifiers = 0;
            if(isAbstract)
            {
                modifiers |= 0x1;
            }
            if(isJaloOnly)
            {
                modifiers |= 0x4;
            }
            if(isViewType)
            {
                modifiers |= 0x2;
            }
            String propsTable = (deployment == null) ? null : deployment.getDumpPropertyTableName();
            int tc = type.getItemTypeCode();
            String itemTableName = (deployment == null) ? null : deployment.getDatabaseTableName();
            if(isAbstractWithoutConcreteSubTypes || (itemTableName == null && !isAbstract))
            {
                infoMap = new TypeInfoMap(typePK, superTypePK, typeCode, isRelationType, modifiers);
                itemTableName = null;
            }
            else
            {
                infoMap = new TypeInfoMap(typePK, superTypePK, typeCode, tc, isRelationType, modifiers, itemTableName, (itemTableName != null) ? (itemTableName + itemTableName) : null, (itemTableName != null) ? (itemTableName + itemTableName) : null, propsTable, deployment.getAuditTableName());
            }
            if(itemTableName == null)
            {
                tableDef = null;
            }
            else if(tableMap == null)
            {
                tableDef = null;
            }
            else if(tableMap.containsKey(itemTableName))
            {
                tableDef = tableMap.get(itemTableName);
            }
            else
            {
                tableMap.put(itemTableName,
                                tableDef = new PropertyTableDefinition(infoMap.getOldPropTableName(), infoMap.getItemTableName(), infoMap.getTableName(true), tc));
            }
            Collection<String> parsed = new ArrayList();
            Collection<String> skipped = new ArrayList();
            Set<AttributeDescriptorRemote> allAttributeDescriptors = tm.getAllAttributeDescriptors(type);
            List<AttributeDescriptorRemote> allSortedAttributeDescriptors = sortInheritedAttributesFirst(allAttributeDescriptors);
            for(Iterator<AttributeDescriptorRemote> it2 = allSortedAttributeDescriptors.iterator(); it2.hasNext(); )
            {
                AttributeDescriptorRemote fd = it2.next();
                String quali = fd.getQualifier();
                String columnName = fd.getDatabaseColumn();
                boolean doSetColumn = (columnName == null && (initial || isAbstract) && !isViewType && !isJaloOnly);
                if(doSetColumn)
                {
                    columnName = tm.getProposedDatabaseColumn(fd);
                }
                if((fd.getModifiers() & 0x4000) == 16384)
                {
                    infoMap.addToEncryptedProperties(quali);
                }
                if(fd.dontOptimize())
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("forced to skip '" + quali + "' (TIM_IGNORE = true)");
                    }
                    skipped.add(quali);
                    continue;
                }
                if(columnName == null)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("skipped '" + quali + "' since there is no column name");
                    }
                    skipped.add(quali);
                    continue;
                }
                Class javaClass = fd.getPersistenceClass();
                if(javaClass == null)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("skipped '" + quali + "' since there is no java class");
                    }
                    skipped.add(quali);
                    continue;
                }
                boolean localized = fd.isLocalized();
                boolean core = (!fd.isProperty() && !localized);
                if(tableDef != null && !core)
                {
                    columnName = tableDef.addColumn(quali
                                                    .toLowerCase(), columnName, javaClass,
                                    getCustomColumnDescription(fd, database), localized,
                                    shouldTrimColumn(fd, columnName));
                }
                infoMap.add(quali, columnName, javaClass, core, localized);
                parsed.add(quali);
                if(doSetColumn)
                {
                    fd.setDatabaseColumn(columnName);
                }
            }
            if(type.getPropertyTableStatus() || initial)
            {
                infoMap.setTablesInitialized();
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("finished TypeInfoMap for " + type.getCode() + "(parsed " + parsed + ", skipped " + skipped + " )");
            }
            return infoMap;
        }
        catch(Exception e)
        {
            e.printStackTrace(System.err);
            return TypeInfoMap.EMPTY_INFOMAP;
        }
    }


    private List<AttributeDescriptorRemote> sortInheritedAttributesFirst(Set<AttributeDescriptorRemote> allAttributeDescriptorsSet)
    {
        List<AttributeDescriptorRemote> allAttributeDescriptorsList = new ArrayList<>(allAttributeDescriptorsSet);
        Collections.sort(allAttributeDescriptorsList, (Comparator<? super AttributeDescriptorRemote>)new Object(this));
        return allAttributeDescriptorsList;
    }


    private boolean hasConcreteSubtypeForDeployment(ComposedTypeRemote type, int tc, TypeManagerEJB tm)
    {
        for(ComposedTypeRemote subType : tm.getSubTypes((HierarchieTypeRemote)type))
        {
            if(subType.getItemTypeCode() == tc)
            {
                if(!subType.isAbstract())
                {
                    return true;
                }
                if(hasConcreteSubtypeForDeployment(subType, tc, tm))
                {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean isViewType(ComposedTypeRemote type)
    {
        Class<?> metaTypeJaloClass = getJaloClass(type.getComposedType());
        return (metaTypeJaloClass != null && ViewType.class.isAssignableFrom(metaTypeJaloClass));
    }


    private boolean isJaloOnly(ComposedTypeRemote type)
    {
        boolean jaloOnly = Boolean.TRUE.equals(type.getProperty(ComposedType.JALOONLY));
        if(!jaloOnly)
        {
            Class<?> jaloClass = getJaloClass(type);
            jaloOnly = (jaloClass != null && JaloOnlyItem.class.isAssignableFrom(jaloClass));
        }
        return jaloOnly;
    }


    private Class getJaloClass(ComposedTypeRemote type)
    {
        Class cl = getDeclaredJaloClass(type);
        if(cl == null)
        {
            for(ComposedTypeRemote superType = type.getSuperType(); cl == null && superType != null;
                            superType = superType.getSuperType())
            {
                cl = getJaloClass(superType);
            }
        }
        return cl;
    }


    private Class getDeclaredJaloClass(ComposedTypeRemote type)
    {
        String className = null;
        try
        {
            className = type.getJaloClassName();
            return (className != null) ? CGLibUtils.getOriginalClass(Class.forName(className)) : null;
        }
        catch(ClassNotFoundException e)
        {
            return null;
        }
    }


    private boolean shouldTrimColumn(AttributeDescriptorRemote fd, String columnName)
    {
        return !columnName.equals(fd.getDatabaseColumn());
    }


    private String getCustomColumnDescription(AttributeDescriptorRemote fd, String database)
    {
        Map sqlColumnDescriptions = (Map)fd.getProperty("sqlcolumndescriptions");
        if(sqlColumnDescriptions != null)
        {
            if(sqlColumnDescriptions.containsKey(database))
            {
                return (String)sqlColumnDescriptions.get(database);
            }
            if(sqlColumnDescriptions.containsKey("_no_db_"))
            {
                return (String)sqlColumnDescriptions.get("_no_db_");
            }
        }
        return null;
    }


    public void simulateFullClear()
    {
        clearTypeCache(LoadStatus.NOT_LOADED);
    }


    public void clearBeforeDropTables()
    {
        clearTypeCache(LoadStatus.NOT_LOADED);
    }


    public void destroy()
    {
        clearTypeCache(LoadStatus.DESTROYED);
    }
}
