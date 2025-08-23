package de.hybris.platform.cockpit.security;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.user.CookieBasedLoginToken;
import de.hybris.platform.jalo.user.LoginToken;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.security.EJBPasswordEncoderNotFoundException;
import de.hybris.platform.spring.security.CoreRememberMeService;
import de.hybris.platform.spring.security.CoreUserDetails;
import de.hybris.platform.util.Config;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;

@Deprecated
public class CockpitRememberMeService extends CoreRememberMeService
{
    private static final Logger log = LoggerFactory.getLogger(CockpitRememberMeService.class.getName());
    public static final String SSO_COOKIE = "sso_cookie__";
    public static final String SSO_COOKIE_PATTERN = "sso.login.";


    public final void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication)
    {
        if(StringUtils.isNotEmpty(request.getContextPath()))
        {
            String ssoParam = "sso.login." + request.getContextPath().substring(1);
            if(Config.getBoolean(ssoParam, false))
            {
                try
                {
                    UserManager.getInstance().storeLoginTokenCookie("sso_cookie__", successfulAuthentication.getName(),
                                    JaloSession.getCurrentSession().getSessionContext().getLanguage().getIsoCode(), (String)successfulAuthentication
                                                    .getCredentials(), "/", null, isSecure(), getTtl(), response);
                }
                catch(EJBPasswordEncoderNotFoundException e)
                {
                    log.error(e.getMessage());
                }
            }
        }
        super.loginSuccess(request, response, successfulAuthentication);
    }


    public UserDetails processAutoLoginCookie(LoginToken token, HttpServletRequest request, HttpServletResponse response)
    {
        if(token == null)
        {
            throw new InvalidCookieException("");
        }
        UserDetails userDetails = null;
        userDetails = lookupUserDetailsService().loadUserByUsername(token.getUser().getLogin());
        JaloSession.getCurrentSession().setUser(token.getUser());
        if(userDetails instanceof CoreUserDetails)
        {
            JaloSession.getCurrentSession().getSessionContext()
                            .setLanguage(C2LManager.getInstance().getLanguageByIsoCode(((CoreUserDetails)userDetails).getLanguageISO()));
        }
        return userDetails;
    }


    public Authentication autoLogin(HttpServletRequest request, HttpServletResponse response)
    {
        if(!Registry.hasCurrentTenant() || !Registry.getCurrentTenant().getJaloConnection().isSystemInitialized())
        {
            return null;
        }
        UserDetails user = null;
        LoginToken token = null;
        try
        {
            token = getLoginToken(request);
            if(token == null)
            {
                return null;
            }
            user = processAutoLoginCookie(token, request, response);
            this.userDetailsChecker.check(user);
        }
        catch(CookieTheftException cte)
        {
            cancelCookie(request, response);
            throw cte;
        }
        catch(UsernameNotFoundException noUser)
        {
            cancelCookie(request, response);
            return null;
        }
        catch(InvalidCookieException invalidCookie)
        {
            cancelCookie(request, response);
            return null;
        }
        catch(AccountStatusException statusInvalid)
        {
            cancelCookie(request, response);
            return null;
        }
        catch(RememberMeAuthenticationException e)
        {
            cancelCookie(request, response);
            return null;
        }
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, token.getPassword(), user.getAuthorities());
        auth.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return (Authentication)auth;
    }


    private Cookie extractSSOCookie(HttpServletRequest request)
    {
        if(StringUtils.isNotEmpty(request.getContextPath()))
        {
            String ssoParam = "sso.login." + request.getContextPath().substring(1);
            if(!Config.getBoolean(ssoParam, false))
            {
                return null;
            }
        }
        Cookie[] cookies = request.getCookies();
        if(cookies == null || cookies.length == 0)
        {
            return null;
        }
        for(int i = 0; i < cookies.length; i++)
        {
            if("sso_cookie__".equals(cookies[i].getName()))
            {
                return cookies[i];
            }
        }
        return null;
    }


    private LoginToken getLoginToken(HttpServletRequest request)
    {
        Cookie ssoCookie = extractSSOCookie(request);
        LoginToken token = null;
        if(ssoCookie != null)
        {
            CookieBasedLoginToken cookieBasedLoginToken = new CookieBasedLoginToken(ssoCookie);
        }
        else
        {
            token = UserManager.getInstance().getLoginToken(request);
        }
        return token;
    }


    protected void cancelCookie(HttpServletRequest request, HttpServletResponse response)
    {
        super.cancelCookie(request, response);
        Cookie cookie = new Cookie("sso_cookie__", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
