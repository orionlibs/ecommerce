package de.hybris.platform.util;

import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.util.logging.context.LoggingContextFactory;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Deprecated(since = "5.0", forRemoval = true)
public class RootRequestFilter implements Filter
{
    private static final Logger LOG = Logger.getLogger(RootRequestFilter.class);
    public static final String DEFAULT_TENANTID_COOKIE_NAME = "_hybris.tenantID_";
    public static final String DEFAULT_MATCHER_PATTERN = ";\\s*tenantID\\s*=\\s*(\\w+)";
    public static final String TENANT_ATTRIBUTE_MAP_PREFIX = "TENANT_ATTRIBUTES_";
    public static final boolean SERVLET_COMPATIBILITY = false;
    FilterConfig filterConfig;
    private Pattern tenantSystemIDPattern;
    private boolean gzip;
    private boolean regExp;
    private boolean initializedOnly;
    private boolean skippedInitSystemTest;
    private boolean redirectOnSystemInit;
    private boolean useCookies4TenantID;
    private boolean touchHttpSessionVar;
    private String tenantIDCookieName;


    protected static Tenant getCurrentTenant()
    {
        Tenant t = Registry.getCurrentTenantNoFallback();
        if(t == null)
        {
            throw new IllegalStateException("no tenant active");
        }
        return t;
    }


    protected HttpServletRequest wrapRequest(HttpServletRequest original, HttpServletResponse response, FilterChain chain)
    {
        return (HttpServletRequest)new HybrisRequestWrapper(this, original, response);
    }


    protected HttpServletResponse wrapResponse(HttpServletResponse original, FilterChain chain, boolean compress, boolean mustEncode, boolean useRegExp)
    {
        return compress ? (HttpServletResponse)new HybrisGZIPResponseWrapper(this, original, mustEncode, useRegExp) : (HttpServletResponse)new HybrisResponseWrapper(this, original, mustEncode);
    }


    protected boolean assureNeverCalledBefore(ServletRequest _request)
    {
        String ROOT_REQUEST_PARAM = "rootRequest_" + getFilterConfig().getFilterName();
        if(_request.getAttribute(ROOT_REQUEST_PARAM) == null)
        {
            _request.setAttribute(ROOT_REQUEST_PARAM, Boolean.TRUE);
            return true;
        }
        return false;
    }


    protected void writeShutDownPage(HttpServletResponse resp) throws IOException
    {
        resp.setContentType("text/plain");
        resp.getOutputStream().println("Shutdown / Restart in progress!");
    }


    private void writeInfoPage(ServletResponse resp) throws IOException
    {
        resp.setContentType("text/plain");
        resp.getOutputStream().println("This server runs hybris Suite software (www.hybris.com)");
        resp.getOutputStream().println("");
        resp.getOutputStream().println("Build version: " + Config.getParameter("build.version"));
    }


