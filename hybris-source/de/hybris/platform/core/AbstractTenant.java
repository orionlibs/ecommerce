package de.hybris.platform.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.ddl.PropertiesLoader;
import de.hybris.bootstrap.ddl.tools.TypeSystemHelper;
import de.hybris.bootstrap.util.DeploymentMigrationUtil;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTarget;
import de.hybris.platform.cache.impl.CacheFactory;
import de.hybris.platform.cache.impl.StaticCache;
import de.hybris.platform.cluster.DefaultBroadcastService;
import de.hybris.platform.cluster.DefaultClusterNodeManagementService;
import de.hybris.platform.core.system.InitializationLockDao;
import de.hybris.platform.core.system.InitializationLockHandler;
import de.hybris.platform.core.system.InitializationLockInfo;
import de.hybris.platform.core.system.MSSQLServerTransactionParameters;
import de.hybris.platform.core.system.query.QueryProvider;
import de.hybris.platform.core.system.query.impl.QueryProviderFactory;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.licence.Licence;
import de.hybris.platform.licence.internal.ValidationResult;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.framework.PersistencePool;
import de.hybris.platform.persistence.numberseries.DefaultCachingSerialNumberGenerator;
import de.hybris.platform.persistence.numberseries.DefaultSerialNumberDAO;
import de.hybris.platform.persistence.numberseries.SerialNumberDAO;
import de.hybris.platform.persistence.numberseries.SerialNumberGenerator;
import de.hybris.platform.persistence.property.DBPersistenceManager;
import de.hybris.platform.persistence.property.HJMPCachePopulator;
import de.hybris.platform.persistence.property.PersistenceManager;
import de.hybris.platform.property.interceptor.ServerStartupPropertiesInterceptor;
import de.hybris.platform.property.interceptor.impl.CompositeServerStartupPropertiesInterceptor;
import de.hybris.platform.property.interceptor.impl.DefaultServerStartupPropertiesInterceptionProcessor;
import de.hybris.platform.property.interceptor.impl.ServerStartupLog4jPropertiesInterceptor;
import de.hybris.platform.property.interceptor.impl.ServerStartupTomcatPropertiesInterceptor;
import de.hybris.platform.servicelayer.event.events.AfterTenantRestartEvent;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerManager;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.SingletonCreator;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.logging.HybrisLogger;
import de.hybris.platform.util.threadpool.ThreadPool;
import java.io.File;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sql.DataSource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public abstract class AbstractTenant implements Tenant, Serializable
{
    public static final String SLAVE_DATASOURCE = "slave.datasource";
    public static final String ALT_DATASOURCE = "alt.datasource";
    public static final String MASTER_DATASOURCE_ID = "master";
    private static final Logger LOGGER = Logger.getLogger(AbstractTenant.class.getName());
    private static final Logger SERVER_LOGGER = Logger.getLogger("hybrisserver");
    private static final TenantListener JMX_BEAN_RECORDER = (TenantListener)new JMXBeanLoader();
    private static final String SHOULD_MIGRATE_CORE_TYPES = "should.migrate.core.types";
    private static final String TENANT_CONNECTION_CHECK_ENABLED_PROPERTY = "tenant.connection.check.enabled";
    private static final String SSO_METADATA_LOCATION_PARAM = "sso.metadata.location";
    private static final String SAMLSINGLESIGNON_EXT_NAME = "samlsinglesignon";
    private final UUID uuid = UUID.randomUUID();
    private Boolean licenseValid;
    private String licenceValidationMessage;
    private Boolean propertiesValid;
    private Map<String, String> missingParameters;
    private final List<Thread> backgroundThreads = new CopyOnWriteArrayList<>();
    private volatile State state = State.INACTIVE;
    private volatile boolean currentlyInitializing = false;
    private volatile boolean currentlyStarting = false;
    private volatile boolean currentlyStopping = false;
    private volatile boolean currentlyNotifyingListeners = false;
    private final TenantStartupNotifier tenantStartupNotifier;
    private final ThreadLocal<Boolean> inSystemInit = new ThreadLocal<>();
    private String tenantID = null;
    private AllDataSourcesHolder dataSourcesHolder;
    private final HybrisDataSourceBuilder dataSourceBuilder = new HybrisDataSourceBuilder(this);
    private int lastChosenSlaveIdx = -1;
    private InvalidationManager iMan;
    private Cache cache;
    private PersistenceManager persistenceManager;
    private PersistencePool persPool;
    private SystemEJB sysEJB;
    private ThreadPool threadPool;
    private ThreadPool workersPool;
    private SingletonCreator singletonCreator;
    private SerialNumberGenerator numberGenerator;
    private JaloConnection jaloConnection;
    private volatile boolean connectionBroken = false;
    private static boolean infoDisplayed = false;
    private static final boolean tenantLifecycleLoggingEnabled = Boolean.parseBoolean(
                    System.getProperty("tenant.lifecycle.logging"));
    private final ThreadLocal<JaloSession> sessionTL = new ThreadLocal<>();
    private final ThreadLocal<List<SessionContext>> sessionContextTL = (ThreadLocal<List<SessionContext>>)new Object(this);
    private volatile GenericApplicationContext applicationContext = null;
    private final AtomicLong tenantRestartMarker = new AtomicLong(Long.MIN_VALUE);


    public abstract List<String> getTenantSpecificExtensionNames();


    public abstract ConfigIntf getConfig();


    public abstract void startUp() throws ConsistencyCheckException;


    public abstract void shutDown();


    abstract void shutDown(ShutDownMode paramShutDownMode);


    public static Tenant getCurrentTenant()
    {
        return Registry.getCurrentTenant();
    }


    protected AbstractTenant(String tenantID)
    {
        setTenantID(tenantID);
        if(!getTenantListeners().contains(JMX_BEAN_RECORDER))
        {
            Registry.registerTenantListener(JMX_BEAN_RECORDER);
        }
        this.tenantStartupNotifier = TenantStartupNotifier.createTenantStartupNotifier(this);
    }


    private void setTenantRestartMarker(ShutDownMode mode)
    {
        if(mode == ShutDownMode.INITIALIZATION)
        {
            resetTenantRestartMarker();
        }
    }


    public void setSystemInit(Boolean state)
    {
        this.inSystemInit.set(state);
    }


    private long getTenantRestartMarkerFromDB()
    {
        long tenantRestartMarkerFromDB = -1L;
        try
        {
            if(Utilities.isSystemInitialized(getDataSource()))
            {
                tenantRestartMarkerFromDB = getSystemEJB().getMetaInformationManager().getSystemInitUpdateTimestamp();
            }
        }
        catch(IllegalStateException e)
        {
            LOGGER.warn("error getting tenant restart marker from db, using current time", e);
        }
        if(tenantRestartMarkerFromDB != -1L)
        {
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("got tenant restart marker from db " + tenantRestartMarkerFromDB);
            }
            return tenantRestartMarkerFromDB;
        }
        long currentTimestamp = System.currentTimeMillis();
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("got tenant restart marker from current time: " + currentTimestamp);
        }
        return currentTimestamp;
    }


    public long getTenantRestartMarker()
    {
        long currentMarker;
        while(true)
        {
            currentMarker = this.tenantRestartMarker.get();
            if(currentMarker == Long.MIN_VALUE)
            {
                long tenantRestartMarkerFromDB = getTenantRestartMarkerFromDB();
                boolean markerUpdated = this.tenantRestartMarker.compareAndSet(Long.MIN_VALUE, tenantRestartMarkerFromDB);
                if(markerUpdated)
                {
                    return tenantRestartMarkerFromDB;
                }
                continue;
            }
            break;
        }
        return currentMarker;
    }


    protected void doInitialize()
    {
        if(getState() == State.INACTIVE)
        {
            synchronized(this)
            {
                if(getState() == State.INACTIVE)
                {
                    if(tenantLifecycleLoggingEnabled)
                    {
                        System.out.println("Tenant " + getTenantID() + " is being initialized...");
                    }
                    doInitializeSafe();
                    if(tenantLifecycleLoggingEnabled)
                    {
                        System.out.println("Tenant " + getTenantID() + " is initialized.");
                    }
                }
            }
        }
    }


    private void doInitializeSafe()
    {
        assertNotCurrentlyInitializing();
        disableLogListeners();
        assertCurrentTenant();
        this.currentlyInitializing = true;
        try
        {
            if(validateProperties())
            {
                this.dataSourcesHolder = this.dataSourceBuilder.createDataSourceFactory(getConfig()).build(this.dataSourcesHolder);
            }
        }
        finally
        {
            this.currentlyInitializing = false;
            enableLogListeners();
            finishInitialization((this.dataSourcesHolder != null));
        }
    }


    HybrisContextHolder getContextHolder()
    {
        return HybrisContextHolder.getInstance();
    }


    GenericApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    protected void relaseAdministrationLockIfNeeded(HybrisDataSource masterDataSource)
    {
        Object object = new Object(this, masterDataSource);
        InitializationLockHandler handler = new InitializationLockHandler((InitializationLockDao)object);
        if(handler.isLocked())
        {
            InitializationLockInfo info = handler.getLockInfo();
            if(info.getInstanceIdentifier() != object.getUniqueInstanceIdentifier())
            {
                if(info.getClusterNodeId() == getClusterID())
                {
                    SERVER_LOGGER.info("Found existing administration lock for the current cluster node, possibly previous administration task '" + info
                                    .getProcessName() + "' started at " + info
                                    .getDate() + " was interrupted suddenly.");
                    if(object.releaseRow(this) || !handler.isLocked())
                    {
                        SERVER_LOGGER.info("Released existing administration lock successfully.");
                    }
                }
                else
                {
                    SERVER_LOGGER
                                    .info("Can't release existing administration lock, possibly there is a pending adminstration task '" + info
                                                    .getProcessName() + "' for tenant <<" + info.getTenantId() + ">> on cluster node " + info
                                                    .getClusterNodeId() + " started at " + info.getDate() + ".\nPlease verify if some administration task is pending on " + info
                                                    .getClusterNodeId() + " cluster node.");
                }
            }
        }
    }


    private void disableLogListeners()
    {
        HybrisLogger.disableListeners();
    }


    private void enableLogListeners()
    {
        HybrisLogger.enableListeners();
    }


    private void initializeSingletons(HybrisDataSource masterDS)
    {
        this.singletonCreator = new SingletonCreator();
        this.numberGenerator = (SerialNumberGenerator)new DefaultCachingSerialNumberGenerator(this, (SerialNumberDAO)new DefaultSerialNumberDAO(this, masterDS));
        this.jaloConnection = new JaloConnection(this);
    }


    private void initializeThreadPools()
    {
        this.threadPool = createThreadPool();
        this.workersPool = createWorkerThreadPool();
    }


    private void initializePersistence(InvalidationManager invalidationManager, Cache cache)
    {
        this.persPool = createPersistencePool();
        this.persPool.registerHJMPListeners(invalidationManager);
        Item.registerJaloInvalidationListeners(invalidationManager, cache);
        this.sysEJB = new SystemEJB(this);
        this.persistenceManager = createPersistenceManager(invalidationManager);
    }


    protected void initializeCache(HybrisDataSource masterDataSource)
    {
        this.cache = createCache();
        this.iMan = createInvalidationManager(this.cache);
    }


    private void finishInitialization(boolean success)
    {
        if(success)
        {
            setState(State.INITIALIZED);
            Registry.registerTenantInitializationTenant(getTenantID());
        }
        else
        {
            setState(State.INACTIVE);
        }
    }


    private void logInitializationInfo(ConfigIntf cfg, HybrisDataSource masterDS, Cache cache)
    {
        if(LOGGER.isInfoEnabled() && !infoDisplayed)
        {
            String dbUrl = masterDS.getDatabaseURL();
            int cut = (dbUrl.indexOf('?') == -1) ? dbUrl.indexOf('&') : dbUrl.indexOf('?');
            if(cut != -1)
            {
                dbUrl = dbUrl.substring(0, cut);
            }
            String jndiName = masterDS.getJNDIName();
            String osName = cfg.getParameter("os.name");
            String osVersion = cfg.getParameter("os.version");
            String osArch = cfg.getParameter("os.arch");
            String runtimeName = cfg.getParameter("java.runtime.name");
            String runtimeverison = cfg.getParameter("java.runtime.version");
            String javaVersion = cfg.getParameter("java.vendor");
            String javaHome = cfg.getParameter("java.home");
            String userLang = System.getProperty("user.language");
            String userCountry = System.getProperty("user.country");
            String userRegion = System.getProperty("user.region");
            String buildVersion = cfg.getParameter("build.version");
            String deployedServerType = cfg.getParameter("deployed.server.type");
            boolean legacyCacheMode = cfg.getBoolean("cache.legacymode", false);
            boolean developmentMode = cfg.getBoolean("development.mode", true);
            infoDisplayed = true;
            SERVER_LOGGER.info("");
            SERVER_LOGGER.info("*****************************************************************");
            SERVER_LOGGER.info("");
            SERVER_LOGGER.info("Starting up hybris server");
            SERVER_LOGGER.info("");
            SERVER_LOGGER.info("Configuration:");
            SERVER_LOGGER.info("");
            if(isClusteringEnabled())
            {
                SERVER_LOGGER.info("Cluster:     " + getClusterID() + " dynamic ID:" + getDynamicClusterNodeID());
            }
            else
            {
                SERVER_LOGGER.info("Cluster:     " + (
                                Licence.getDefaultLicence().isClusteringPermitted() ? "disabled" : "not permitted by licence"));
            }
            SERVER_LOGGER.info("Tenant:      " + getTenantID());
            SERVER_LOGGER.info("Mode:        " + ((developmentMode == true) ? "development" : "production"));
            SERVER_LOGGER.info("OS:          " + osName + " " + osVersion + ", " + osArch);
            SERVER_LOGGER.info("Database:    " + ((jndiName == null) ? "Pool: hybris - " : ("Pool: JNDI=" + jndiName + " - ")) + masterDS
                            .getDatabaseName() + ", table prefix: " + (
                            (masterDS.getTablePrefix() != null) ? masterDS.getTablePrefix() : "<none>"));
            SERVER_LOGGER.info("             " + masterDS.getDatabaseUser() + " - " + dbUrl);
            Map<String, String> customParams = extractCustomDBParams(masterDS.getConnectionParameters(), true);
            if(MapUtils.isNotEmpty(customParams))
            {
                SERVER_LOGGER.info("             custom params: " + customParams.keySet());
            }
            SERVER_LOGGER.info("Platform:    " + buildVersion);
            SERVER_LOGGER.info("Java:        " + javaVersion);
            SERVER_LOGGER.info("             " + runtimeName + ", " + runtimeverison);
            SERVER_LOGGER.info("             " + javaHome);
            SERVER_LOGGER.info("VM Locale:   language=" + userLang + ",country=" + userCountry + ",region=" + userRegion);
            SERVER_LOGGER.info("Cache:       " + (
                            legacyCacheMode ? ("" + cache.getMaxAllowedSize() + " entries. ") : "RegionCache is not yet initialized. ") + "[" + cache
                            .getClass().getName() + "]");
            if(!StringUtils.isEmpty(deployedServerType))
            {
                SERVER_LOGGER.info("Server type: " + deployedServerType);
            }
            if(!compareServerTypesString(deployedServerType, getConfig().getParameter("bundled.server.type")))
            {
                SERVER_LOGGER.info("*********************************************************");
                SERVER_LOGGER.info("*************************WARNING*************************");
                SERVER_LOGGER.info("*********************************************************");
                SERVER_LOGGER.info(String.format("Server type configuration incompatibility detected,\nconfigured one is <%s> , deployed one is <%s>.", new Object[] {getConfig().getParameter("bundled.server.type"), deployedServerType}));
                if(getConfig().getString("bundled.server.type", "").equalsIgnoreCase("tomcat"))
                {
                    SERVER_LOGGER.info("Please verify if you started the standalone hybriserver correctly.");
                }
                SERVER_LOGGER.info("*********************************************************");
                SERVER_LOGGER.info("***********************WARNING***************************");
                SERVER_LOGGER.info("*********************************************************");
            }
            SERVER_LOGGER.info("");
            SERVER_LOGGER.info("*****************************************************************");
            SERVER_LOGGER.info("");
            logExtensionSetup(SERVER_LOGGER, buildVersion);
            if("sqlserver".equals(masterDS.getDatabaseName()))
            {
                MSSQLServerTransactionParameters msSqlParams = readParameters((DataSource)masterDS);
                if(!msSqlParams.isReadCommittedSnapshotOn())
                {
                    SERVER_LOGGER.warn("");
                    SERVER_LOGGER.warn("*****************************************************************************");
                    SERVER_LOGGER.warn("******************************   WARNING   **********************************");
                    SERVER_LOGGER.warn("*****************************************************************************");
                    SERVER_LOGGER.warn("************  Parameter READ_COMMITTED_SNAPSHOT is turned off!!  ************");
                    SERVER_LOGGER.warn("** Without this parameter set if you are using ImpEx to run multi-threaded **");
                    SERVER_LOGGER.warn("** imports, you may experience deadlocks. Hybris recommends to have this   **");
                    SERVER_LOGGER.warn("*********** parameter set. You can set it in the following way: *************");
                    SERVER_LOGGER.warn("******** ALTER DATABASE database_name SET READ_COMMITTED_SNAPSHOT ON; *******");
                    SERVER_LOGGER.warn("*****************************************************************************");
                    SERVER_LOGGER.warn("*****************************************************************************");
                    SERVER_LOGGER.warn("");
                }
                if(!msSqlParams.isSnapshotIsolationStateOn())
                {
                    SERVER_LOGGER.warn("");
                    SERVER_LOGGER.warn("*****************************************************************************");
                    SERVER_LOGGER.warn("******************************   WARNING   **********************************");
                    SERVER_LOGGER.warn("*****************************************************************************");
                    SERVER_LOGGER.warn("************ Parameter ALLOW_SNAPSHOT_ISOLATION is turned off!!  ************");
                    SERVER_LOGGER.warn("** Without this parameter set if you are using ImpEx to run multi-threaded **");
                    SERVER_LOGGER.warn("** imports, you may experience deadlocks. Hybris recommends to have this   **");
                    SERVER_LOGGER.warn("*********** parameter set. You can set it in the following way: *************");
                    SERVER_LOGGER.warn("******* ALTER DATABASE database_name SET ALLOW_SNAPSHOT_ISOLATION ON; *******");
                    SERVER_LOGGER.warn("*****************************************************************************");
                    SERVER_LOGGER.warn("*****************************************************************************");
                    SERVER_LOGGER.warn("");
                }
            }
            interceptServerStartupProperties(cfg);
            logMisconfigurationWarnings(cfg);
        }
    }


    private void interceptServerStartupProperties(ConfigIntf cfg)
    {
        CompositeServerStartupPropertiesInterceptor compositeServerStartupPropertiesInterceptor = CompositeServerStartupPropertiesInterceptor.builder().register((ServerStartupPropertiesInterceptor)new ServerStartupLog4jPropertiesInterceptor(cfg, LOGGER))
                        .register((ServerStartupPropertiesInterceptor)new ServerStartupTomcatPropertiesInterceptor(LOGGER)).build();
        DefaultServerStartupPropertiesInterceptionProcessor defaultServerStartupPropertiesInterceptionProcessor = new DefaultServerStartupPropertiesInterceptionProcessor(cfg, (ServerStartupPropertiesInterceptor)compositeServerStartupPropertiesInterceptor);
        defaultServerStartupPropertiesInterceptionProcessor.startProcessing();
    }


    private void logMisconfigurationWarnings(ConfigIntf config)
    {
        PlatformConfig platformConfig = Utilities.getPlatformConfig();
        if(platformConfig.getExtensionInfo("samlsinglesignon") != null)
        {
            boolean exists = false;
            String filename = config.getString("sso.metadata.location", null);
            if(filename != null)
            {
                exists = fileExistsAsFileOrResource(filename);
            }
            if(!exists)
            {
                SERVER_LOGGER.warn("Warning: Extension samlsinglesignon is configured but sso.metadata.location is not set or points to non existing file (" + filename + ")!");
                SERVER_LOGGER.warn("---------------------------------------------------------------");
            }
        }
    }


    private boolean fileExistsAsFileOrResource(String filename)
    {
        boolean exists = (new File(filename)).exists();
        if(!exists)
        {
            try
            {
                DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
                exists = defaultResourceLoader.getResource(filename).exists();
            }
            catch(IllegalArgumentException ex)
            {
                SERVER_LOGGER.debug(
                                String.format("It is not possible to verify that the '%s' filename exists as a file or resource due to error: %s", new Object[] {filename, ex.getMessage()}));
            }
        }
        return exists;
    }


    public MSSQLServerTransactionParameters readParameters(DataSource dataSource)
    {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        try
        {
            QueryProvider qp = (new QueryProviderFactory("sqlserver")).getQueryProviderInstance();
            return (MSSQLServerTransactionParameters)template.queryForObject(qp
                            .getQueryForTransactionsIsolation(), (RowMapper)new Object(this));
        }
        catch(DataAccessException e)
        {
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("Can't read lock info :" + e.getMessage(), (Throwable)e);
            }
            return null;
        }
    }


    private void logExtensionSetup(Logger log, String platformBuildVersion)
    {
        for(String s : Utilities.getPlatformConfig().getExtensionSetupInfo(platformBuildVersion).split("[\n]"))
        {
            log.info(s);
        }
    }


    private void assertNotCurrentlyInitializing()
    {
        if(this.currentlyInitializing)
        {
            throw new IllegalStateException("Recursive call of doInitialize() detected!");
        }
    }


    private void assertCurrentTenant()
    {
        if(this != Registry.getCurrentTenantInternal())
        {
            throw new IllegalStateException("cannot startup " + this + " since it is not yet current system. current system is: " +
                            Registry.getCurrentTenantInternal());
        }
    }


    private boolean compareServerTypesString(String deployed, String bundled)
    {
        if(!StringUtils.isEmpty(deployed) && !StringUtils.isEmpty(bundled))
        {
            return bundled.equalsIgnoreCase(deployed);
        }
        return true;
    }


    public final void doStartUp() throws ConsistencyCheckException
    {
        try
        {
            State preInitState = getState();
            doInitialize();
            if(getState() == State.INITIALIZED)
            {
                synchronized(this)
                {
                    if(getState() == State.INITIALIZED)
                    {
                        doStartupSafe(preInitState);
                    }
                }
            }
        }
        catch(UninstantiableCoreApplicationContextException e)
        {
            e.printStackTrace(System.err);
            System.err.println(e.getMessage() + " Shutting down hybris platform since the system cannot be used without working Spring context...");
            System.exit(-1);
        }
        finally
        {
            if(Boolean.FALSE.equals(this.licenseValid))
            {
                Utilities.failLicence(this.licenceValidationMessage, Licence.getDefaultLicence());
            }
            if(Boolean.FALSE.equals(this.propertiesValid))
            {
                Utilities.failProperties(this.missingParameters);
            }
        }
    }


    GenericApplicationContext createCoreApplicationContext()
    {
        return getContextHolder().getApplicationInstance(this);
    }


    private void doStartupSafe(State preInitState) throws ConsistencyCheckException, UninstantiableCoreApplicationContextException
    {
        if(this.currentlyStarting)
        {
            throw new IllegalStateException("recursive call to doStartup()");
        }
        if(tenantLifecycleLoggingEnabled)
        {
            System.out.println("Tenant " + getTenantID() + " is starting up...");
        }
        this.currentlyStarting = true;
        boolean success = false;
        boolean notifyAboutRestart = false;
        RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
        try
        {
            try
            {
                if(validateLicence())
                {
                    assureTypeSystemStructureIsUpToDate();
                    getPersistenceManager().loadPersistenceInfos();
                    if(getConfig().getBoolean("should.migrate.core.types", false))
                    {
                        migrateCoreTypes();
                    }
                    initNumberSeriesPersistence();
                    this.connectionBroken = cannotConnect();
                    startUp();
                    if(this.applicationContext == null)
                    {
                        try
                        {
                            this.applicationContext = createCoreApplicationContext();
                        }
                        catch(Exception e)
                        {
                            throw new UninstantiableCoreApplicationContextException("Error creating Spring application context.", e);
                        }
                    }
                    else
                    {
                        notifyAboutRestart = true;
                    }
                    if(getState() == State.INACTIVE)
                    {
                        success = false;
                    }
                    else
                    {
                        success = true;
                    }
                }
            }
            finally
            {
                this.currentlyStarting = false;
                if(tenantLifecycleLoggingEnabled)
                {
                    System.out.println("Tenant " +
                                    getTenantID() + " has " + (success ? "successfully" : "incorrectly") + " started up.");
                }
                if(success)
                {
                    setState(State.STARTED);
                    populateCacheHintForHJMP();
                    if(preInitState == State.INACTIVE)
                    {
                        this.tenantStartupNotifier.scheduleNotifyTenantListenersAboutStartup(
                                        (List)ImmutableList.copyOf(getTenantListeners()));
                    }
                    if(notifyAboutRestart)
                    {
                        AfterTenantRestartEvent evt = new AfterTenantRestartEvent(this);
                        evt.setTenantId(getTenantID());
                        this.applicationContext.publishEvent((ApplicationEvent)evt);
                        ServicelayerManager.getInstance().notifyTenantRestart(this);
                    }
                }
                else
                {
                    setState(State.INITIALIZED);
                }
            }
            if(theUpdate != null)
            {
                theUpdate.close();
            }
        }
        catch(Throwable throwable)
        {
            if(theUpdate != null)
            {
                try
                {
                    theUpdate.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    void populateCacheHintForHJMP()
    {
        (new HJMPCachePopulator()).populateCacheHintForHJMP();
    }


    protected void assureTypeSystemStructureIsUpToDate()
    {
        if(!getJaloConnection().isSystemInitialized())
        {
            return;
        }
        Object object = new Object(this);
        TypeSystemHelper.assureTypeSystemStructureIsUpToDate((DataSource)getMasterDataSource(), (PropertiesLoader)object);
    }


    private boolean validateProperties()
    {
        this.missingParameters = new LinkedHashMap<>();
        for(Map.Entry<String, String> e : (Iterable<Map.Entry<String, String>>)getConfig().getAllParameters().entrySet())
        {
            if("<CHANGE_ME>".equalsIgnoreCase(e.getValue()))
            {
                this.missingParameters.put(e.getKey(), e.getValue());
            }
        }
        this.propertiesValid = Boolean.valueOf(this.missingParameters.isEmpty());
        return this.propertiesValid.booleanValue();
    }


    private boolean validateLicence()
    {
        Licence licence = Licence.getDefaultLicence();
        ValidationResult standardResult = licence.validate();
        this.licenseValid = Boolean.valueOf(standardResult.isValid());
        this.licenceValidationMessage = standardResult.getMessage();
        if(this.licenseValid.booleanValue() && licence.isDemoOrDevelopLicence() && isMasterTenantAndNotInitState())
        {
            ValidationResult result = licence.validateDemoExpiration();
            if(result != null)
            {
                this.licenseValid = Boolean.valueOf(result.isValid());
                this.licenceValidationMessage = result.getMessage();
            }
        }
        return this.licenseValid.booleanValue();
    }


    private boolean isMasterTenantAndNotInitState()
    {
        return ("master".equals(getTenantID()) && getJaloConnection().isSystemInitialized() &&
                        !Boolean.TRUE.equals(this.inSystemInit.get()));
    }


    private void initNumberSeriesPersistence()
    {
        try
        {
            ((DefaultSerialNumberDAO)((DefaultCachingSerialNumberGenerator)getSerialNumberGenerator()).getDao()).initPersistence();
        }
        catch(ClassCastException e)
        {
            LOGGER.warn("Cannot patch old number series due to " + e.getMessage() + " - ignored", e);
        }
    }


    private void migrateCoreTypes()
    {
        String migrationExtensionName = "core";
        DeploymentMigrationUtil.migrateSelectedDeployments("core", getMigratedCoreTypeNames());
    }


    private String[] getMigratedCoreTypeNames()
    {
        String propertyValue = Registry.getCurrentTenant().getConfig().getParameter("migrated_core_type.info");
        Object[] typesSet = DeploymentMigrationUtil.prepareMigratedCoreTypesInfoMap(propertyValue).keySet().toArray();
        return Arrays.<String, Object>copyOf(typesSet, typesSet.length, String[].class);
    }


    public final void doShutDown()
    {
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("shutting down " + this);
        }
        doShutdown(ShutDownMode.SYSTEM_SHUTDOWN);
    }


    final void doShutDownDuringInitialization()
    {
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("shutting down " + this + " during init/update");
        }
        doShutdown(ShutDownMode.INITIALIZATION);
    }


    private void doShutdown(ShutDownMode mode)
    {
        if(getState() != State.INACTIVE)
        {
            synchronized(this)
            {
                if(getState() != State.INACTIVE)
                {
                    assertNotCurrentlyStopping();
                    if(tenantLifecycleLoggingEnabled)
                    {
                        System.out.println("Tenant " + getTenantID() + " is shutting down...");
                    }
                    disableLogListeners();
                    Tenant oldtenant = Registry.getCurrentTenantInternal();
                    try
                    {
                        Registry.setCurrentTenantInternal(this);
                        this.currentlyNotifyingListeners = true;
                        notifyTenantListenersBeforeShutdown();
                        waitForThreadsToFinish();
                        waitForBackgroundProcessesToFinish();
                        shutDown(mode);
                        this.currentlyStopping = true;
                        shutDownApplicationContext(mode);
                        shutdownSingletons();
                        shutdownThreadPools();
                        shutdownPersistence();
                        shutdownCache(mode);
                        shutdownDataSources(mode);
                        setTenantRestartMarker(mode);
                        Registry.resetInitCounter(this);
                        this.currentlyStarting = false;
                        this.currentlyInitializing = false;
                    }
                    finally
                    {
                        Registry.setCurrentTenantInternal(oldtenant);
                        this.currentlyStopping = false;
                        this.currentlyNotifyingListeners = false;
                        setState(State.INACTIVE);
                        enableLogListeners();
                        if(tenantLifecycleLoggingEnabled)
                        {
                            System.out.println("Tenant " + getTenantID() + " is shut down.");
                        }
                        Registry.unregisterShutdownTenant(getTenantID());
                    }
                }
            }
        }
    }


    private void waitForBackgroundProcessesToFinish()
    {
        waitForBackgroundProcesses(getConfig().getInt("shutdown.background.processes.wait.timeout", 30), TimeUnit.SECONDS);
        interruptBackgroundThreads();
        waitForBackgroundProcesses(getConfig().getInt("shutdown.background.processes.forced.wait.timeout", 30), TimeUnit.SECONDS);
    }


    private void waitForBackgroundProcesses(long timeout, TimeUnit timeUnit)
    {
        long endTime = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(timeout, timeUnit);
        try
        {
            while(backgroundThreadsAreRunning() && System.currentTimeMillis() < endTime)
            {
                Thread.sleep(100L);
            }
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }


    private void interruptBackgroundThreads()
    {
        iterateOverBackgroundThreads(true);
    }


    private boolean backgroundThreadsAreRunning()
    {
        return iterateOverBackgroundThreads(false);
    }


    private boolean iterateOverBackgroundThreads(boolean interrupt)
    {
        boolean threadsChangedTryAgain = false;
        do
        {
            try
            {
                threadsChangedTryAgain = false;
                for(Thread thread : this.backgroundThreads)
                {
                    if(thread.isAlive())
                    {
                        if(interrupt)
                        {
                            thread.interrupt();
                            continue;
                        }
                        return true;
                    }
                }
            }
            catch(ConcurrentModificationException ex)
            {
                threadsChangedTryAgain = true;
            }
        }
        while(threadsChangedTryAgain);
        return false;
    }


    protected void shutdownCache(ShutDownMode mode)
    {
        if(mode == ShutDownMode.INITIALIZATION)
        {
            return;
        }
        if(this.iMan != null)
        {
            try
            {
                this.iMan.destroy();
            }
            catch(Exception e)
            {
                LOGGER.warn(e.getMessage(), e);
            }
            finally
            {
                this.iMan = null;
            }
        }
        if(this.cache != null)
        {
            try
            {
                this.cache.clear();
            }
            catch(Exception e)
            {
                LOGGER.warn(e.getMessage(), e);
            }
            finally
            {
                try
                {
                    this.cache.destroy();
                }
                catch(Exception e)
                {
                    LOGGER.warn(e.getMessage(), e);
                }
                finally
                {
                    this.cache = null;
                }
            }
        }
    }


    final void shutDownApplicationContext(ShutDownMode mode)
    {
        if(mode == ShutDownMode.SYSTEM_SHUTDOWN && getApplicationContext() != null)
        {
            getApplicationContext().close();
            this.applicationContext = null;
        }
    }


    private void shutdownDataSources(ShutDownMode mode)
    {
        this.dataSourceBuilder.destroy(mode);
        if(mode == ShutDownMode.INITIALIZATION)
        {
            return;
        }
        if(this.dataSourcesHolder != null)
        {
            this.dataSourcesHolder.destroy(mode);
            this.dataSourcesHolder = null;
        }
    }


    private void shutdownPersistence()
    {
        this.sysEJB = null;
        if(this.persistenceManager != null)
        {
            try
            {
                ((DBPersistenceManager)this.persistenceManager).destroy();
            }
            catch(Exception e)
            {
                LOGGER.warn(e.getMessage(), e);
            }
            finally
            {
                this.persistenceManager = null;
            }
        }
        if(this.persPool != null)
        {
            try
            {
                this.persPool.clearCache();
            }
            catch(Exception e)
            {
                LOGGER.warn(e.getMessage(), e);
            }
            finally
            {
                this.persPool = null;
            }
        }
    }


    private void waitForThreadsToFinish()
    {
        int timeoutSeconds = getConfig().getInt("shutdown.threadpool.wait.timeout", 60);
        waitForRunningThreads(this.threadPool, timeoutSeconds);
        waitForRunningThreads(this.workersPool, timeoutSeconds);
    }


    private void waitForRunningThreads(ThreadPool threadPool, int timeoutSeconds)
    {
        if(threadPool != null)
        {
            long endTime = System.currentTimeMillis() + (timeoutSeconds * 1000);
            try
            {
                while(threadPool.getNumActive() > 0 && System.currentTimeMillis() < endTime)
                {
                    Thread.sleep(100L);
                }
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
            int active = threadPool.getNumActive();
            if(active > 0)
            {
                System.err.println("Still got " + active + " threads running during shutdown of " + getTenantID() + " (timeout " + timeoutSeconds + " sec)");
            }
        }
    }


    private void shutdownThreadPools()
    {
        if(this.threadPool != null)
        {
            try
            {
                this.threadPool.close();
            }
            catch(Exception e)
            {
                LOGGER.warn(e.getMessage(), e);
            }
            finally
            {
                this.threadPool = null;
            }
        }
        if(this.workersPool != null)
        {
            try
            {
                this.workersPool.close();
            }
            catch(Exception e)
            {
                LOGGER.warn(e.getMessage(), e);
            }
            finally
            {
                this.workersPool = null;
            }
        }
    }


    private void shutdownSingletons()
    {
        if(this.singletonCreator != null)
        {
            this.singletonCreator.destroy();
            this.singletonCreator = null;
        }
        this.numberGenerator = null;
        this.jaloConnection = null;
    }


    private void notifyTenantListenersBeforeShutdown()
    {
        for(TenantListener listener : getTenantListeners())
        {
            try
            {
                listener.beforeTenantShutDown(this);
            }
            catch(Exception t)
            {
                LOGGER.warn("error notifying tenant listener : " + t.getMessage(), t);
            }
        }
    }


    List<TenantListener> getTenantListeners()
    {
        return Registry.getTenantListeners();
    }


    private void assertNotCurrentlyStopping()
    {
        if(this.currentlyStopping)
        {
            throw new IllegalStateException("recursive call to doShutDown()");
        }
    }


    protected final void executeInitsIfNecessary()
    {
        if(!RedeployUtilities.isShutdownInProgress())
        {
            doInitialize();
            if(!isStopping())
            {
                if(cannotAccess())
                {
                    throw new IllegalStateException("cannot execute inits since system is neither initialized nor started");
                }
                if(mustExecuteInits())
                {
                    executeInitsNoBlocking();
                }
                this.tenantStartupNotifier.executeStartupNotify();
            }
        }
    }


    private boolean mustExecuteInits()
    {
        return Registry.hasUninitializedInits(this);
    }


    private void executeInitsNoBlocking()
    {
        Set<String> validExtensions = new HashSet<>(getTenantSpecificExtensionNames());
        validExtensions.add(null);
        Registry.Init init = null;
        while((init = Registry.getNextInit(this)) != null)
        {
            if(validExtensions.contains(init.getExtensionName()))
            {
                try
                {
                    init.startup();
                }
                catch(Exception e)
                {
                    LOGGER.error("Error starting " + init + " : " + e.getMessage(), e);
                }
            }
        }
    }


    protected final boolean cannotAccess()
    {
        State accessState = this.state;
        return (accessState == State.INACTIVE || accessState == null || this.currentlyStopping);
    }


    protected String getStateInfo()
    {
        return "state:" + this.state + " currentlyStopping:" + this.currentlyStopping;
    }


    public final State getState()
    {
        return this.state;
    }


    protected final void setState(State state)
    {
        this.state = state;
    }


    public String getTenantID()
    {
        return this.tenantID;
    }


    public void setTenantID(String id)
    {
        this.tenantID = id;
    }


    public String toString()
    {
        return "<<" + getTenantID() + ">>";
    }


    public HybrisDataSource getDataSource(String className)
    {
        Preconditions.checkArgument((this.dataSourcesHolder != null && this.dataSourcesHolder.masterDataSource != null), "master data source is not created " + this.dataSourcesHolder);
        return this.dataSourceBuilder.getDataSource(this.dataSourcesHolder.customDataSources, className);
    }


    public HybrisDataSource getDataSource()
    {
        HybrisDataSource ret = getMasterDataSource();
        if(hasAltDataSource())
        {
            DataSourceSelection currentAlternativeDS = getThreadDataSource();
            if(currentAlternativeDS != null)
            {
                if(currentAlternativeDS.canUseDataSource())
                {
                    ret = currentAlternativeDS.getDataSource();
                }
            }
        }
        return ret;
    }


    public void cancelForceMasterMode()
    {
        if(!cannotAccess() && this.dataSourcesHolder != null)
        {
            DataSourceSelection currentSelection = getThreadDataSource();
            if(currentSelection != null)
            {
                if(currentSelection.isForceMasterPlaceholder())
                {
                    this.dataSourcesHolder.remove();
                }
                else
                {
                    currentSelection.cancelForceMasterMode();
                }
            }
        }
    }


    public void forceMasterDataSource()
    {
        if(cannotAccess())
        {
            throw new IllegalStateException("tenant " + this + " not yet started - got no datasource TL");
        }
        if(hasAltDataSource())
        {
            DataSourceSelection currentAlternativeDS = getThreadDataSource();
            if(currentAlternativeDS == null)
            {
                currentAlternativeDS = new DataSourceSelection(null);
                setThreadDataSource(currentAlternativeDS);
            }
            currentAlternativeDS.enforceMaster();
        }
    }


    protected DataSourceFactory createDataSourceFactory(String className)
    {
        try
        {
            Class<?> clazz = Class.forName(className);
            return (DataSourceFactory)clazz.newInstance();
        }
        catch(Exception e)
        {
            LOGGER.warn(e.getMessage(), e);
            throw new JaloSystemException("cannot load data source factory '" + className + "' due to '" + e.getMessage());
        }
    }


    protected DataSourceFactory createDataSourceFactory(ConfigIntf cfg)
    {
        DataSourceImplFactory dataSourceImplFactory;
        DataSourceFactory ret = null;
        String className = cfg.getParameter(Config.SystemSpecificParams.DB_FACTORY);
        if(className != null)
        {
            try
            {
                ret = (DataSourceFactory)Class.forName(className).newInstance();
            }
            catch(Exception e)
            {
                LOGGER.error("cannot load data source factory '" + className + "' due to '" + e.getMessage(), e);
            }
        }
        if(ret == null)
        {
            dataSourceImplFactory = new DataSourceImplFactory();
        }
        return (DataSourceFactory)dataSourceImplFactory;
    }


    public HybrisDataSource getMasterDataSource()
    {
        if(cannotAccess() || this.dataSourcesHolder == null || this.dataSourcesHolder.masterDataSource == null)
        {
            throw new TenantNotYetStartedException(this, "tenant " + this + " not yet started - got no active datasource");
        }
        return this.dataSourcesHolder.masterDataSource;
    }


    public void deactivateSlaveDataSource()
    {
        deactivateAlternativeDataSource();
    }


    public void deactivateAlternativeDataSource()
    {
        if(!cannotAccess() && this.dataSourcesHolder != null)
        {
            DataSourceSelection currentSelection = getThreadDataSource();
            if(currentSelection != null && !currentSelection.isForceMasterPlaceholder())
            {
                this.dataSourcesHolder.remove();
            }
        }
    }


    public Collection<HybrisDataSource> getAllSlaveDataSources()
    {
        return getAllAlternativeDataSources(false);
    }


    protected Collection<HybrisDataSource> getAllAlternativeDataSources(boolean asMaster)
    {
        if(hasAltDataSource())
        {
            Collection<HybrisDataSource> retset = new HashSet<>(this.dataSourcesHolder.alternativeDataSources.size());
            for(DataSourceHolder holder : this.dataSourcesHolder.alternativeDataSources)
            {
                if(holder.isMaster() == asMaster)
                {
                    retset.add(holder.getDataSource());
                }
            }
            return retset;
        }
        return Collections.EMPTY_SET;
    }


    public Collection<HybrisDataSource> getAllAlternativeMasterDataSources()
    {
        return getAllAlternativeDataSources(true);
    }


    public Set<String> getAllDataSourceIDs()
    {
        return getAllSlaveDataSourceIDs();
    }


    protected Set<String> getAllAlterntiveDataSourceIDs(boolean asMaster)
    {
        if(hasAltDataSource())
        {
            Set<String> retset = new HashSet<>(this.dataSourcesHolder.alternativeDataSources.size());
            for(DataSourceHolder holder : this.dataSourcesHolder.alternativeDataSources)
            {
                if(holder.isMaster() == asMaster)
                {
                    retset.add(holder.getID());
                }
            }
            return retset;
        }
        return Collections.EMPTY_SET;
    }


    public Set<String> getAllSlaveDataSourceIDs()
    {
        return getAllAlterntiveDataSourceIDs(false);
    }


    public Set<String> getAllAlternativeMasterDataSourceIDs()
    {
        return getAllAlterntiveDataSourceIDs(true);
    }


    public String activateSlaveDataSource()
    {
        if(hasAltDataSource())
        {
            if(cannotAccess())
            {
                throw new IllegalStateException("tenant " + this + " not yet started - got no datasource TL");
            }
            DataSourceSelection currentSelectedDS = getThreadDataSource();
            if(canSwitch(currentSelectedDS, false) && (currentSelectedDS == null || currentSelectedDS.isMaster()))
            {
                currentSelectedDS = getNextSlave();
                setThreadDataSource(currentSelectedDS);
            }
            return currentSelectedDS.getEffectiveID();
        }
        return null;
    }


    protected boolean canSwitch(DataSourceSelection currentSelection, boolean toMaster)
    {
        return (currentSelection == null || toMaster || (
                        !currentSelection.isForceMasterPlaceholder() && !currentSelection.isForceMasterMode()));
    }


    public void activateSlaveDataSource(String id)
    {
        activateAlternativeDataSource(id, false);
    }


    protected void activateAlternativeDataSource(String id, boolean asMaster)
    {
        if(hasAltDataSource())
        {
            if(cannotAccess())
            {
                throw new IllegalStateException("tenant " + this + " not yet started - got no datasource TL");
            }
            DataSourceSelection currentSelection = getThreadDataSource();
            if(canSwitch(currentSelection, asMaster))
            {
                for(DataSourceHolder holder : this.dataSourcesHolder.alternativeDataSources)
                {
                    if(id.equalsIgnoreCase(holder.getID()) && holder.isMaster() == asMaster)
                    {
                        if(holder.hasConnectionErrors())
                        {
                            throw new IllegalStateException("Cannot connect to given " + (asMaster ? "master" : "slave") + " data source with id '" + id + "'");
                        }
                        setThreadDataSource(new DataSourceSelection(holder));
                        return;
                    }
                }
                throw new IllegalArgumentException("invalid " + (asMaster ? "master" : "slave") + " data source id '" + id + "' - no data source configured");
            }
        }
    }


    private void setThreadDataSource(DataSourceSelection dataSourceSelection)
    {
        this.dataSourcesHolder.set(dataSourceSelection);
    }


    private DataSourceSelection getThreadDataSource()
    {
        return (DataSourceSelection)this.dataSourcesHolder.get();
    }


    private boolean hasAltDataSource()
    {
        return (this.dataSourcesHolder != null && CollectionUtils.isNotEmpty(this.dataSourcesHolder.alternativeDataSources));
    }


    public void activateAlternativeMasterDataSource(String id)
    {
        activateAlternativeDataSource(id, true);
    }


    public boolean isForceMaster()
    {
        if(hasAltDataSource())
        {
            if(cannotAccess() || this.dataSourcesHolder == null)
            {
                return false;
            }
            DataSourceSelection selection = getThreadDataSource();
            return (selection != null && selection.isForceMasterMode());
        }
        return false;
    }


    public boolean isSlaveDataSource()
    {
        if(hasAltDataSource())
        {
            if(cannotAccess() || this.dataSourcesHolder == null)
            {
                return false;
            }
            DataSourceSelection currentSelection = getThreadDataSource();
            return (currentSelection != null && !currentSelection.isForceMasterMode() && currentSelection.isSlave());
        }
        return false;
    }


    public boolean isAlternativeMasterDataSource()
    {
        if(hasAltDataSource())
        {
            if(cannotAccess() || this.dataSourcesHolder == null)
            {
                return false;
            }
            DataSourceSelection currentSelection = getThreadDataSource();
            return (currentSelection != null && !currentSelection.isForceMasterPlaceholder() && currentSelection.isMaster());
        }
        return false;
    }


    protected DataSourceSelection getNextSlave()
    {
        DataSourceHolder ret = null;
        if(hasAltDataSource())
        {
            int available = this.dataSourcesHolder.alternativeDataSources.size();
            int nextDsIdx;
            for(int i = 0; i < available; i++, nextDsIdx = (nextDsIdx + 1) % available)
            {
                DataSourceHolder candidate = this.dataSourcesHolder.alternativeDataSources.get(nextDsIdx);
                if(candidate.isSlave() && !candidate.hasConnectionErrors())
                {
                    this.lastChosenSlaveIdx = nextDsIdx;
                    ret = candidate;
                    break;
                }
            }
        }
        return (ret == null) ? null : new DataSourceSelection(ret);
    }


    public InvalidationManager getInvalidationManager()
    {
        if(cannotAccess() || this.iMan == null)
        {
            throw new IllegalStateException("tenant " + this + " not yet started - got no invalidation manager");
        }
        return this.iMan;
    }


    public Cache getCache()
    {
        if(cannotAccess() || this.cache == null)
        {
            throw new IllegalStateException("tenant " + this + ", (" + hashCode() + ") not yet started - got no cache (" +
                            getStateInfo() + " cache:" + this.cache + ")");
        }
        return this.cache;
    }


    public PersistencePool getPersistencePool()
    {
        if(cannotAccess() || this.persPool == null)
        {
            throw new IllegalStateException("tenant " + this + " not yet started - got no persistence pool");
        }
        return this.persPool;
    }


    public PersistenceManager getPersistenceManager()
    {
        if(cannotAccess() || this.persistenceManager == null)
        {
            throw new IllegalStateException("tenant " + this + " not yet started - got no persistence manager");
        }
        return this.persistenceManager;
    }


    public SystemEJB getSystemEJB()
    {
        if(cannotAccess() || this.sysEJB == null)
        {
            throw new IllegalStateException("tenant " + this + " not yet started - got no SystemEJB");
        }
        return this.sysEJB;
    }


    public ThreadPool getThreadPool()
    {
        if(cannotAccess() || this.threadPool == null)
        {
            throw new IllegalStateException("tenant " + this + " not yet started - got no thread pool");
        }
        return this.threadPool;
    }


    public ThreadPool getWorkersThreadPool()
    {
        if(cannotAccess() || this.workersPool == null)
        {
            throw new IllegalStateException("tenant " + this + " not yet started - got no workers thread pool");
        }
        return this.workersPool;
    }


    public SingletonCreator getSingletonCreator()
    {
        if(cannotAccess() || this.singletonCreator == null)
        {
            throw new IllegalStateException("tenant " + this + " not started yet - got no singleton creator");
        }
        return this.singletonCreator;
    }


    public SerialNumberGenerator getSerialNumberGenerator()
    {
        if(cannotAccess() || this.numberGenerator == null)
        {
            throw new IllegalStateException("tenant " + this + " not started yet - got no number generator");
        }
        return this.numberGenerator;
    }


    private ThreadPool createThreadPool()
    {
        return createDefaultThreadPool(getTenantID(),
                        getConfig().getInt("cronjob.maxthreads", 50));
    }


    public static ThreadPool createDefaultThreadPool(String tenantID, int poolSize)
    {
        ThreadPool ret = new ThreadPool(tenantID, poolSize);
        GenericObjectPool.Config config = new GenericObjectPool.Config();
        config.maxActive = poolSize;
        config.maxIdle = poolSize;
        config.maxWait = -1L;
        config.whenExhaustedAction = 1;
        config.testOnBorrow = true;
        config.testOnReturn = true;
        config.timeBetweenEvictionRunsMillis = 30000L;
        ret.setConfig(config);
        return ret;
    }


    private ThreadPool createWorkerThreadPool()
    {
        int poolSize = getConfig().getInt("workers.maxthreads", 64);
        ThreadPool ret = new ThreadPool(getTenantID(), poolSize);
        GenericObjectPool.Config config = new GenericObjectPool.Config();
        config.maxActive = poolSize;
        config.maxIdle = poolSize;
        config.maxWait = -1L;
        config.whenExhaustedAction = 0;
        config.testOnBorrow = true;
        config.testOnReturn = true;
        config.timeBetweenEvictionRunsMillis = 30000L;
        ret.setConfig(config);
        return ret;
    }


    private PersistencePool createPersistencePool()
    {
        PersistencePool pool = new PersistencePool(this);
        return pool;
    }


    private PersistenceManager createPersistenceManager(InvalidationManager invalidationManager)
    {
        return (PersistenceManager)new DBPersistenceManager(this, invalidationManager);
    }


    private InvalidationManager createInvalidationManager(Cache cache)
    {
        return new InvalidationManager(this, (InvalidationTarget)cache);
    }


    protected HybrisDataSource createMasterDataSource(DataSourceFactory factory)
    {
        return this.dataSourceBuilder.createMasterDataSource(factory);
    }


    protected List<DataSourceHolder> createAlternativeDataSources(DataSourceFactory defaultFactory, ConfigIntf cfg, Collection<HybrisDataSource> createdForRollback)
    {
        return this.dataSourceBuilder.createAlternativeDataSources(defaultFactory, cfg, createdForRollback);
    }


    protected HybrisDataSource createAlternativeDataSource(DataSourceFactory factory, String id, Map<String, String> params, boolean readOnly)
    {
        return this.dataSourceBuilder.createAlternativeDataSource(factory, id, params, readOnly);
    }


    protected Map<String, String> mergeSlaveDataSourceParameter(String dsID, Map<String, String> tenantSettings, String... keys)
    {
        return this.dataSourceBuilder.mergeSlaveDataSourceParameter(dsID, tenantSettings, keys);
    }


    public static Map<String, String> extractCustomDBParams(ConfigIntf cfg)
    {
        return extractCustomDBParams(cfg, false);
    }


    public static Map<String, String> extractCustomDBParams(ConfigIntf cfg, boolean stripPrefix)
    {
        return cfg.getParametersMatching("db.connectionparam\\.(.*)", stripPrefix);
    }


    public static Map<String, String> extractCustomDBParams(Map<String, String> map)
    {
        return extractCustomDBParams(map, false);
    }


    public static Map<String, String> extractCustomDBParams(Map<String, String> map, boolean stripPrefix)
    {
        Map<String, String> ret = new HashMap<>();
        Pattern pattern = Pattern.compile("db.connectionparam\\.(.*)", 2);
        for(Map.Entry<String, String> e : map.entrySet())
        {
            Matcher matcher = pattern.matcher(e.getKey());
            if(matcher.matches())
            {
                if(stripPrefix)
                {
                    ret.put(matcher.group(1), e.getValue());
                    continue;
                }
                ret.put(e.getKey(), e.getValue());
            }
        }
        return ret;
    }


    protected Cache createCache()
    {
        return (new CacheFactory()).createCacheInstance(this);
    }


    public boolean isClusteringEnabled()
    {
        boolean dobroadcasts = getConfig().getBoolean(Config.Params.CLUSTERMODE, false);
        if(dobroadcasts && !Licence.getDefaultLicence().isClusteringPermitted())
        {
            LOGGER.error("Installed licence does not allow clustering - UDP broadcasts disabled!");
            dobroadcasts = false;
        }
        return dobroadcasts;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public int getClusterID()
    {
        return DefaultClusterNodeManagementService.getInstance().getClusterID();
    }


    public long getDynamicClusterNodeID()
    {
        return DefaultBroadcastService.getInstance().getDynamicClusterNodeID();
    }


    protected Object performWithinOwnSystem(CodeWrapper wrapper)
    {
        Tenant prev = null;
        boolean prevForceMaster = false;
        boolean prevSlaveDataSourceMode = false;
        String prevAlternativeDataSource = null;
        if(Registry.hasCurrentTenant() && (prev = Registry.getCurrentTenantNoFallback()) != this)
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
            Registry.unsetCurrentTenant();
        }
        try
        {
            Registry.setCurrentTenant(this);
            return wrapper.perform();
        }
        finally
        {
            if(prev != null)
            {
                if(prev != this)
                {
                    Registry.unsetCurrentTenant();
                    if(prev instanceof SlaveTenant)
                    {
                        Registry.setCurrentTenant(prev);
                    }
                    else
                    {
                        Registry.activateMasterTenantAndFailIfAlreadySet();
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
                Registry.unsetCurrentTenant();
            }
        }
    }


    public JaloSession getActiveSession()
    {
        return this.sessionTL.get();
    }


    public void setActiveSessionForCurrentThread(JaloSession session)
    {
        assertTenant(session);
        JaloSession before = this.sessionTL.get();
        if(before != session && (before == null || !before.equals(session)))
        {
            if(session == null)
            {
                for(TenantListener tl : getTenantListeners())
                {
                    tl.beforeUnsetActivateSession(this);
                }
            }
            this.sessionTL.set(session);
            if(!RedeployUtilities.isShutdownInProgress())
            {
                Cache staticCache = getCache();
                if(staticCache instanceof StaticCache)
                {
                    ((StaticCache)staticCache).clearRequestCache();
                }
            }
            if(session != null)
            {
                for(TenantListener tl : getTenantListeners())
                {
                    tl.afterSetActivateSession(this);
                }
            }
        }
    }


    protected void assertTenant(JaloSession session)
    {
        if(session != null && !equals(session.getTenant()))
        {
            throw new IllegalArgumentException("cannot activate jalo session " + session + " for tenent " + this + " since it actually belong to " + session
                            .getTenant());
        }
    }


    public List<SessionContext> getActiveSessionContextList()
    {
        return this.sessionContextTL.get();
    }


    public JaloConnection getJaloConnection()
    {
        if(cannotAccess() || this.jaloConnection == null)
        {
            throw new IllegalStateException("tenant " + this + ",(" + hashCode() + ") not started yet - got no jaloConnection (" +
                            getStateInfo() + " jaloConnection:" + this.jaloConnection + ")");
        }
        return this.jaloConnection;
    }


    public boolean cannotConnect()
    {
        HybrisDataSource dataSource = getMasterDataSource();
        boolean cannot = (dataSource == null || dataSource.cannotConnect());
        if(cannot)
        {
            this.connectionBroken = true;
        }
        return cannot;
    }


    public boolean isConnectionPoolCheckEnabled()
    {
        return getConfig().getBoolean("tenant.connection.check.enabled", false);
    }


    public boolean connectionHasBeenBroken()
    {
        return (this.connectionBroken || (isConnectionPoolCheckEnabled() && cannotConnect()));
    }


    public void clearConnectionHasBeenBroken()
    {
        this.connectionBroken = false;
    }


    public Object writeReplace() throws ObjectStreamException
    {
        return new TenantDTO(this.tenantID);
    }


    public final boolean isStarting()
    {
        return (this.currentlyInitializing || this.currentlyStarting);
    }


    public final boolean isStopping()
    {
        return this.currentlyStopping;
    }


    public final boolean isNotifiyingListeners()
    {
        return this.currentlyNotifyingListeners;
    }


    public Thread createAndRegisterBackgroundThread(Runnable payload, ThreadFactory factory)
    {
        return (factory == null) ? (Thread)new RegistrableThread(wrapPayload(payload)) : factory.newThread(wrapPayload(payload));
    }


    Runnable wrapPayload(Runnable payload)
    {
        return (Runnable)new Object(this, payload);
    }


    protected void backgroundThreadStarted(Thread thread, Runnable payload)
    {
        this.backgroundThreads.add(thread);
    }


    protected void backgroundThreadFinished(Thread thread, Runnable payload)
    {
        this.backgroundThreads.remove(thread);
    }


    public void resetTenantRestartMarker()
    {
        this.tenantRestartMarker.set(Long.MIN_VALUE);
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof AbstractTenant))
        {
            return false;
        }
        AbstractTenant that = (AbstractTenant)o;
        return this.uuid.equals(that.uuid);
    }


    public int hashCode()
    {
        return this.uuid.hashCode();
    }
}
