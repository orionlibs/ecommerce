package de.hybris.platform.servicelayer.web;

import com.google.common.base.Splitter;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.web.session.stale.StaleSessionCheckingFilterChain;
import de.hybris.platform.servicelayer.web.session.stale.StaleSessionStrategy;
import de.hybris.platform.servicelayer.web.session.stale.StaleSessionSupport;
import de.hybris.platform.spring.HybrisContextLoaderListener;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.WebSessionFunctions;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.logging.context.LoggingContextFactory;
import de.hybris.platform.util.logging.context.LoggingContextHandler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.GenericFilterBean;

public class SessionFilter extends GenericFilterBean
{
    public static final String TOMCAT_REPLICATION_SUPPORT = "session.replication.support";
    public static final String SESSION_SERIALIZATION_CHECK_PROPERTY = "session.serialization.check";
    public static final String SESSION_SERIALIZATION_EXTENSIONS_PROPERTY = "session.serialization.check.extensions";
    public static final String SERIALIZATION_CHECK_RESPONSE_ERROR = "session.serialization.check.response.error";
    private static final String JALOSESSION = "jalosession";
    private static final Logger LOG = LoggerFactory.getLogger(SessionFilter.class.getName());
    private static final Splitter CSV_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();
    private static final LoggingContextHandler LOG_CTX = LoggingContextFactory.getLoggingContextHandler();
    private SessionService sessionService;
    private StaleSessionSupport staleSessionSupport;
    private boolean touchHttpSession;
    private volatile Boolean enabled;


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        StaleSessionCheckingFilterChain staleSessionCheckingFilterChain = getStaleSessionSupport().getStaleSessionCheckingFilterChain(filterChain);
        doFilterInternal(request, response, (FilterChain)staleSessionCheckingFilterChain);
        StaleSessionStrategy.Action staleSessionAction = staleSessionCheckingFilterChain.getStaleSessionAction();
        if(staleSessionAction == null || StaleSessionStrategy.Action.BREAK_REQUEST_PROCESSING.equals(staleSessionAction))
        {
            return;
        }
        if(StaleSessionStrategy.Action.CONTINUE_REQUEST_PROCESSING.equals(staleSessionAction))
        {
            doFilterInternal(request, response, filterChain);
            return;
        }
        throw new IllegalStateException("Unknown stale session action `" + staleSessionAction + "`.");
    }


    private void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        HttpSession httpSession = httpRequest.getSession();
        boolean enableUserIdInLogginContext = false;
        try
        {
            activateSession(httpSession, httpRequest);
            JaloSession jaloSession = JaloSession.getCurrentSession();
            enableUserIdInLogginContext = (isUserIdEnabledInLoggingContext(httpRequest) && jaloSession != null && jaloSession.getUser() != null);
            if(isSessionSerializationCheckEnabled(httpRequest) && isExtensionEnabledForSessionChecking(httpRequest))
            {
                assureAllSessionAttributesAreSerializable(httpResponse, httpSession);
            }
            if(enableUserIdInLogginContext)
            {
                LOG_CTX.put("UserId", String.valueOf(jaloSession.getUser().getPK()));
            }
            filterChain.doFilter((ServletRequest)httpRequest, (ServletResponse)httpResponse);
        }
        finally
        {
            if(enableUserIdInLogginContext)
            {
                LOG_CTX.remove("UserId");
            }
            resetJaloSessionForSessionReplication(httpSession);
            deactivateSession(httpSession);
        }
    }


    private boolean isUserIdEnabledInLoggingContext(HttpServletRequest request)
    {
        String tenantIDForWebapp = HybrisContextLoaderListener.getTenantIDForWebapp(request.getServletContext());
        ConfigIntf config = Registry.getTenantByID(tenantIDForWebapp).getConfig();
        return config.getBoolean("loggingContext.UserId.enabled", false);
    }


    protected boolean isSessionSerializationCheckEnabled(HttpServletRequest httpRequest)
    {
        Boolean enabledHelper = this.enabled;
        if(enabledHelper == null)
        {
            synchronized(this)
            {
                enabledHelper = this.enabled;
                if(enabledHelper == null)
                {
                    enabledHelper = this.enabled = isSessionSerializationEnabled(httpRequest);
                }
            }
        }
        return enabledHelper.booleanValue();
    }


    protected Boolean isSessionSerializationEnabled(HttpServletRequest request)
    {
        String tenantIDForWebapp = HybrisContextLoaderListener.getTenantIDForWebapp(request.getServletContext());
        ConfigIntf config = Registry.getTenantByID(tenantIDForWebapp).getConfig();
        return Boolean.valueOf(config.getBoolean("session.serialization.check", false));
    }


    protected boolean isExtensionEnabledForSessionChecking(HttpServletRequest request)
    {
        String webroot = request.getServletContext().getContextPath();
        String tenantIDForWebapp = HybrisContextLoaderListener.getTenantIDForWebapp(request.getServletContext());
        String extensionName = Utilities.getExtensionForWebroot(webroot,
                        (tenantIDForWebapp == null) ? "master" : tenantIDForWebapp);
        ConfigIntf config = Registry.getTenantByID(tenantIDForWebapp).getConfig();
        String serializationCheckEnabledFor = config.getString("session.serialization.check.extensions", "");
        List<String> extensionsWithCheck = CSV_SPLITTER.splitToList(serializationCheckEnabledFor);
        return (extensionsWithCheck.isEmpty() || extensionsWithCheck.contains(extensionName));
    }


    protected void assureAllSessionAttributesAreSerializable(HttpServletResponse response, HttpSession httpSession)
    {
        try
        {
            Enumeration<String> attributeNames = httpSession.getAttributeNames();
            while(attributeNames.hasMoreElements())
            {
                String attributeName = attributeNames.nextElement();
                Object attribute = httpSession.getAttribute(attributeName);
                try
                {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    try
                    {
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        try
                        {
                            oos.writeObject(attribute);
                            oos.close();
                        }
                        catch(Throwable throwable)
                        {
                            try
                            {
                                oos.close();
                            }
                            catch(Throwable throwable1)
                            {
                                throwable.addSuppressed(throwable1);
                            }
                            throw throwable;
                        }
                        baos.close();
                    }
                    catch(Throwable throwable)
                    {
                        try
                        {
                            baos.close();
                        }
                        catch(Throwable throwable1)
                        {
                            throwable.addSuppressed(throwable1);
                        }
                        throw throwable;
                    }
                }
                catch(IOException e)
                {
                    LOG.error("Failed to serialize attribute: " + attributeName, e);
                    sendResponseErrorIfEnabled(response);
                }
            }
        }
        catch(IllegalStateException e)
        {
            LOG.debug("Session is no longer valid");
        }
    }


    private void sendResponseErrorIfEnabled(HttpServletResponse response)
    {
        if(Registry.getCurrentTenant().getConfig().getBoolean("session.serialization.check.response.error", false))
        {
            try
            {
                response.sendError(500);
            }
            catch(IOException e1)
            {
                throw new SystemException(e1);
            }
        }
    }


    protected void activateSession(HttpSession httpSession, HttpServletRequest request)
    {
        logRequest("Activate session:  ", httpSession);
        WebSessionFunctions.setCurrentHttpServletRequest(request);
        JaloSession jaloSession = getHttpBoundSessionIfValid(httpSession);
        if(jaloSession != null)
        {
            if(JaloSession.assureSessionNotStale(jaloSession))
            {
                jaloSession.activate();
                this.sessionService.getCurrentSession();
            }
            else
            {
                createNewJaloSession(httpSession);
            }
        }
        else
        {
            createNewJaloSession(httpSession);
        }
    }


    private void createNewJaloSession(HttpSession httpSession)
    {
        try
        {
            this.sessionService.createNewSession();
            JaloSession jaloSession = JaloSession.getCurrentSession();
            jaloSession.setHttpSessionId(httpSession.getId());
            bindJaloSession(httpSession, jaloSession);
        }
        catch(SystemException e)
        {
            String msg = "Cannot create session (reason: " + e.getMessage() + ")";
            if(LOG.isDebugEnabled())
            {
                LOG.debug(msg, (Throwable)e);
            }
            else
            {
                LOG.warn(msg);
            }
        }
    }


    protected void deactivateSession(HttpSession httpSession)
    {
        logRequest("Deactivate session:  ", httpSession);
        WebSessionFunctions.clearCurrentHttpServletRequest();
        JaloSession.deactivate();
    }


    private JaloSession getHttpBoundSessionIfValid(HttpSession httpSession)
    {
        JaloSession jaloSession = getJaloSession(httpSession);
        if(jaloSession != null)
        {
            if(jaloSession.isClosed())
            {
                bindJaloSession(httpSession, jaloSession = null);
            }
        }
        return jaloSession;
    }


    private void logRequest(String message, HttpSession httpSession)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("{} ( http: {} / js: {} )", new Object[] {message, httpSession.getId(), getJaloSessionId(httpSession)});
        }
    }


    private JaloSession getJaloSession(HttpSession httpSession)
    {
        synchronized(httpSession)
        {
            return (JaloSession)httpSession.getAttribute("jalosession");
        }
    }


    private void bindJaloSession(HttpSession httpSession, JaloSession jaloSession)
    {
        synchronized(httpSession)
        {
            httpSession.setAttribute("jalosession", jaloSession);
        }
    }


    private String getJaloSessionId(HttpSession httpSession)
    {
        try
        {
            JaloSession jaloSession = getJaloSession(httpSession);
            return (jaloSession == null) ? null : jaloSession.getSessionID();
        }
        catch(IllegalStateException exp)
        {
            return null;
        }
    }


    private void resetJaloSessionForSessionReplication(HttpSession session)
    {
        try
        {
            if(this.touchHttpSession && session != null && StringUtils.isNotEmpty(getJaloSessionId(session)))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("remove jaloSession attribute from httpSession and insert it again - required for session replication");
                }
                synchronized(session)
                {
                    Object attribute = session.getAttribute("jalosession");
                    if(attribute != null)
                    {
                        session.removeAttribute("jalosession");
                        session.setAttribute("jalosession", attribute);
                    }
                }
            }
        }
        catch(IllegalStateException e)
        {
            LOG.debug("Session is no longer valid");
        }
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public void setStaleSessionSupport(StaleSessionSupport staleSessionSupport)
    {
        this.staleSessionSupport = Objects.<StaleSessionSupport>requireNonNull(staleSessionSupport, "staleSessionSupport cannot be null.");
    }


    public StaleSessionSupport getStaleSessionSupport()
    {
        if(this.staleSessionSupport == null)
        {
            this.staleSessionSupport = (StaleSessionSupport)Registry.getCoreApplicationContext().getBean("staleSessionSupport", StaleSessionSupport.class);
        }
        return this.staleSessionSupport;
    }


    public void afterPropertiesSet() throws ServletException
    {
        super.afterPropertiesSet();
        this
                        .touchHttpSession = (Config.getBoolean("session.replication.support", false) || Config.getBoolean("spring.session.enabled", false));
    }
}