    public final void doFilter(ServletRequest _request, ServletResponse _response, FilterChain chain) throws IOException, ServletException
    {
        if(assureNeverCalledBefore(_request))
        {
            boolean compress;
            if(RedeployUtilities.isShutdownInProgress())
            {
                HttpServletResponse resp = (HttpServletResponse)_response;
                writeShutDownPage(resp);
                return;
            }
            StringBuffer request = ((HttpServletRequest)_request).getRequestURL();
            String query = ((HttpServletRequest)_request).getQueryString();
            String requestString = (request == null) ? "" : request.toString();
            query = (query == null) ? "" : query;
            String all = requestString + requestString;
            if(all.contains("hybrissuite$info"))
            {
                writeInfoPage(_response);
                return;
            }
            LoggingContextFactory.getLoggingContextHandler().put("RemoteAddr", Utilities.getIPAddressForLogOutput(_request.getRemoteAddr()).toString());
            if(this.gzip)
            {
                String ae = ((HttpServletRequest)_request).getHeader("accept-encoding");
                compress = (ae != null && ae.indexOf("gzip") != -1);
            }
            else
            {
                compress = false;
            }
            try
            {
                adjustEncodings((HttpServletRequest)_request, (HttpServletResponse)_response);
                touchHttpSession((HttpServletRequest)_request);
                boolean doAppend = handleTenantID((HttpServletRequest)_request, (HttpServletResponse)_response);
                handleSlaveSettings((HttpServletRequest)_request, (HttpServletResponse)_response);
                HttpServletRequest httpServletRequest = wrapRequest((HttpServletRequest)_request, (HttpServletResponse)_response, chain);
                HttpServletResponse response = wrapResponse((HttpServletResponse)_response, chain, compress, doAppend, (doAppend && this.regExp));
                assignCurrentRequestToThread(httpServletRequest);
                if(!this.initializedOnly || JaloConnection.getInstance().isSystemInitialized())
                {
                    RequestAttributes previousRequestAttributes = RequestContextHolder.getRequestAttributes();
                    ServletRequestAttributes requestAttributes = new ServletRequestAttributes(httpServletRequest);
                    RequestContextHolder.setRequestAttributes((RequestAttributes)requestAttributes);
                    try
                    {
                        if(doPreRequest(httpServletRequest, response))
                        {
                            try
                            {
                                String forwardTarget = doForward(httpServletRequest, response);
                                if(forwardTarget != null)
                                {
                                    getFilterConfig().getServletContext()
                                                    .getRequestDispatcher(
                                                                    (forwardTarget.charAt(0) == '/') ? forwardTarget : ("/" + forwardTarget))
                                                    .forward((ServletRequest)httpServletRequest, (ServletResponse)response);
                                }
                                else
                                {
                                    chain.doFilter((ServletRequest)httpServletRequest, (ServletResponse)response);
                                }
                                if(compress)
                                {
                                    ((HybrisGZIPResponseWrapper)response).finishResponse();
                                }
                                boolean inv = true;
                                try
                                {
                                    httpServletRequest.getSession().getAttribute("test");
                                    inv = false;
                                }
                                catch(IllegalStateException illegalStateException)
                                {
                                }
                                if(!inv)
                                {
                                    doPostRequest(httpServletRequest, response);
                                }
                            }
                            finally
                            {
                                for(JaloSession toClose : ((HybrisRequestWrapper)httpServletRequest).getSessionsToClose())
                                {
                                    toClose.close();
                                }
                            }
                        }
                    }
                    finally
                    {
                        RequestContextHolder.setRequestAttributes(previousRequestAttributes);
                    }
                }
                else
                {
                    if(this.redirectOnSystemInit)
                    {
                        httpServletRequest.setAttribute("requesturl", getURI(httpServletRequest));
                        String redirectionURL = MasterTenant.getInstance().getConfig().getString("webapps.redirection.url", "/maintenance");
                        redirectionURL = response.encodeRedirectURL(redirectionURL + "?backurl=" + redirectionURL);
                        response.sendRedirect(redirectionURL);
                    }
                    else
                    {
                        for(Enumeration<String> en = httpServletRequest.getSession().getAttributeNames(); en.hasMoreElements(); )
                        {
                            httpServletRequest.getSession().removeAttribute(en.nextElement());
                        }
                        chain.doFilter((ServletRequest)httpServletRequest, (ServletResponse)response);
                    }
                    if(compress)
                    {
                        ((HybrisGZIPResponseWrapper)response).finishResponse();
                    }
                }
            }
            finally
            {
                JaloSession.deactivate();
                Registry.unsetCurrentTenant();
                clearCurrentRequestFromThread();
                LoggingContextFactory.getLoggingContextHandler().remove("RemoteAddr");
            }
        }
        else
        {
            chain.doFilter(_request, _response);
        }
    }


    protected void assignCurrentRequestToThread(HttpServletRequest request)
    {
        WebSessionFunctions.setCurrentHttpServletRequest(request);
    }


    protected void clearCurrentRequestFromThread()
    {
        WebSessionFunctions.clearCurrentHttpServletRequest();
    }


    protected void touchHttpSession(HttpServletRequest request)
    {
        if(this.touchHttpSessionVar && request != null)
        {
            HttpSession session = request.getSession();
            if(session != null)
            {
                String name = "jalosession";
                String tenantID = getTenantSystemID(request);
                if(tenantID != null)
                {
                    name = "TENANT_ATTRIBUTES_" + tenantID;
                }
                Object attribute = session.getAttribute(name);
                session.removeAttribute(name);
                session.setAttribute(name, attribute);
            }
        }
    }


    protected void adjustEncodings(HttpServletRequest _request, HttpServletResponse _response)
    {
        try
        {
            _request.setCharacterEncoding("UTF-8");
        }
        catch(UnsupportedEncodingException e1)
        {
            e1.printStackTrace();
        }
        _response.setCharacterEncoding("UTF-8");
    }


