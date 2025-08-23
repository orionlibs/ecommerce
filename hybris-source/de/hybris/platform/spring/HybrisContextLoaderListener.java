package de.hybris.platform.spring;

import com.google.common.base.Joiner;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.ClassLoaderUtils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.web.SessionCloseStrategy;
import de.hybris.platform.spring.ctx.HybrisWebApplicationBeanFactoryPostProcessor;
import de.hybris.platform.spring.ctx.TenantIgnoreXmlWebApplicationContext;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HybrisContextLoaderListener extends ContextLoaderListener
{
    private static final ClassLoaderUtils clu = null;
    private static final Log LOG = (Log)ClassLoaderUtils.executeWithWebClassLoaderParentIfNeeded(HybrisContextLoaderListener::getLogger);
    protected static final String ADDITIONAL_WEB_SPRING_CONFIGS = ".additionalWebSpringConfigs";
    protected static final String ADDITIONAL_WEB_SPRING_CONFIGS_FROM_EXT = ".additionalWebSpringConfigs.";
    public static final String CORE_SPRING_DEVELOPMENT_MODE_FLAG_KEY = "spring.lazy.load.singletons";
    public static final String WEB_SPRING_DEVELOPMENT_MODE_FLAG_KEY = "web.spring.lazy.load.singletons";


    private static Log getLogger()
    {
        return LogFactory.getLog(HybrisContextLoaderListener.class.getName());
    }


    private static final HttpServletRequest DUMMY_REQUEST = (HttpServletRequest)new MockHttpServletRequest();
    private static final String HYBRIS_WEB_TENANT = "hybris.web.tenant";
    private SessionCloseStrategy sessionCloseStrategy;


    public void contextInitialized(ServletContextEvent event)
    {
        ServletContextStartupWatcher.StartingContext stCtx = ServletContextStartupWatcher.getStartupServletContextWatcher().newServletContextStarted();
        try
        {
            contextInitializedInternal(event);
            if(stCtx != null)
            {
                stCtx.close();
            }
        }
        catch(Throwable throwable)
        {
            if(stCtx != null)
            {
                try
                {
                    stCtx.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    protected void contextInitializedInternal(ServletContextEvent event)
    {
        super.contextInitialized(event);
        registerHttpSessionListener(event.getServletContext());
    }


    protected void registerHttpSessionListener(ServletContext servletContext)
    {
        String webApp = servletContext.getContextPath();
        servletContext.addListener((EventListener)new Object(this, webApp, servletContext));
        if(LOG.isInfoEnabled())
        {
            LOG.info("Registered HttpSession timeout listener for '" + (StringUtils.isNotBlank(webApp) ? webApp : "/") + "'");
        }
    }


    protected void runWithTenant(String tenantId, Runnable runnable)
    {
        try
        {
            Registry.runAsTenant(Registry.getTenantByID(tenantId), () -> {
                runnable.run();
                return null;
            });
        }
        catch(Exception e)
        {
            throw new SystemException(e);
        }
    }


    protected void runWithActiveJaloSession(String tenantId, HttpSession httpSession, Runnable runnable)
    {
        try
        {
            Registry.runAsTenant(Registry.getTenantByID(tenantId), () -> {
                boolean deactivateJaloSession = activateJaloSession(httpSession);
                runnable.run();
                if(deactivateJaloSession)
                {
                    AbstractTenant currentTenant = (AbstractTenant)Registry.getCurrentTenantNoFallback();
                    currentTenant.setActiveSessionForCurrentThread(null);
                }
                return null;
            });
        }
        catch(Exception e)
        {
            throw new SystemException(e);
        }
    }


    private boolean activateJaloSession(HttpSession httpSession)
    {
        if(Registry.getCurrentTenantNoFallback().getActiveSession() != null)
        {
            return false;
        }
        JaloSession js = (JaloSession)httpSession.getAttribute("jalosession");
        if(js != null && !js.isClosed() && JaloSession.assureSessionNotStale(js) && !RedeployUtilities.isShutdownInProgress())
        {
            js.activate();
            return true;
        }
        return false;
    }


    public WebApplicationContext initWebApplicationContext(ServletContext servletContext)
    {
        WebApplicationContext ctx = doInitWebApplicationContext(servletContext);
        this.sessionCloseStrategy = (SessionCloseStrategy)ctx.getBean("sessionCloseStrategy", SessionCloseStrategy.class);
        return ctx;
    }


    protected WebApplicationContext doInitWebApplicationContext(ServletContext servletContext)
    {
        long start = System.currentTimeMillis();
        RegistrableThread.registerThread(
                        OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).asNotSuspendableOperation().build());
        startRegistry();
        RequestAttributes reqAttrBefore = RequestContextHolder.getRequestAttributes();
        Tenant tenantBefore = Registry.getCurrentTenantNoFallback();
        try
        {
            adjustTenant(servletContext);
            if(reqAttrBefore == null)
            {
                RequestContextHolder.setRequestAttributes((RequestAttributes)new ServletRequestAttributes(DUMMY_REQUEST));
            }
            return super.initWebApplicationContext(servletContext);
        }
        catch(TenantNotFoundException tnfe)
        {
            LOG.error("There has been defined a context " + servletContext.getContextPath() + " for tenant but no tenant configuration found in platform/tenant_{tenantID}.properties or config/local_tenant_{tenantID}.properties details ," + tnfe
                            .getMessage() + "\n.!!!! The application mounted at " + servletContext.getContextPath() + " will not work correctly.!!!!");
            return null;
        }
        finally
        {
            if(tenantBefore == null)
            {
                Registry.unsetCurrentTenant();
            }
            else
            {
                Registry.setCurrentTenant(tenantBefore);
            }
            if(reqAttrBefore == null)
            {
                RequestContextHolder.resetRequestAttributes();
            }
            RegistrableThread.unregisterThread();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("##### init web app context took " + System.currentTimeMillis() - start + " for app <<" + servletContext
                                .getContextPath() + ">>");
            }
        }
    }


    protected ApplicationContext loadParentContext(ServletContext servletContext) throws BeansException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("loadParentContext" + servletContext
                            .getContextPath() + " for tenant " + Registry.getCurrentTenantNoFallback());
        }
        return getCoreApplicationContext();
    }


    protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext applicationContext)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("customizeContext " + servletContext
                            .getContextPath() + " for tenant " + Registry.getCurrentTenantNoFallback());
        }
        super.customizeContext(servletContext, applicationContext);
        String appName = StringUtils.deleteWhitespace(servletContext.getServletContextName());
        List<String> locations = new ArrayList<>();
        fillConfigLocations(appName, locations);
        addCustomConfigLocations(applicationContext, locations);
        applicationContext.addBeanFactoryPostProcessor((BeanFactoryPostProcessor)new HybrisWebApplicationBeanFactoryPostProcessor());
    }


    private ApplicationContext getCoreApplicationContext()
    {
        return Registry.getCoreApplicationContext();
    }


    protected void adjustTenant(ServletContext servletContext) throws TenantNotFoundException
    {
        String tenantID = getTenanId(servletContext.getContextPath());
        if(tenantID == null)
        {
            throw new TenantNotFoundException("Can not find the tenant mapping for context " + servletContext.getContextPath());
        }
        attachTenantIDToServletContext(servletContext, tenantID);
        Registry.setCurrentTenantByID(tenantID);
    }


    public static String getTenantIDForWebapp(ServletContext ctx)
    {
        return (String)ctx.getAttribute("hybris.web.tenant");
    }


    protected void attachTenantIDToServletContext(ServletContext ctx, String tenantID)
    {
        ctx.setAttribute("hybris.web.tenant", tenantID);
    }


    protected void startRegistry()
    {
        ClassLoaderUtils.executeWithWebClassLoaderParentIfNeeded(Registry::startup);
    }


    protected WebApplicationContext createWebApplicationContext(ServletContext servletContext)
    {
        String tenantId = getTenanId(servletContext.getContextPath());
        String contextPath = servletContext.getContextPath();
        return (WebApplicationContext)new TenantIgnoreXmlWebApplicationContext(tenantId, contextPath);
    }


    protected String getTenanId(String contextPath)
    {
        return Utilities.getTenantIdForContext(contextPath, "master");
    }


    protected void fillConfigLocations(String appName, List<String> locations)
    {
        fillLegacyConfigLocations(appName, locations);
        fillConfigLocationsFromExtensions(appName, locations);
    }


    protected void fillLegacyConfigLocations(String appName, List<String> locations)
    {
        String parameterKey = appName + ".additionalWebSpringConfigs";
        fillConfigLocationsFromParameter(parameterKey, locations);
    }


    protected void fillConfigLocationsFromExtensions(String appName, List<String> locations)
    {
        for(Extension extension : getExtensions())
        {
            fillConfigLocationsFromExtension(appName, extension.getName(), locations);
        }
    }


    List<? extends Extension> getExtensions()
    {
        return ExtensionManager.getInstance().getExtensions();
    }


    protected void fillConfigLocationsFromExtension(String appName, String fromExtension, List<String> locations)
    {
        String parameterKey = appName + ".additionalWebSpringConfigs." + appName;
        fillConfigLocationsFromParameter(parameterKey, locations);
    }


    protected void fillConfigLocationsFromParameter(String parameterKey, List<String> locations)
    {
        String additionalSpringConfigsStr = readConfigParameter(parameterKey);
        if(StringUtils.isNotBlank(additionalSpringConfigsStr))
        {
            String[] customConfLocations = StringUtils.split(additionalSpringConfigsStr, ',');
            if(customConfLocations != null && customConfLocations.length > 0)
            {
                Collections.addAll(locations, customConfLocations);
            }
        }
    }


    protected void addCustomConfigLocations(ConfigurableWebApplicationContext applicationContext, List<String> customConfLocations)
    {
        if(customConfLocations != null && !customConfLocations.isEmpty())
        {
            List<String> confLocations = new ArrayList<>();
            String[] stdConfLocations = applicationContext.getConfigLocations();
            if(stdConfLocations != null && stdConfLocations.length > 0)
            {
                Collections.addAll(confLocations, stdConfLocations);
            }
            for(String customContextLocation : customConfLocations)
            {
                confLocations.add(StringUtils.deleteWhitespace(customContextLocation));
            }
            LOG.info("*************************************************************");
            LOG.info("adding custom config locations [" + Joiner.on(",").join(confLocations) + "] for ctx " + applicationContext);
            LOG.info("*************************************************************");
            applicationContext.setConfigLocations(confLocations.<String>toArray(new String[confLocations.size()]));
        }
    }


    private ConfigIntf getConfig()
    {
        return Utilities.getConfig();
    }


    protected String readConfigParameter(String parameterKey)
    {
        return getConfig().getParameter(parameterKey);
    }
}
