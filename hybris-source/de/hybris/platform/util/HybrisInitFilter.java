package de.hybris.platform.util;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloConnectException;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemNotInitializedException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

@Deprecated(since = "5.0", forRemoval = true)
public class HybrisInitFilter extends RootRequestFilter
{
    private static final String CACHING_MODEL_SERVICE_LIST_OF_TYPES = "caching.model.service.list.of.types";
    private static final String TENANT_MISMATCH_CHECK = "tenant.mismatch.check";
    @Deprecated(since = "ages", forRemoval = true)
    private static final String ENABLE_MODEL_CACHING_FOR_TYPES = "model.enable.caching.for";
    private static final String FETCH_ALWAYS = "fetchAlways";
    private static final Logger LOG = Logger.getLogger(HybrisInitFilter.class.getName());
    private volatile Set<PK> cachingModelServiceListOfTypes = null;
    private boolean checkTenantMismatch = false;
    private Boolean useBlacklist = null;
    private Boolean useServletContext = null;
    private Boolean fetchAlways = null;
    private String cachingModelServiceList = null;


    public void init(FilterConfig filterConfig) throws ServletException
    {
        super.init(filterConfig);
        this.cachingModelServiceList = filterConfig.getInitParameter("model.enable.caching.for");
        if(this.cachingModelServiceList == null)
        {
            this.cachingModelServiceList = filterConfig.getInitParameter("caching.model.service.list.of.types");
        }
        if(filterConfig.getInitParameter("fetchAlways") != null)
        {
            this.fetchAlways = Boolean.valueOf(filterConfig.getInitParameter("fetchAlways"));
        }
        if(filterConfig.getInitParameter("useServletContext") != null)
        {
            this.useServletContext = Boolean.valueOf(filterConfig.getInitParameter("useServletContext"));
        }
        if(filterConfig.getInitParameter("useBlacklist") != null)
        {
            this.useBlacklist = Boolean.valueOf(filterConfig.getInitParameter("useBlacklist"));
        }
        if(filterConfig.getInitParameter("tenant.mismatch.check") != null)
        {
            this.checkTenantMismatch = Boolean.valueOf(filterConfig.getInitParameter("tenant.mismatch.check")).booleanValue();
        }
    }


    public boolean doPreRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException
    {
        try
        {
            getJaloSession(request, response, true);
            notifyExtensions(request, response, true);
            initializeCachingModelServiceListOfTypes();
            if(this.fetchAlways != null)
            {
                JaloSession.getCurrentSession().getSessionContext().setAttribute("fetchAlways", this.fetchAlways);
            }
            if(this.useServletContext != null && this.useServletContext.booleanValue())
            {
                ServletContext ctx = request.getSession().getServletContext();
                if(this.cachingModelServiceListOfTypes != null && ctx.getAttribute("cachingModelServiceListOfTypes") == null)
                {
                    ctx.setAttribute("cachingModelServiceListOfTypes", this.cachingModelServiceListOfTypes);
                }
                if(this.useBlacklist != null)
                {
                    ctx.setAttribute("useBlacklist", this.useBlacklist);
                }
            }
            else
            {
                SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
                if(this.cachingModelServiceListOfTypes != null && ctx.getAttribute("cachingModelServiceListOfTypes") == null)
                {
                    ctx.setAttribute("cachingModelServiceListOfTypes", this.cachingModelServiceListOfTypes);
                }
                if(this.useBlacklist != null)
                {
                    ctx.setAttribute("useBlacklist", this.useBlacklist);
                }
            }
            return true;
        }
        catch(JaloConnectException e)
        {
            throw new ServletException("error getting session ", e);
        }
    }