    protected String doForward(HttpServletRequest request, HttpServletResponse response)
    {
        return null;
    }


    protected boolean doPreRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException
    {
        return true;
    }


    protected void doPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException
    {
    }


    protected FilterConfig getFilterConfig()
    {
        return this.filterConfig;
    }


    protected void handleSlaveSettings(HttpServletRequest _request, HttpServletResponse _response)
    {
        Tenant t = getCurrentTenant();
        String slaveMode = _request.getParameter("slave");
        if(StringUtils.isNotBlank(slaveMode))
        {
            if("true".equalsIgnoreCase(slaveMode))
            {
                String assignedID = getSlaveDataSourceID(_request);
                if(assignedID != null)
                {
                    try
                    {
                        t.activateSlaveDataSource(assignedID);
                    }
                    catch(IllegalArgumentException e)
                    {
                        assignedID = null;
                    }
                }
                if(assignedID == null)
                {
                    assignedID = t.activateSlaveDataSource();
                    setSlaveDataSourceID(assignedID, _response);
                }
            }
            else
            {
                setSlaveDataSourceID(null, _response);
            }
        }
        else
        {
            String assignedID = getSlaveDataSourceID(_request);
            if(assignedID != null)
            {
                try
                {
                    t.activateSlaveDataSource(assignedID);
                }
                catch(IllegalArgumentException e)
                {
                    setSlaveDataSourceID(null, _response);
                }
            }
        }
    }


    protected void setSlaveDataSourceID(String id, HttpServletResponse response)
    {
        Cookie c = new Cookie("use.slave.datasource", id);
        c.setMaxAge((id == null) ? 0 : -1);
        c.setPath("/");
        c.setHttpOnly(true);
        response.addCookie(c);
    }


