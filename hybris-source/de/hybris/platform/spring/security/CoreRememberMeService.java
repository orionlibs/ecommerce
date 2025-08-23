package de.hybris.platform.spring.security;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.GeneratedC2LItem;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.user.LoginToken;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.security.EJBPasswordEncoderNotFoundException;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.util.Assert;

public class CoreRememberMeService implements RememberMeServices, InitializingBean, LogoutHandler
{
    private static final Logger log = Logger.getLogger(CoreRememberMeService.class.getName());
    public static final String DEFAULT_PARAMETER = "_spring_security_remember_me";
    private String path = null;
    private String domain = null;
    private String cookieName = null;
    private boolean secure = true;
    private int ttl = -1;
    private static final int DEFAULT_TTL_VALUE = 15000;
    private static final String LOGIN_TOKEN_AUTHENTICATION_ENABLED = "login.token.authentication.enabled";
    public static final String SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY = "SPRING_SECURITY_REMEMBER_ME_COOKIE";
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    protected final UserDetailsChecker userDetailsChecker = (UserDetailsChecker)new AccountStatusUserDetailsChecker();
    protected final AuthenticationDetailsSource authenticationDetailsSource = (AuthenticationDetailsSource)new WebAuthenticationDetailsSource();
    private String key;
    private final String parameter = "_spring_security_remember_me";
    private boolean alwaysRemember;


