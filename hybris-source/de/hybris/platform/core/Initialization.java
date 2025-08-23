package de.hybris.platform.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.ddl.HybrisSchemaGenerator;
import de.hybris.platform.audit.internal.config.AuditConfigService;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupCollector;
import de.hybris.platform.core.initialization.SystemSetupCollectorResult;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.system.InitializationLockDao;
import de.hybris.platform.core.system.InitializationLockHandler;
import de.hybris.platform.core.system.InitializationLockInfo;
import de.hybris.platform.core.system.impl.DefaultInitLockDao;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.jalo.AbstractSystemCreator;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.licence.Licence;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.property.DBPersistenceManager;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerManager;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.WebSessionFunctions;
import de.hybris.platform.util.database.DropTablesTool;
import de.hybris.platform.util.localization.TypeLocalization;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Initialization
{
    static final String JSPCONTEXT_KEY = "jspc";
    private static final Logger LOG = Logger.getLogger(Initialization.class.getName());
    private static final Set<String> LOCALLYINITIALIZINGTENANTIDS = new CopyOnWriteArraySet<>();
    public static final String LOG_HEADER = "###############################################################";
    public static final String SYSTEM_INITIALIZATION_OP_NAME = "System initialization";
    public static final String SYSTEM_UPDATE_OP_NAME = "System update";
    public static final String EXT_PROJECT_DATA_SUFFIX = "_sample";
    private static final boolean DEFAULT_FORCE_CLEAN = true;


    public static boolean isSystemLocked()
    {
        return Config.getBoolean("system.unlocking.disabled", false);
    }


    public static boolean isCurrentTenantInitializing()
    {
        return isTenantInitializing(Registry.getCurrentTenantNoFallback());
    }


    public static boolean isTenantInitializing(Tenant tenant)
    {
        return (isTenantInitializingLocally(tenant) || isTenantInitializingGlobally(tenant));
    }


    public static boolean isTenantInitializingLocally(Tenant tenant)
    {
        return LOCALLYINITIALIZINGTENANTIDS.contains(tenant.getTenantID());
    }


    public static boolean isTenantInitializingGlobally(Tenant tenant)
    {
        if(isSystemLocked())
        {
            return false;
        }
        if(hasRunningMasterTenant())
        {
            InitializationLockInfo lockInfo = (new InitializationLockHandler((InitializationLockDao)new DefaultInitLockDao())).getLockInfo();
            return (lockInfo != null && lockInfo.isLocked() && tenant.getTenantID().equalsIgnoreCase(lockInfo.getTenantId()));
        }
        return false;
    }


    public static boolean isAnyTenantInitializingGlobally()
    {
        if(isSystemLocked())
        {
            return false;
        }
        if(hasRunningMasterTenant())
        {
            InitializationLockInfo lockInfo = (new InitializationLockHandler((InitializationLockDao)new DefaultInitLockDao())).getLockInfo();
            return (lockInfo != null && lockInfo.isLocked() && "System initialization".equals(lockInfo.getProcessName()));
        }
        return false;
    }


    public static boolean hasRunningMasterTenant()
    {
        return (Registry.getMasterTenant().getState() == AbstractTenant.State.STARTED);
    }


    private static void markSystemInitialized(HybrisDataSource dataSource, boolean initialized)
    {
        String id = dataSource.getTenant().getTenantID();
        if(!initialized)
        {
            if(!LOCALLYINITIALIZINGTENANTIDS.add(id))
            {
                throw new IllegalStateException("tenant " + id + " is currently being initialized - cannot start second initialization");
            }
        }
        try
        {
            dataSource.getTenant().getSystemEJB().setInitializedFlag(initialized);
        }
        catch(RuntimeException re)
        {
            if(initialized)
            {
                throw re;
            }
        }
        catch(Exception e)
        {
            if(initialized)
            {
                throw new RuntimeException(e);
            }
        }
        finally
        {
            if(initialized)
            {
                LOCALLYINITIALIZINGTENANTIDS.remove(id);
            }
        }
    }


    private static boolean performLockedAdministrativeTaskForTestSystem(SystemCallable callable) throws Exception
    {
        Utilities.setJUnitTenant();
        Tenant testTenant = Registry.getCurrentTenantNoFallback();
        InitializationLockHandler handler = new InitializationLockHandler((InitializationLockDao)new DefaultInitLockDao());
        if(!handler.performLocked(testTenant, (Callable)callable, callable.getOperationName()))
        {
            InitializationLockInfo info = handler.getLockInfo();
            String initializationLockInfoMessage = callable.getOperationName() + " can not be performed, there is already existing lock :  '" + callable.getOperationName() + "' on '" + info.getProcessName() + "' tenant and cluster id " + info.getTenantId() + ", issued at " + info.getClusterNodeId();
            LOG.info(initializationLockInfoMessage);
            throw new IllegalStateException(initializationLockInfoMessage);
        }
        return true;
    }


    public static boolean initializeTestSystem() throws Exception
    {
        Utilities.setJUnitTenant();
        Tenant testTenant = Registry.getCurrentTenant();
        Object object = new Object(testTenant);
        return performLockedAdministrativeTaskForTestSystem((SystemCallable)object);
    }


    public static boolean updateTestSystem() throws Exception
    {
        Utilities.setJUnitTenant();
        Tenant testTenant = Registry.getCurrentTenant();
        Object object = new Object(testTenant);
        return performLockedAdministrativeTaskForTestSystem((SystemCallable)object);
    }


    static JspContext createDummyInitJspContext()
    {
        ImmutableMap immutableMap = ImmutableMap.of("initmethod", "init", "init", "junit");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameters((Map)immutableMap);
        MockHttpServletResponse response = new MockHttpServletResponse();
        return new JspContext(null, (HttpServletRequest)request, (HttpServletResponse)response);
    }


    static JspContext createDummyUpdateJspContext()
    {
        ImmutableMap immutableMap = ImmutableMap.of("initmethod", "update", "init", "junit");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameters((Map)immutableMap);
        MockHttpServletResponse response = new MockHttpServletResponse();
        return new JspContext(null, (HttpServletRequest)request, (HttpServletResponse)response);
    }


    private static void performAdministrativeTaskForTestSystem(Map props) throws Exception
    {
        Utilities.setJUnitTenant();
        boolean forcedInitialization = isForcedInitialization(props);
        if(LOG.isInfoEnabled())
        {
            logJunitInitOrUpdateStarted(forcedInitialization);
        }
        if(!runChecksBeforeInitialization(JspContext.NULL_CONTEXT, forcedInitialization))
        {
            throw new ConsistencyCheckException("Some of pre-initialization checks failed. Check logs for details", 0);
        }
        SystemEJB.getInstance().setLocked(false);
        for(Extension ext : getCreators())
        {
            try
            {
                loginAdmin();
                ext.notifyInitializationStart(props, JspContext.NULL_CONTEXT);
            }
            catch(Exception e)
            {
                LOG.error("error notifying extension " + ext.getName() + " about initialization start", e);
            }
        }
        (new Object(props))
                        .execute(null);
        CoreBasicDataCreator coreBasicDataCreator = new CoreBasicDataCreator();
        coreBasicDataCreator.createSupportedEncodings();
        coreBasicDataCreator.createRootMediaFolder();
        JaloConnection con = JaloConnection.getInstance();
        if(con.isSystemInitialized())
        {
            for(Extension ext : getCreators())
            {
                try
                {
                    loginAdmin();
                    ext.notifyInitializationEnd(props, null);
                }
                catch(Exception e)
                {
                    LOG.error("error notifying extension " + ext.getName() + " about initialization end", e);
                }
            }
        }
        if(LOG.isInfoEnabled())
        {
            logJunitInitOrUpdateFinished(forcedInitialization);
        }
    }


    private static void dropTables(JspContext jspc)
    {
        AbstractSystemCreator.logln("<h3>Dropping tables ...</h3>", jspc);
        Tenant tenant = Registry.getCurrentTenantNoFallback();
        markSystemInitialized(tenant.getDataSource(), false);
        try
        {
            (new DropTablesTool(tenant, jspc)).dropAllTables();
        }
        catch(SQLException e)
        {
            throw new JaloSystemException(e);
        }
    }


    private static void clearHMCConfig()
    {
        try
        {
            Class<?> ttl = Class.forName("de.hybris.platform.hmc.jalo.HMCManager");
            Method method = ttl.getMethod("getInstance", (Class[])null);
            Object manager = method.invoke(null, (Object[])null);
            method = ttl.getMethod("clearHMCStructure", (Class[])null);
            method.invoke(manager, (Object[])null);
        }
        catch(ClassNotFoundException e)
        {
            LOG.trace(e);
        }
        catch(Exception e)
        {
            LOG.warn("Can't invoke methods of HMCManager: " + e.getMessage(), e);
        }
    }


    public static boolean isUpdateParameter(Map params)
    {
        return "update".equals(params.get("initmethod"));
    }


    private static boolean isInitOrUpdate(HttpServletRequest request)
    {
        return ((request.getParameter("init") != null && StringUtils.isNotBlank(request.getParameter("initmethod"))) ||
                        isDefault(request));
    }


    private static boolean isDrop(HttpServletRequest request)
    {
        return (request.getParameter("droptables") != null || (isForceInit(request) && isDefault(request)));
    }


    private static boolean isForceInit(HttpServletRequest request)
    {
        return (isInitOrUpdate(request) && !"update".equals(request.getParameter("initmethod")));
    }


    private static boolean isDryRun(HttpServletRequest request)
    {
        return Boolean.parseBoolean(request.getParameter("dryrun"));
    }


    private static boolean isDefault(HttpServletRequest request)
    {
        boolean defaultFlag = false;
        if(request.getParameter("default") != null)
        {
            defaultFlag = Boolean.parseBoolean(request.getParameter("default"));
        }
        return defaultFlag;
    }


    private static boolean doInitializeImpl(JspContext jspc) throws Exception
    {
        RevertibleUpdate revertibleUpdate = updateOperationInfoAsInitOrUpdateThread();
        HttpServletRequest request = jspc.getServletRequest();
        if(SystemEJB.getInstance().isLocked())
        {
            AbstractSystemCreator.logln("<br/><font color='red'>System is locked. Please unlock (using <a href=\"system_config.jsp?tab=system#lock\">System Configuration</a> page) to allow changes.</font>", jspc);
            return false;
        }
        boolean dropTables = (request.getParameter("droptables") != null);
        boolean INITorUPDATE = isInitOrUpdate(request);
        boolean DEFAULT = isDefault(request);
        boolean forceInit = isForceInit(request);
        boolean dryRun = isDryRun(request);
        if(INITorUPDATE && !runChecksBeforeInitialization(jspc, isForceInit(jspc.getServletRequest())))
        {
            throw new ConsistencyCheckException("Some of pre-initialization checks failed. Check logs for details", 0);
        }
        boolean showForm = true;
        long time = System.currentTimeMillis();
        if(dropTables || (forceInit && DEFAULT))
        {
            showForm = false;
        }
        if(INITorUPDATE)
        {
            if(Registry.hasCurrentTenant())
            {
                AbstractTenant curTen = (AbstractTenant)Registry.getCurrentTenant();
                curTen.getThreadPool().close();
                for(Extension ext : getCreators())
                {
                    try
                    {
                        loginAdmin();
                        Map<String, String> params = getParameterMap(ext, request);
                        params.put("initmethod", request.getParameter("initmethod"));
                        ext.notifyInitializationStart(params, jspc);
                    }
                    catch(Exception e)
                    {
                        AbstractSystemCreator.logln("<br/><font color='red'>Error notifying initialization start for " + ext
                                        .getCreatorName() + ". See console output.</font>", jspc);
                        LOG.trace(e.getMessage(), e);
                    }
                }
            }
        }
        if(request.getParameter("init") != null || DEFAULT)
        {
            showForm = false;
            if(INITorUPDATE)
            {
                ImmutableMap immutableMap = ImmutableMap.of("metainformation.systemname",
                                Registry.getCurrentTenant().getTenantID(), "force.clean",
                                forceInit ? "true" : "false", "move.properties", "true");
                logMasterSystemInitOrUpdateStarted(jspc, forceInit);
                (new Object((Map)immutableMap, jspc))
                                .execute(jspc);
            }
            if(dryRun)
            {
                return showForm;
            }
            loginAdmin();
            if(request.getParameter("clearhmc") != null || DEFAULT)
            {
                AbstractSystemCreator.logln("<br/><h3>Clearing hmc configuration from database ...</h3>", jspc);
                WebSessionFunctions.getSession(request);
                clearHMCConfig();
                showForm = false;
            }
            if(INITorUPDATE)
            {
                loginAdmin();
            }
            reEncodePasswordsForUsersCreatedDuringInitialization(jspc);
            storeGenericAuditConfigurations(jspc);
            if(request.getParameter("essential") != null || DEFAULT)
            {
                createEssentialData(jspc);
            }
            if(request.getParameter("localizetypes") != null || DEFAULT)
            {
                AbstractSystemCreator.logln("<br/><h3>Localizing types ...</h3>", jspc);
                TypeLocalization.getInstance().localizeTypes();
            }
            if(request.getParameter("createProjectData") != null)
            {
                createProjectData(jspc, true);
            }
            else
            {
                createProjectData(jspc, DEFAULT);
            }
            JaloConnection con = JaloConnection.getInstance();
            if(con.isSystemInitialized())
            {
                for(Extension ext : getCreators())
                {
                    try
                    {
                        loginAdmin();
                        Map<String, String> params = getParameterMap(ext, request);
                        params.put("initmethod", request.getParameter("initmethod"));
                        ext.notifyInitializationEnd(params, jspc);
                    }
                    catch(Exception e)
                    {
                        AbstractSystemCreator.logln("<br/><font color='red'>Error notifying initialization end for " + ext
                                        .getCreatorName() + ". See console output.</font>", jspc);
                        LOG.info(e.getMessage(), e);
                    }
                }
            }
            if(forceInit)
            {
                ServicelayerManager.getInstance().notifyClusterAboutTenantInitialization(Registry.getCurrentTenant());
            }
            time = System.currentTimeMillis() - time;
            logMasterSystemUpdateOrInitFinished(jspc, forceInit, time);
            String contextPath = "/";
            try
            {
                contextPath = Utilities.getExtensionInfo("hac").getWebModule().getWebRoot();
                if(contextPath == null || contextPath.length() == 0)
                {
                    contextPath = "/";
                }
            }
            catch(Exception e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Can not determine webroot", e);
                }
            }
            jspc.println("<br/><a href=\"" + jspc.getServletResponse().encodeURL(contextPath) + "\">Continue...</a>");
            if(INITorUPDATE)
            {
                logoutCurrentUser();
            }
        }
        updateOperationInfoToPreviousState(revertibleUpdate);
        return showForm;
    }


    private static boolean runChecksBeforeInitialization(JspContext jspc, boolean forceInit) throws Exception
    {
        for(Extension ext : getCreators())
        {
            loginAdmin();
            try
            {
                ext.checkBeforeInitialization(jspc, forceInit);
            }
            catch(Exception e)
            {
                AbstractSystemCreator.logln("<br/><font color='red'>The check before initialization for extension " + ext
                                .getName() + " failed with: " + e
                                .getMessage() + "</font>", jspc);
                LOG.error(e.getMessage(), e);
                return false;
            }
        }
        return true;
    }


    private static RevertibleUpdate updateOperationInfoAsInitOrUpdateThread()
    {
        OperationInfo operationInfo = OperationInfo.builder().asNotSuspendableOperation().withCategory(OperationInfo.Category.INIT_OR_UPDATE).build();
        return OperationInfo.updateThread(operationInfo);
    }


    private static void updateOperationInfoToPreviousState(RevertibleUpdate previousState)
    {
        previousState.revert();
    }


    private static void attachNewJaloSessionToHttpSession(JspContext jspc)
    {
        if(jspc != null && jspc.getServletResponse() != null)
        {
            HttpSession session = jspc.getServletRequest().getSession();
            WebSessionFunctions.getSession(Collections.EMPTY_MAP, null, session, jspc
                            .getServletRequest(), jspc.getServletResponse());
        }
    }


    private static void detachJaloSessionFromHttpSessionIfNeeded(JspContext jspc)
    {
        if(jspc != null && jspc.getServletRequest() != null)
        {
            HttpSession session = jspc.getServletRequest().getSession(false);
            if(session != null)
            {
                WebSessionFunctions.clearSession(session);
            }
        }
        if(JaloSession.hasCurrentSession())
        {
            JaloSession jaloSession = JaloSession.getCurrentSession();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Closing Session AFTER: " + jaloSession.getSessionID());
            }
            jaloSession.close();
        }
    }


    private static void printCurrentSession()
    {
        if(JaloSession.hasCurrentSession())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Session BEFORE: " + JaloSession.getCurrentSession().getSessionID());
            }
        }
    }


    private static void createEmptySystemOrUpdate(Map<?, ?> params, JspContext jspc) throws Exception
    {
        AbstractSystemCreator.logln("Please wait. This step can take some minutes to complete.", jspc);
        AbstractSystemCreator.logln("If you do not receive any feedback on this page in this time, consult the applicationserver logs for possible errors.", jspc);
        if(isForcedInitialization(params))
        {
            AbstractSystemCreator.logln("Dropping old and creating new empty system ... ", jspc);
        }
        else
        {
            AbstractSystemCreator.logln("Updating system... ", jspc);
        }
        Map<Object, Object> initProp = new HashMap<>(params);
        initProp.put("jspc", jspc);
        initialize(initProp, null);
    }


    private static void logoutCurrentUser()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null)
        {
            auth.setAuthenticated(false);
        }
    }


    public static boolean doInitialize(JspContext jspc) throws Exception
    {
        HttpServletRequest request = jspc.getServletRequest();
        if(SystemEJB.getInstance().isLocked())
        {
            String systemIsLocked = "System is locked. Please unlock";
            AbstractSystemCreator.logln("<br/><font color='red'>System is locked. Please unlock (using <a href=\"system_config.jsp?tab=system#lock\">System Configuration</a> page) to allow changes.</font>", jspc);
            throw new IllegalStateException("System is locked. Please unlock");
        }
        Object object = new Object(jspc, request);
        if(isInitOrUpdate(request) || isDrop(request))
        {
            InitializationLockHandler handler = new InitializationLockHandler((InitializationLockDao)new DefaultInitLockDao());
            if(!handler.performLocked(object.getCurrentTenant(), (Callable)object, object
                            .getOperationName()))
            {
                InitializationLockInfo info = handler.getLockInfo();
                String initializationLockInfoText = object.getOperationName() + " can not be performed, there is already existing lock :  '" + object.getOperationName() + "' on '" + info.getProcessName() + "' tenant and cluster id " + info.getTenantId() + ", issued at " + info.getClusterNodeId();
                AbstractSystemCreator.logln("<br/><font color='red'>" + initializationLockInfoText + "</font>", jspc);
                throw new IllegalStateException(initializationLockInfoText);
            }
        }
        else
        {
            object.call();
        }
        return object.isShowFormFlag();
    }


    @Deprecated(since = "5.0", forRemoval = true)
    protected static void createJUnitTenant() throws ConsistencyCheckException
    {
        throw new UnsupportedOperationException("This operation is not allowed, please do provide an offline <<tenants.xml>> configuration instead");
    }


    public static void createAlternativeTestDataSources()
    {
        throw new UnsupportedOperationException("This operation is not allowed, please do provide the offline platform/tenant_{tenantID}.properties or config/local_tenant_{tenantID}.properties configuration instead");
    }


    protected static void putDataSourceParams(Map<String, String> params, String dsId, String driver, boolean slave)
    {
        String prefix = slave ? "slave.datasource" : "alt.datasource";
        params.put(prefix + "." + prefix + "." + dsId, driver);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static void updateSystem() throws Exception
    {
        Map<Object, Object> map = new HashMap<>();
        map.put("force.clean", "false");
        map.put("metainformation.systemname", "master");
        initialize(map, null);
    }


    private static void loginAdmin() throws Exception
    {
        try
        {
            if(JaloSession.hasCurrentSession())
            {
                if(JaloSession.getCurrentSession().getUser() == null || !JaloSession.getCurrentSession().getUser().isAdmin())
                {
                    JaloSession.getCurrentSession().setUser((User)UserManager.getInstance().getAdminEmployee());
                }
            }
            else
            {
                JaloConnection conn = JaloConnection.getInstance();
                JaloSession jaloSession = conn.createAnonymousCustomerSession();
                jaloSession.setUser((User)UserManager.getInstance().getAdminEmployee());
            }
        }
        catch(Exception e)
        {
            LOG.debug(e);
        }
    }


    private static Map getParameterMap(Extension ext, HttpServletRequest request)
    {
        Collection dcParams = ext.getCreatorParameterNames();
        Map<Object, Object> inputParams = new HashMap<>();
        if(dcParams != null && !dcParams.isEmpty())
        {
            for(Iterator<String> it = dcParams.iterator(); it.hasNext(); )
            {
                String param = it.next();
                String value = request.getParameter(ext.getCreatorName() + "_" + ext.getCreatorName());
                inputParams.put(param, value);
            }
        }
        if(!inputParams.isEmpty() && LOG.isDebugEnabled())
        {
            LOG.debug(inputParams);
        }
        return inputParams;
    }


    public static Map<String, List<SystemSetupCollectorResult>> getAllPatchesForExtensions()
    {
        Map<String, List<SystemSetupCollectorResult>> result = new HashMap<>();
        SystemSetupCollector systemSetupCollector = getSystemSetupCollector();
        for(ExtensionInfo extensionInfo : getAllExtensions())
        {
            String extensionName = extensionInfo.getName();
            List<SystemSetupCollectorResult> applicablePatches = systemSetupCollector.getApplicablePatches(extensionName);
            if(CollectionUtils.isNotEmpty(applicablePatches))
            {
                result.put(extensionName, applicablePatches);
            }
        }
        return result;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static void createEssentialData(JspContext jspc) throws Exception
    {
        HttpServletRequest request = jspc.getServletRequest();
        AbstractSystemCreator.logln("<br/><h3>################ Creating essential data </h3>", jspc);
        SystemSetupCollector dataSetupCollector = getSystemSetupCollector();
        ExtensionManager extensionManager = ExtensionManager.getInstance();
        for(ExtensionInfo extensionInfo : getAllExtensions())
        {
            try
            {
                AbstractSystemCreator.logln("Creating essential data for " + extensionInfo.getName() + " ...", jspc);
                if(extensionInfo.getCoreModule() != null)
                {
                    Extension ext = extensionManager.getExtension(extensionInfo.getName());
                    loginAdmin();
                    Map<String, String> paramMap = getParameterMap(ext, request);
                    if(paramMap != null)
                    {
                        paramMap.put("initmethod", request.getParameter("initmethod"));
                    }
                    ext.createEssentialData(paramMap, jspc);
                }
                SystemSetupContext ctx = new SystemSetupContext(request.getParameterMap(), SystemSetup.Type.ESSENTIAL, extensionInfo.getName());
                ctx.setJspContext(jspc);
                dataSetupCollector.executeMethods(ctx);
            }
            catch(Exception e)
            {
                AbstractSystemCreator.logln("<br/><font color='red'>Error creating essential data for " + extensionInfo.getName() + ". See console output.</font>", jspc);
                LOG.error(e.getMessage(), e);
            }
        }
    }


    private static void reEncodePasswordsForUsersCreatedDuringInitialization(JspContext jspc)
    {
        PasswordsReEncoder.reEncodePasswordFor(logLine -> AbstractSystemCreator.logln("################ " + logLine, jspc), new String[] {"admin", "anonymous"});
    }


    private static void storeGenericAuditConfigurations(JspContext jspc)
    {
        AbstractSystemCreator.logln("################ Storing Generic Audit Report configurations ...", jspc);
        ((AuditConfigService)Registry.getApplicationContext().getBean("auditConfigService", AuditConfigService.class)).storeConfigurations();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static void createProjectData(JspContext jspc, boolean forceAll) throws Exception
    {
        HttpServletRequest request = jspc.getServletRequest();
        AbstractSystemCreator.logln("<h3>################ Creating project data </h3>", jspc);
        SystemSetupCollector dataSetupCollector = getSystemSetupCollector();
        ExtensionManager extensionManager = ExtensionManager.getInstance();
        for(ExtensionInfo extensionInfo : getAllExtensions())
        {
            try
            {
                if(request.getParameter(extensionInfo.getName() + "_sample") != null || forceAll)
                {
                    AbstractSystemCreator.logln("Creating project data for " + extensionInfo.getName() + " ...", jspc);
                    if(extensionInfo.getCoreModule() != null)
                    {
                        Extension ext = extensionManager.getExtension(extensionInfo.getName());
                        loginAdmin();
                        ext.createProjectData(getParameterMap(ext, request), jspc);
                    }
                    SystemSetupContext ctx = new SystemSetupContext(request.getParameterMap(), SystemSetup.Type.PROJECT, extensionInfo.getName());
                    ctx.setJspContext(jspc);
                    dataSetupCollector.executeMethods(ctx);
                }
            }
            catch(Exception e)
            {
                AbstractSystemCreator.logln("<br/><font color='red'>Error creating sample data for " + extensionInfo.getName() + ". See console output.</font>", jspc);
                LOG.error(e);
            }
        }
    }


    public static void initialize(Map params, Licence licence) throws Exception
    {
        Preconditions.checkNotNull(params.get("jspc"), "JspContext is required in the provided map of params");
        Registry.getCurrentTenant().forceMasterDataSource();
        if(SystemEJB.getInstance().isLocked())
        {
            throw new ConsistencyCheckException("system is locked - please unlock to allow initialization or update", 0);
        }
        boolean forcedInitialization = isForcedInitialization(params);
        if(LOG.isInfoEnabled())
        {
            logTypeSystemInitOrUpdateStarted(forcedInitialization);
        }
        boolean success = false;
        prepare(params);
        boolean txbefore = Transaction.isUserTransactionEnabled();
        Transaction.enableUserTransactionForThread(Config.getBoolean("initialization.enabletx", false));
        try
        {
            long time1 = System.currentTimeMillis();
            JspContext jspc = (JspContext)params.get("jspc");
            initializeSchemaAndTypeSystemFullyNewStyle(jspc);
            long time2 = System.currentTimeMillis();
            if(LOG.isInfoEnabled())
            {
                logTypeSystemInitOrUpdateFinished(forcedInitialization, time2, time1);
            }
            success = true;
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
            throw e;
        }
        finally
        {
            unPrepare(params, success);
            Transaction.enableUserTransactionForThread(txbefore);
        }
    }


    private static void logTypeSystemInitOrUpdateStarted(boolean forcedInitialization)
    {
        if(forcedInitialization)
        {
            LOG.info("### Starting type system initialization");
        }
        else
        {
            LOG.info("### Starting type system update");
        }
    }


    private static void logTypeSystemInitOrUpdateFinished(boolean forcedInitialization, long time2, long time1)
    {
        String duration = Utilities.formatTime(time2 - time1);
        if(forcedInitialization)
        {
            LOG.info("### Done type system initialization in " + duration + ".");
        }
        else
        {
            LOG.info("### Done type system update in " + duration + ".");
        }
    }


    private static void logJunitInitOrUpdateStarted(boolean forcedInitialization)
    {
        LOG.info("###############################################################");
        if(forcedInitialization)
        {
            LOG.info(">>> Starting JUNIT system initialization");
        }
        else
        {
            LOG.info(">>> Starting JUNIT system update");
        }
        LOG.info("###############################################################");
    }


    private static void logJunitInitOrUpdateFinished(boolean forcedInitialization)
    {
        LOG.info("###############################################################");
        if(forcedInitialization)
        {
            LOG.info(">>> Done JUNIT system initialization");
        }
        else
        {
            LOG.info(">>> Done JUNIT system update");
        }
        LOG.info("###############################################################");
    }


    private static void logMasterSystemUpdateOrInitFinished(JspContext jspc, boolean forceInit, long time)
    {
        LOG.info("###############################################################");
        if(forceInit)
        {
            AbstractSystemCreator.logln("<br/><br/>FINISHED. The initialization took: " + Utilities.formatTime(time), jspc);
        }
        else
        {
            AbstractSystemCreator.logln("<br/><br/>FINISHED. The updating took: " + Utilities.formatTime(time), jspc);
        }
        LOG.info("###############################################################");
    }


    private static void logMasterSystemInitOrUpdateStarted(JspContext jspc, boolean forceInit)
    {
        LOG.info("###############################################################");
        if(forceInit)
        {
            AbstractSystemCreator.logln("<h3>Initialize system ...</h3>", jspc);
        }
        else
        {
            AbstractSystemCreator.logln("<h3>Updating system ...</h3>", jspc);
        }
        LOG.info("###############################################################");
    }


    private static void initializeSchemaAndTypeSystemFullyNewStyle(JspContext jspc)
    {
        boolean isFullInit = isForceInit(jspc.getServletRequest());
        boolean dryRun = isDryRun(jspc.getServletRequest());
        HybrisSchemaGenerator schemaGenerator = SchemaGenerator.createSchemaGenerator(isFullInit, dryRun);
        if(isFullInit)
        {
            AbstractSystemCreator.logln(">>> Creating schema and type system ...", jspc);
            schemaGenerator.initialize();
            if(!dryRun)
            {
                resetClusterIslandPK();
                changeSystemInitUpdateTimestamp();
            }
        }
        else
        {
            AbstractSystemCreator.logln(">>> Updating schema (and type system) ...", jspc);
            schemaGenerator.update();
        }
        if(!dryRun)
        {
            AbstractSystemCreator.logln(">>> Reloading persistence ...", jspc);
            ((DBPersistenceManager)Registry.getPersistenceManager()).reloadPersistenceInfos();
            AbstractSystemCreator.logln(">>> Clearing cache ...", jspc);
            Registry.getCurrentTenant().getCache().clear();
            AbstractSystemCreator.logln(">>> Initializing media storages ...", jspc);
            MediaManager.getInstance().initializeMediaStorage(isFullInit);
        }
    }


    protected static void resetClusterIslandPK()
    {
        SystemEJB.getInstance().getMetaInformationManager().resetSystemPK();
        Registry.getMasterTenant().updateClusterIslandPKFromDatabase();
    }


    protected static void changeSystemInitUpdateTimestamp()
    {
        String defaultOptLockSetting = Config.getParameter("hjmp.throw.concurrent.modification.exceptions");
        try
        {
            Config.setParameter("hjmp.throw.concurrent.modification.exceptions", "false");
            SystemEJB.getInstance().getMetaInformationManager().setSystemInitUpdateTimestamp(System.currentTimeMillis());
        }
        finally
        {
            Config.setParameter("hjmp.throw.concurrent.modification.exceptions", defaultOptLockSetting);
        }
    }


    public static boolean forceClean(Map params)
    {
        if(params == null)
        {
            return true;
        }
        Object value = params.get("force.clean");
        if(value == null)
        {
            return true;
        }
        return Boolean.parseBoolean((String)value);
    }


    private static void unPrepare(Map params, boolean success)
    {
        try
        {
            if(!isForcedInitialization(params))
            {
                markSystemInitialized(Registry.getCurrentTenant().getDataSource(), true);
            }
            else
            {
                markSystemInitialized(Registry.getCurrentTenant().getDataSource(), success);
            }
        }
        catch(Exception e2)
        {
            LOG.debug(e2);
        }
        Registry.getCurrentTenant().getDataSource().getConnectionPool().resetOracleStatementCaching();
    }


    private static void prepare(Map params)
    {
        if(isForcedInitialization(params))
        {
            if(!isCurrentTenantInitializing())
            {
                markSystemInitialized(Registry.getCurrentTenant().getDataSource(), false);
            }
        }
        Registry.getCurrentTenant().getDataSource().getConnectionPool().disableOracleStatementCaching();
    }


    private static boolean isForcedInitialization(Map params)
    {
        return forceClean(params);
    }


    public static SystemSetupCollector getSystemSetupCollector()
    {
        return (SystemSetupCollector)Registry.getGlobalApplicationContext().getBean("systemSetupCollector", SystemSetupCollector.class);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static boolean hasProjectData(Extension creator)
    {
        return true;
    }


    public static List<Extension> getCreators()
    {
        ExtensionManager extman = ExtensionManager.getInstance();
        List<Extension> creators = new ArrayList<>();
        for(Iterator<String> it = Utilities.getExtensionNames().iterator(); it.hasNext(); )
        {
            String extname = it.next();
            Extension ext = extman.getExtension(extname);
            if(!ext.isCreatorDisabled())
            {
                creators.add(ext);
            }
        }
        return creators;
    }


    private static List<ExtensionInfo> getAllExtensions()
    {
        return ConfigUtil.getPlatformConfig(Initialization.class).getExtensionInfosInBuildOrder();
    }


    public static void main(String[] args) throws Exception
    {
        if(args.length == 0)
        {
            if(LOG.isInfoEnabled())
            {
                LOG.info("Use: java de.hybris.platform.core.Initialization junit");
            }
        }
        if("junit".equals(args[0]))
        {
            initializeTestSystem();
        }
        else if(LOG.isInfoEnabled())
        {
            LOG.info("wrong parameter.");
        }
    }
}
