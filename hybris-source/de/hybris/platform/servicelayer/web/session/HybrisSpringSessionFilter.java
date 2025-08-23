package de.hybris.platform.servicelayer.web.session;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.serializer.Deserializer;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.MultiHttpSessionStrategy;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.web.filter.GenericFilterBean;

public class HybrisSpringSessionFilter extends GenericFilterBean
{
    private static final Logger LOG = LoggerFactory.getLogger(HybrisSpringSessionFilter.class);
    public static final String SPRING_SESSION_ENABLED_PROPERTY = "spring.session.enabled";
    private static final String SPRING_SESSION_CONFIG_PATTERN = "spring.session.%s.%s";
    private final ConcurrentHashMap<String, Filter> springSessionFilters = new ConcurrentHashMap<>();
    private boolean springSessionEnabled;
    private HybrisSpringSessionRepositoryFactory sessionRepositoryFactory;


    @PostConstruct
    public void init()
    {
        this.springSessionEnabled = Registry.getCurrentTenant().getConfig().getBoolean("spring.session.enabled", false);
        LOG.info("Hybris Spring Session filter {}", this.springSessionEnabled ? "enabled" : "disabled");
    }


    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        if(this.springSessionEnabled)
        {
            doFilterWithSessionRepository(servletRequest, servletResponse, filterChain);
        }
        else
        {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }


