package de.hybris.platform.servicelayer.web.session;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class ClusterAwareCookieSerializer implements CookieSerializer
{
    private static final Logger LOG = LoggerFactory.getLogger(ClusterAwareCookieSerializer.class);
    private final DefaultCookieSerializer target = new DefaultCookieSerializer();
    private String cookieName;
    private String jvmRoute;


    public ClusterAwareCookieSerializer()
    {
        this.target.setUseBase64Encoding(false);
    }


    public void writeCookieValue(CookieSerializer.CookieValue cookieValue)
    {
        this.target.writeCookieValue(cookieValue);
    }


    public List<String> readCookieValues(HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();
        List<String> matchingCookieValues = new ArrayList<>();
        if(cookies != null)
        {
            for(Cookie cookie : cookies)
            {
                if(this.cookieName.equals(cookie.getName()))
                {
                    String sessionId = cookie.getValue();
                    if(sessionId != null)
                    {
                        if(this.jvmRoute != null)
                        {
                            String[] parts = StringUtils.split(sessionId, '.');
                            if(parts.length == 2)
                            {
                                sessionId = parts[0];
                                if(!this.jvmRoute.equals(parts[1]))
                                {
                                    LOG.info("Received request for node: '{}'. Serving node: '{}'", parts[1], this.jvmRoute);
                                }
                            }
                        }
                        matchingCookieValues.add(sessionId);
                    }
                }
            }
        }
        return matchingCookieValues;
    }


    public void setCookieName(String cookieName)
    {
        this.target.setCookieName(cookieName);
        this.cookieName = cookieName;
    }


    public void setCookiePath(String cookiePath)
    {
        this.target.setCookiePath(cookiePath);
    }


    public void setJvmRoute(String jvmRoute)
    {
        this.target.setJvmRoute(jvmRoute);
        this.jvmRoute = jvmRoute;
    }


    public void setUseHttpOnly(boolean useHttpOnly)
    {
        this.target.setUseHttpOnlyCookie(useHttpOnly);
    }


    public void setUseSecureCookie(boolean useSecureCookie)
    {
        this.target.setUseSecureCookie(useSecureCookie);
    }
}