    public void afterPropertiesSet() throws Exception
    {
        Assert.hasLength("_spring_security_remember_me");
        Assert.hasLength(this.cookieName);
        this.ttl = (this.ttl < 0) ? Config.getInt("login.token.ttl", 15000) : this.ttl;
    }


    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) throws EJBPasswordEncoderNotFoundException
    {
        if(request.getParameter("_spring_security_remember_me") != null)
        {
            UserManager.getInstance().storeLoginTokenCookie(getCookieName(), successfulAuthentication.getName(),
                            JaloSession.getCurrentSession()
                                            .getSessionContext()
                                            .getLanguage()
                                            .getIsoCode(), (String)successfulAuthentication
                                            .getCredentials(), getPath(request),
                            getDomain(), isSecure(), getTtl(), response);
        }
    }


    public UserDetails processAutoLoginCookie(LoginToken token, HttpServletRequest request, HttpServletResponse response)
    {
        UserDetails userDetails = null;
        if(token != null && token.getUser() != null)
        {
            userDetails = lookupUserDetailsService().loadUserByUsername(token.getUser().getLogin());
            JaloSession.getCurrentSession().setUser(token.getUser());
        }
        updateLanguageInSessionContext(userDetails, token, request);
        return userDetails;
    }


    protected void updateLanguageInSessionContext(UserDetails userDetails, LoginToken token, HttpServletRequest request)
    {
        String languageIsoCode = getLanguageIsoCode(userDetails, token, request);
        Language language = C2LManager.getInstance().getLanguageByIsoCode(languageIsoCode);
        JaloSession.getCurrentSession().getSessionContext().setLanguage(language);
    }


    protected String getLanguageIsoCode(UserDetails userDetails, LoginToken token, HttpServletRequest request)
    {
        return getLanguageFromCookie(token)
                        .orElseGet(() -> (String)getLanguageFromUserDetails(userDetails).orElseGet(()));
    }


    protected Optional<String> getLanguageFromCookie(LoginToken token)
    {
        return Optional.<LoginToken>ofNullable(token)
                        .map(LoginToken::getLanguage)
                        .map(GeneratedC2LItem::getIsocode);
    }


    protected Optional<String> getLanguageFromUserDetails(UserDetails userDetails)
    {
        Objects.requireNonNull(CoreUserDetails.class);
        Objects.requireNonNull(CoreUserDetails.class);
        return Optional.<UserDetails>ofNullable(userDetails).filter(CoreUserDetails.class::isInstance).map(CoreUserDetails.class::cast)
                        .map(CoreUserDetails::getLanguageISO);
    }


    protected Optional<String> getLanguageFromHttpRequest(HttpServletRequest request)
    {
        return Optional.<Locale>ofNullable(request.getLocale())
                        .map(Locale::toLanguageTag);
    }


    protected String getDefaultLanguageForTenant()
    {
        Language language = C2LManager.getInstance().getDefaultLanguageForTenant(Registry.getCurrentTenant());
        return (language != null) ? language.getIsocode() : null;
    }


    private boolean checkIfAutoLoginIsEnabled(HttpServletRequest request)
    {
        String extension = Utilities.getExtensionNameFromRequest(request);
        String loginTokenAuthenticationPropertyNameForExt = "login.token.authentication." + extension + ".enabled";
        String loginTokenAuthenticationEnabledValue = Config.getParameter(loginTokenAuthenticationPropertyNameForExt);
        if(loginTokenAuthenticationEnabledValue != null)
        {
            return Config.getBoolean(loginTokenAuthenticationPropertyNameForExt, true);
        }
        return Config.getBoolean("login.token.authentication.enabled", true);
    }


    public Authentication autoLogin(HttpServletRequest request, HttpServletResponse response)
    {
        if(!Registry.hasCurrentTenant() || !Registry.getCurrentTenant().getJaloConnection().isSystemInitialized())
        {
            return null;
        }
        if(!checkIfAutoLoginIsEnabled(request))
        {
            return null;
        }
        UserDetails user = null;
        LoginToken token = null;
        try
        {
            token = UserManager.getInstance().getLoginToken(request);
            if(token == null)
            {
                throw new InvalidCookieException("");
            }
            validateToken(token);
            user = processAutoLoginCookie(token, request, response);
            if(user == null)
            {
                throw new InvalidCookieException("");
            }
            this.userDetailsChecker.check(user);
        }
        catch(CookieTheftException cte)
        {
            cancelCookie(request, response);
            throw cte;
        }
        catch(Exception ex)
        {
            if(log.isDebugEnabled())
            {
                log.debug(String.format("autoLogin failed: %s", new Object[] {ex.getMessage()}));
            }
            cancelCookie(request, response);
            return null;
        }
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
        auth.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return (Authentication)auth;
    }


    private void validateToken(LoginToken loginToken)
    {
        if(!loginToken.isTokenValid())
        {
            throw new InvalidCookieException("Token invalid for user: " + loginToken.getUser());
        }
    }


    public String getPath(HttpServletRequest request)
    {
        if(this.path == null || this.path.trim().length() < 1)
        {
            this.path = request.getContextPath();
        }
        if(this.path == null || this.path.isEmpty())
        {
            this.path = "/";
        }
        return this.path;
    }


    public void setPath(String path)
    {
        this.path = path;
    }


    public boolean isSecure()
    {
        return this.secure;
    }


    public void setSecure(boolean secure)
    {
        this.secure = secure;
    }


    public int getTtl()
    {
        return this.ttl;
    }


    public void setTtl(int ttl)
    {
        this.ttl = ttl;
    }


    public String getDomain()
    {
        return this.domain;
    }


    public void setDomain(String domain)
    {
        this.domain = domain;
    }


    public void setCookieName(String cookieName)
    {
        this.cookieName = cookieName;
    }


    protected String getCookieName()
    {
        return this.cookieName;
    }


    protected UserDetailsService lookupUserDetailsService()
    {
        throw new IllegalStateException("lookupUserDetailsService() must be matched by <lookup-method name=\"lookupUserDetailsService\" ref=\"...\"/>");
    }


    protected void cancelCookie(HttpServletRequest request, HttpServletResponse response)
    {
        deleteLocalLoginToken(request, response);
        UserManager.getInstance().deleteLoginTokenCookie(request, response);
    }


    private void deleteLocalLoginToken(HttpServletRequest request, HttpServletResponse response)
    {
        Cookie cookie = new Cookie(this.cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath(getPath(request));
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }


    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
    {
        cancelCookie(request, response);
    }


    public final void loginFail(HttpServletRequest request, HttpServletResponse response)
    {
        cancelCookie(request, response);
        onLoginFail(request, response);
    }


    protected boolean rememberMeRequested(HttpServletRequest request, String parameter)
    {
        if(this.alwaysRemember)
        {
            return true;
        }
        String paramValue = request.getParameter(parameter);
        return (paramValue != null && (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("on") || paramValue.equalsIgnoreCase("yes") || "1"
                        .equals(paramValue)));
    }


    protected void onLoginFail(HttpServletRequest request, HttpServletResponse response)
    {
    }


    public void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication)
    {
        if(!rememberMeRequested(request, "_spring_security_remember_me"))
        {
            return;
        }
        try
        {
            onLoginSuccess(request, response, successfulAuthentication);
        }
        catch(EJBPasswordEncoderNotFoundException e)
        {
            log.error(e.getMessage());
        }
    }


    public String getKey()
    {
        return this.key;
    }


    public void setKey(String key)
    {
        this.key = key;
    }
}