    protected void doFilterWithSessionRepository(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException
    {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String ext = Utilities.getExtensionNameFromRequest(request);
        replaceExistingSessionCookieIfRequired(ext, request, response);
        getOrCreateSessionRepositoryFilter(ext, request).doFilter((ServletRequest)request, servletResponse, filterChain);
    }


    protected Filter getOrCreateSessionRepositoryFilter(String extension, HttpServletRequest request)
    {
        Filter sessionRepository = this.springSessionFilters.get(extension);
        if(sessionRepository == null)
        {
            sessionRepository = createSessionRepositoryFilter(extension, request);
            Filter alreadyCreateFilter = this.springSessionFilters.putIfAbsent(extension, sessionRepository);
            if(alreadyCreateFilter != null)
            {
                sessionRepository = alreadyCreateFilter;
            }
        }
        return sessionRepository;
    }


    private void replaceExistingSessionCookieIfRequired(String extension, HttpServletRequest request, HttpServletResponse response)
    {
        Cookie[] cookies = request.getCookies();
        if(cookies == null)
        {
            return;
        }
        Optional<String> jvmRoute = tryToObtainJvmRoute(extension, request);
        if(jvmRoute.isEmpty())
        {
            return;
        }
        String cookieName = getSessionCookieNameFromConfig(extension);
        Arrays.<Cookie>stream(cookies)
                        .filter(c -> hasDifferentJvmRoute(jvmRoute.get(), cookieName, c))
                        .findFirst()
                        .ifPresent(c -> tryReplaceCurrentSessionCookie(extension, response, request.isSecure(), c, jvmRoute.get()));
    }


    private boolean hasDifferentJvmRoute(String jvmRoute, String cookieName, Cookie c)
    {
        return (cookieName.equals(c.getName()) && c.getValue() != null && !c.getValue().endsWith(jvmRoute));
    }


    private void tryReplaceCurrentSessionCookie(String extension, HttpServletResponse response, boolean requestIsSecure, Cookie currentCookie, String jvmRoute)
    {
        String[] parts = StringUtils.split(currentCookie.getValue(), '.');
        if(parts.length != 2)
        {
            return;
        }
        String sessionId = parts[0];
        Cookie cookie = createNewSessionCookie(extension, currentCookie, requestIsSecure, jvmRoute, sessionId);
        response.addCookie(cookie);
    }


    private Cookie createNewSessionCookie(String extension, Cookie currentCookie, boolean requestIsSecure, String jvmRoute, String sessionId)
    {
        Cookie cookie = new Cookie(currentCookie.getName(), sessionId + "." + sessionId);
        cookie.setPath(getCookiePathFromConfig(extension));
        Optional<Boolean> httpOnlyFromConfig = getCookieHttpOnlyFromConfig(extension);
        cookie.setHttpOnly(((Boolean)httpOnlyFromConfig.orElse(Boolean.valueOf(true))).booleanValue());
        Optional<Boolean> secureFromConfig = getCookieSecureFromConfig(extension);
        cookie.setSecure(((Boolean)secureFromConfig.orElse(Boolean.valueOf(requestIsSecure))).booleanValue());
        return cookie;
    }


    protected Filter createSessionRepositoryFilter(String extension, HttpServletRequest request)
    {
        String webroot = request.getServletContext().getContextPath();
        SessionRepository sessionRepository = getSessionRepository(extension, webroot);
        if(sessionRepository == null)
        {
            return (Filter)new NoSessionRepositoryFilter();
        }
        SessionRepositoryFilter sessionRepositoryFilter = new SessionRepositoryFilter(sessionRepository);
        sessionRepositoryFilter.setHttpSessionStrategy(createHttpSessionStrategy(extension, request));
        return (Filter)sessionRepositoryFilter;
    }


    private SessionRepository getSessionRepository(String extension, String webroot)
    {
        HybrisDeserializer deserializer = new HybrisDeserializer(Thread.currentThread().getContextClassLoader());
        return this.sessionRepositoryFactory.createRepository((Deserializer)deserializer, extension, webroot);
    }


    protected MultiHttpSessionStrategy createHttpSessionStrategy(String extension, HttpServletRequest request)
    {
        CookieHttpSessionStrategy httpSessionStrategy = new CookieHttpSessionStrategy();
        CookieSerializer cookieSerializer = createCookieSerializer(extension, request);
        httpSessionStrategy.setCookieSerializer(cookieSerializer);
        return (MultiHttpSessionStrategy)httpSessionStrategy;
    }


    protected CookieSerializer createCookieSerializer(String extension, HttpServletRequest request)
    {
        String cookieName = getSessionCookieNameFromConfig(extension);
        String cookiePath = getCookiePathFromConfig(extension);
        ClusterAwareCookieSerializer cookieSerializer = new ClusterAwareCookieSerializer();
        cookieSerializer.setCookieName(cookieName);
        if(StringUtils.isNotBlank(cookiePath))
        {
            cookieSerializer.setCookiePath(cookiePath);
        }
        Optional<Boolean> httpOnly = getCookieHttpOnlyFromConfig(extension);
        Objects.requireNonNull(cookieSerializer);
        httpOnly.ifPresent(cookieSerializer::setUseHttpOnly);
        Optional<Boolean> secure = getCookieSecureFromConfig(extension);
        Objects.requireNonNull(cookieSerializer);
        secure.ifPresent(cookieSerializer::setUseSecureCookie);
        Optional<String> jvmRoute = tryToObtainJvmRoute(extension, request);
        Objects.requireNonNull(cookieSerializer);
        jvmRoute.ifPresent(cookieSerializer::setJvmRoute);
        return (CookieSerializer)cookieSerializer;
    }


    private Optional<String> tryToObtainJvmRoute(String extension, HttpServletRequest request)
    {
        String jvmRoute = System.getenv("JVM_ROUTE");
        if(StringUtils.isNotBlank(jvmRoute))
        {
            return Optional.of(jvmRoute);
        }
        jvmRoute = getJvmRouteFromConfig(extension);
        if(StringUtils.isNotBlank(jvmRoute))
        {
            return Optional.of(jvmRoute);
        }
        return Optional.ofNullable((String)request.getServletContext().getAttribute("de.hybris.tomcat.jvmRoute"));
    }


    @Required
    public void setSessionRepositoryFactory(HybrisSpringSessionRepositoryFactory sessionRepositoryFactory)
    {
        this.sessionRepositoryFactory = sessionRepositoryFactory;
    }


    private String getSessionCookieNameFromConfig(String extension)
    {
        return getValueFromConfig(String.format("spring.session.%s.%s", new Object[] {extension, "cookie.name"}));
    }


    private String getCookiePathFromConfig(String extension)
    {
        return getValueFromConfig(String.format("spring.session.%s.%s", new Object[] {extension, "cookie.path"}));
    }


    private String getJvmRouteFromConfig(String extension)
    {
        return getValueFromConfig(String.format("spring.session.%s.%s", new Object[] {extension, "jvm.route"}));
    }


    private Optional<Boolean> getCookieHttpOnlyFromConfig(String extension)
    {
        String httpOnly = getValueFromConfig(String.format("spring.session.%s.%s", new Object[] {extension, "cookie.httponly"}));
        return getDeclaredBoolean(httpOnly);
    }


    private Optional<Boolean> getCookieSecureFromConfig(String extension)
    {
        String secure = getValueFromConfig(String.format("spring.session.%s.%s", new Object[] {extension, "cookie.secure"}));
        return getDeclaredBoolean(secure);
    }


    private Optional<Boolean> getDeclaredBoolean(String value)
    {
        if("true".equalsIgnoreCase(value))
        {
            return Optional.of(Boolean.TRUE);
        }
        if("false".equalsIgnoreCase(value))
        {
            return Optional.of(Boolean.FALSE);
        }
        return Optional.empty();
    }


    private String getValueFromConfig(String key)
    {
        return Config.getString(key, "");
    }
}
