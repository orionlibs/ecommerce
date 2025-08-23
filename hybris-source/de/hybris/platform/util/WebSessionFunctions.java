package de.hybris.platform.util;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloConnectException;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.JaloSystemNotInitializedException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.user.Customer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class WebSessionFunctions
{
    private static final String STALE_REQUEST_MARKER = "de.hybris.stale.session.request.marker";
    private static final Logger LOG = Logger.getLogger(WebSessionFunctions.class.getName());
    private static final ThreadLocal<HttpServletRequest> CURRENT_HTTP_REQUEST = new ThreadLocal<>();


    public static boolean isStaleRequest(HttpServletRequest request)
    {
        Objects.requireNonNull(request, "request cannot be null.");
        return Boolean.TRUE.equals(request.getAttribute("de.hybris.stale.session.request.marker"));
    }


    public static void markRequestAsStale(HttpServletRequest request)
    {
        Objects.requireNonNull(request, "request cannot be null.");
        request.setAttribute("de.hybris.stale.session.request.marker", Boolean.TRUE);
    }


    public static HttpServletRequest getCurrentHttpServletRequest()
    {
        return CURRENT_HTTP_REQUEST.get();
    }


    public static HttpSession getCurrentHttpSession()
    {
        HttpServletRequest currentHttpServletRequest = getCurrentHttpServletRequest();
        return (currentHttpServletRequest == null) ? null : currentHttpServletRequest.getSession();
    }


    public static void clearCurrentHttpServletRequest()
    {
        CURRENT_HTTP_REQUEST.remove();
    }


    public static void setCurrentHttpServletRequest(HttpServletRequest request)
    {
        CURRENT_HTTP_REQUEST.set(request);
    }


    public static final void invalidateSession(HttpSession session)
    {
        try
        {
            JaloSession sess = (JaloSession)session.getAttribute("jalosession");
            if(sess != null)
            {
                try
                {
                    sess.close();
                }
                catch(Exception e)
                {
                    LOG.debug(e.getMessage());
                }
                finally
                {
                    session.removeAttribute("jalosession");
                }
            }
            session.invalidate();
        }
        catch(IllegalStateException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e);
            }
        }
    }


    public static final void clearSession(HttpSession session)
    {
        try
        {
            JaloSession sess = (JaloSession)session.getAttribute("jalosession");
            if(sess != null)
            {
                try
                {
                    sess.close();
                }
                catch(Exception e)
                {
                    LOG.debug(e);
                }
            }
            List<String> list = new ArrayList<>();
            for(Enumeration<String> en = session.getAttributeNames(); en.hasMoreElements(); )
            {
                list.add(en.nextElement());
            }
            for(String key : list)
            {
                session.removeAttribute(key);
            }
        }
        catch(IllegalStateException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e);
            }
        }
    }


    public static final JaloSession tryGetJaloSession(HttpSession session)
    {
        JaloSession ret = null;
        try
        {
            ret = (JaloSession)session.getAttribute("jalosession");
        }
        catch(IllegalStateException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e);
            }
        }
        if(ret != null && (!Registry.hasCurrentTenant() || !Registry.getCurrentTenant().equals(ret.getTenant())))
        {
            ret = null;
        }
        return ret;
    }


    public static final JaloSession getSession(HttpServletRequest request) throws JaloConnectException, JaloSystemNotInitializedException
    {
        return getSession(Collections.EMPTY_MAP, null, request.getSession(), request, null);
    }


    public static final JaloSession getSession(Map connProp, String cookiename, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws JaloSystemNotInitializedException
    {
        return getSession(connProp, cookiename, session, request, response, null);
    }


    public static final JaloSession getSession(Map _connProp, String cookiename, HttpSession session, HttpServletRequest request, HttpServletResponse response, Class sessionClass) throws JaloSystemNotInitializedException
    {
        return getSession(cookiename, session, request, response, sessionClass, false);
    }


    public static final JaloSession getSession(HttpServletRequest request, boolean skipSystemInitTest) throws JaloConnectException, JaloSystemNotInitializedException
    {
        return getSession(null, request.getSession(), request, null, skipSystemInitTest);
    }


    public static final JaloSession getSession(String cookiename, HttpSession session, HttpServletRequest request, HttpServletResponse response, boolean skipSystemInitTest) throws JaloSystemNotInitializedException
    {
        return getSession(cookiename, session, request, response, null, skipSystemInitTest);
    }


    public static final JaloSession getSession(String cookiename, HttpSession session, HttpServletRequest request, HttpServletResponse response, Class sessionClass, boolean skipSystemInitTest) throws JaloSystemNotInitializedException
    {
        JaloSession js = tryGetJaloSession(session);
        if(js != null && !js.isClosed() && JaloSession.assureSessionNotStale(js))
        {
            js.activate();
            js.setHttpSessionId(session.getId());
        }
        else
        {
            JaloSession.deactivate();
            String customerPKStr = null;
            if(cookiename != null)
            {
                LOG.debug("using cookies.");
                Cookie[] cookies = request.getCookies();
                if(cookies != null && cookies.length > 0)
                {
                    for(int i = 0; i < cookies.length; i++)
                    {
                        if(cookies[i].getName().equals(cookiename))
                        {
                            customerPKStr = cookies[i].getValue();
                            LOG.debug("cookiestring found:" + customerPKStr);
                            break;
                        }
                    }
                }
                if(customerPKStr != null)
                {
                    LOG.debug("found CustomerPK in cookie '" + cookiename + "': " + customerPKStr);
                    Customer knownCustomer = null;
                    try
                    {
                        JaloConnection jctmp = JaloConnection.getInstance();
                        JaloSession jstmp = createAnonymousCustomerSession(jctmp, sessionClass);
                        knownCustomer = (Customer)jstmp.getItem(PK.parse(customerPKStr));
                        session.setAttribute("blnCookieFound", Boolean.TRUE);
                        Map<Object, Object> loginProp = new HashMap<>();
                        loginProp.put("user.principal", knownCustomer.getLogin());
                        loginProp.put("user.pk", knownCustomer.getPK());
                        loginProp.put("session.type", "customer");
                        JaloConnection jc = JaloConnection.getInstance();
                        js = jc.createSession(loginProp);
                    }
                    catch(IllegalArgumentException e)
                    {
                        LOG.warn("wrong customer pk (" + customerPKStr + ") in cookie '" + cookiename + "' : " + e);
                    }
                    catch(JaloItemNotFoundException e)
                    {
                        LOG.warn("customer with pk (" + customerPKStr + ") not found in cookie '" + cookiename + "': " + e);
                    }
                    catch(JaloSecurityException e)
                    {
                        if(knownCustomer != null)
                        {
                            LOG.warn("customer '" + knownCustomer.getLogin() + "' found in cookie '" + cookiename + "' not allowed to login: " + e);
                        }
                        else
                        {
                            LOG.warn("customer found in cookie '" + cookiename + "' not allowed to login: " + e);
                        }
                    }
                    catch(NullPointerException e)
                    {
                        LOG.warn("getSession error : " + e);
                        e.printStackTrace();
                        Cookie cookie = new Cookie(cookiename, "none");
                        cookie.setValue("");
                        cookie.setMaxAge(0);
                        cookie.setHttpOnly(true);
                        response.addCookie(cookie);
                    }
                    catch(StringIndexOutOfBoundsException e)
                    {
                        LOG.warn("getSession error : " + e);
                        e.printStackTrace();
                        Cookie cookie = new Cookie(cookiename, "none");
                        cookie.setValue("");
                        cookie.setMaxAge(0);
                        cookie.setHttpOnly(true);
                        response.addCookie(cookie);
                    }
                    catch(de.hybris.platform.core.PK.PKException e)
                    {
                        LOG.warn("PKException: Invalid PK format '" + customerPKStr + "': " + e);
                        Cookie cookie = new Cookie(cookiename, "none");
                        cookie.setValue("");
                        cookie.setMaxAge(0);
                        cookie.setHttpOnly(true);
                        response.addCookie(cookie);
                    }
                }
            }
            if(js == null || js.isClosed())
            {
                if(js != null)
                {
                    LOG.info("Session closed. Anonymous session created");
                }
                try
                {
                    JaloConnection jc = JaloConnection.getInstance();
                    if(!skipSystemInitTest && !jc.isSystemInitialized())
                    {
                        throw new JaloSystemNotInitializedException(null, "system not initialized", 0);
                    }
                    js = createAnonymousCustomerSession(jc, sessionClass);
                }
                catch(JaloSecurityException e)
                {
                    LOG.error("error creating anonymous session: " + e);
                    throw new JaloSystemException(e);
                }
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("JaloSession created. customer: " + js.getUser().getPK().getLongValueAsString());
            }
            session.setAttribute("jalosession", js);
            js.setHttpSessionId(session.getId());
        }
        return js;
    }


    private static JaloSession createAnonymousCustomerSession(JaloConnection jctmp, Class sessionClass) throws JaloSecurityException
    {
        return (sessionClass == null) ? jctmp.createAnonymousCustomerSession() : jctmp.createAnonymousCustomerSession(sessionClass);
    }
}