    private void initializeCachingModelServiceListOfTypes()
    {
        if(this.cachingModelServiceList != null && this.cachingModelServiceListOfTypes == null &&
                        JaloConnection.getInstance().isSystemInitialized())
        {
            synchronized(this)
            {
                if(this.cachingModelServiceListOfTypes == null)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("caching enabled");
                    }
                    this.cachingModelServiceListOfTypes = new HashSet<>();
                    String[] typeCodes = this.cachingModelServiceList.split(",");
                    for(String typeCode : typeCodes)
                    {
                        try
                        {
                            ComposedType type = TypeManager.getInstance().getComposedType(typeCode);
                            this.cachingModelServiceListOfTypes.add(type.getPK());
                        }
                        catch(JaloItemNotFoundException e)
                        {
                            LOG.warn("Can not add type " + typeCode + " to list of caching enabled types", (Throwable)e);
                        }
                    }
                }
            }
        }
    }


    protected void notifyExtensions(HttpServletRequest request, HttpServletResponse response, boolean pre)
    {
        for(Extension e : ExtensionManager.getInstance().getExtensions())
        {
            try
            {
                if(e instanceof WebRequestInterceptor)
                {
                    if(pre)
                    {
                        ((WebRequestInterceptor)e).doPreRequest(request, response);
                        continue;
                    }
                    ((WebRequestInterceptor)e).doPostRequest(request, response);
                }
            }
            catch(Exception ex)
            {
                LOG.error("error in " + e.getName() + (pre ? ".doPreRequest()" : ".doPostRequest()") + " : " + ex.getMessage());
                LOG.error(Utilities.getStackTraceAsString(ex));
            }
        }
    }


    public void doPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException
    {
        try
        {
            if(getJaloSession(request, response, false) != null)
            {
                notifyExtensions(request, response, false);
            }
            else if(LOG.isDebugEnabled())
            {
                LOG.debug("doPostRequest: cannot notify extensions since no jalo session is available.");
            }
        }
        catch(JaloSystemNotInitializedException e)
        {
            throw new ServletException("error getting session ", e);
        }
        catch(JaloConnectException e)
        {
            throw new ServletException("error getting session ", e);
        }
        finally
        {
            if(JaloSession.hasCurrentSession())
            {
                JaloSession.deactivate();
            }
        }
    }


    protected String getLoginCookieName()
    {
        return null;
    }


    protected JaloSession getJaloSession(HttpServletRequest request, HttpServletResponse response, boolean pre) throws JaloConnectException
    {
        if(pre)
        {
            JaloSession jaloSession = WebSessionFunctions.getSession(getLoginCookieName(), request.getSession(), request, response,
                            isSkippedInitSystemTest());
            if(!JaloSession.hasCurrentSession() && jaloSession != null)
            {
                jaloSession.activate();
            }
            return jaloSession;
        }
        if(JaloSession.hasCurrentSession())
        {
            return JaloSession.getCurrentSession();
        }
        JaloSession ret = WebSessionFunctions.tryGetJaloSession(request.getSession());
        if(ret == null)
        {
            if(!response.isCommitted())
            {
                ret = WebSessionFunctions.getSession(request, isSkippedInitSystemTest());
            }
        }
        if(ret != null)
        {
            ret.activate();
        }
        return ret;
    }


    protected String matchTenantSystemIDToken(String uri)
    {
        String context = Utilities.getContextFromRequestUri(uri);
        String contextBasedTenantId = Utilities.getTenantIdForContext(context, "master");
        if(this.checkTenantMismatch)
        {
            checkTenantAmbiguity(uri, contextBasedTenantId);
        }
        return contextBasedTenantId;
    }


    private void checkTenantAmbiguity(String uri, String contextBasedTenantId)
    {
        String parambasedTenantId = super.matchTenantSystemIDToken(uri);
        if(!StringUtils.equalsIgnoreCase(parambasedTenantId, contextBasedTenantId))
        {
            LOG.warn("!!!!!!!!!!! Tenant id conflict found !!!!!!!!!!!!!!");
            LOG.warn(" Determined tenant id for context " + uri + " is " + contextBasedTenantId + ", but found obsolete tenantID param which points to different tenant " + parambasedTenantId + " which will be ignored");
            LOG.warn("Please refer to documentation to avoid this ambiguity.");
        }
    }
}