    protected String getSlaveDataSourceID(HttpServletRequest request)
    {
        String id = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null)
        {
            for(Cookie c : cookies)
            {
                if("use.slave.datasource".equalsIgnoreCase(c.getName()))
                {
                    id = c.getValue();
                }
            }
        }
        return id;
    }


    protected boolean handleTenantID(HttpServletRequest _request, HttpServletResponse _response)
    {
        TenantInformation tenantInformationFromRequest = getTenantInformationFrom(_request);
        String tenantID = tenantInformationFromRequest.getTenantId();
        boolean mustEncodeIfSlave = tenantInformationFromRequest.getMustEncodeIfSlave();
        if(tenantID != null && !"master".equalsIgnoreCase(tenantID))
        {
            Registry.setCurrentTenantByID(tenantID);
            if(this.initializedOnly && !JaloConnection.getInstance().isSystemInitialized())
            {
                Registry.unsetCurrentTenant();
            }
        }
        if(!Registry.hasCurrentTenant())
        {
            Registry.activateMasterTenantAndFailIfAlreadySet();
            if(this.initializedOnly && !JaloConnection.getInstance().isSystemInitialized())
            {
                Registry.unsetCurrentTenant();
            }
            tenantID = null;
        }
        setTenantID(tenantID, _request, _response);
        return (mustEncodeIfSlave && tenantID != null && !"master".equalsIgnoreCase(tenantID));
    }


    protected TenantInformation getTenantInformationFrom(HttpServletRequest _request)
    {
        boolean mustEncodeIfSlave = true;
        String tenantID = null;
        try
        {
            String uri = getURI(_request);
            String storedTenantID = assertValidTenantID(getTenantSystemID(_request), false);
            String tokenTenantID = assertValidTenantID(matchTenantSystemIDToken(uri), true);
            if(tokenTenantID != null)
            {
                if(storedTenantID != null)
                {
                    tenantID = tokenTenantID;
                    mustEncodeIfSlave = false;
                }
                else
                {
                    tenantID = tokenTenantID;
                }
            }
            else if(storedTenantID != null)
            {
                mustEncodeIfSlave = false;
                tenantID = storedTenantID;
            }
            else
            {
                tenantID = assertValidTenantID(matchTenantSystemIDHost(uri), false);
                if(tenantID != null)
                {
                    mustEncodeIfSlave = false;
                }
            }
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage());
        }
        return new TenantInformation(this, tenantID, mustEncodeIfSlave);
    }


    protected String getURI(HttpServletRequest _request)
    {
        return _request.getRequestURI();
    }


    protected void setTenantID(String tenantID, HttpServletRequest request, HttpServletResponse response)
    {
        if(this.useCookies4TenantID)
        {
            Cookie c = new Cookie(this.tenantIDCookieName, tenantID);
            c.setMaxAge((tenantID == null) ? 0 : -1);
            c.setPath("/");
            c.setHttpOnly(true);
            response.addCookie(c);
        }
    }


    protected String getTenantSystemID(HttpServletRequest request)
    {
        String id = null;
        if(this.useCookies4TenantID)
        {
            Cookie[] cookies = request.getCookies();
            if(cookies != null)
            {
                for(Cookie c : cookies)
                {
                    if(this.tenantIDCookieName.equalsIgnoreCase(c.getName()))
                    {
                        id = c.getValue();
                    }
                }
            }
        }
        return id;
    }


    protected String assertValidTenantID(String id, boolean allowMaster)
    {
        if(id != null && id
                        .length() > 0 && ((allowMaster && "master"
                        .equalsIgnoreCase(id)) || Registry.getMasterTenant()
                        .getSlaveTenant(id) != null))
        {
            return id;
        }
        return null;
    }


    protected String matchTenantSystemIDToken(String uri)
    {
        Matcher m = this.tenantSystemIDPattern.matcher(uri);
        if(m.find())
        {
            return m.group(1);
        }
        return null;
    }


    protected String matchTenantSystemIDHost(String uri)
    {
        try
        {
            URI u = new URI(uri);
            return u.getHost();
        }
        catch(URISyntaxException e)
        {
            System.err
                            .println("cannot analyze the URL, because it seems that there are not correctly encoded characters in it. See exception below:");
            e.printStackTrace();
            return null;
        }
    }


    public void init(FilterConfig filterConfig) throws ServletException
    {
        this.filterConfig = filterConfig;
        String pattern = filterConfig.getInitParameter("tenantIDPattern");
        if(pattern == null || pattern.length() == 0)
        {
            pattern = ";\\s*tenantID\\s*=\\s*(\\w+)";
        }
        try
        {
            this.tenantSystemIDPattern = Pattern.compile(pattern, 2);
        }
        catch(PatternSyntaxException e)
        {
            System.err.println("illegal tenant id pattern '" + pattern + "' : " + e.getMessage());
        }
        this.gzip = !"false".equalsIgnoreCase(filterConfig.getInitParameter("enable.compression"));
        this.regExp = (this.gzip && "true".equalsIgnoreCase(filterConfig.getInitParameter("enable.buffer.regexp")));
        this.initializedOnly = "true".equalsIgnoreCase(filterConfig.getInitParameter("initialized.only"));
        this.skippedInitSystemTest = "true".equalsIgnoreCase(filterConfig.getInitParameter("skip.initsystemtest"));
        if(this.skippedInitSystemTest && this.initializedOnly)
        {
            LOG.warn("Within servlet context name: " + filterConfig.getServletContext().getServletContextName()
                            + " the combination of parameters is defined: initialized.only=true and skip.initsystemtest=true. Be aware that where property initialized.only is set to true the db statement in order to determine if the system is initialize will be executed partially(even if skip.initsystemtest is set to true).");
        }
        this.redirectOnSystemInit = "true".equalsIgnoreCase(filterConfig.getInitParameter("redirectOnSystemInit"));
        this.useCookies4TenantID = !"false".equalsIgnoreCase(filterConfig.getInitParameter("tenantID.cookies"));
        if(this.useCookies4TenantID)
        {
            this.tenantIDCookieName = filterConfig.getInitParameter("tenantID.cookie.name");
            if(StringUtils.isBlank(this.tenantIDCookieName))
            {
                this.tenantIDCookieName = "_hybris.tenantID_";
            }
        }
        this.touchHttpSessionVar = "true".equalsIgnoreCase(filterConfig.getInitParameter("touch.httpSession"));
    }


    public void destroy()
    {
        this.filterConfig = null;
    }


    public boolean isSkippedInitSystemTest()
    {
        return this.skippedInitSystemTest;
    }


    public void setSkippedInitSystemTest(boolean skippedInitSystemTest)
    {
        this.skippedInitSystemTest = skippedInitSystemTest;
    }
}
