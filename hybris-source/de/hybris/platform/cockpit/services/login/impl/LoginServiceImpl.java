package de.hybris.platform.cockpit.services.login.impl;

import de.hybris.platform.cockpit.model.login.UserSessionSettings;
import de.hybris.platform.cockpit.services.impl.AbstractServiceImpl;
import de.hybris.platform.cockpit.services.login.LoginService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.user.LoginToken;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.security.EJBPasswordEncoderNotFoundException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.WebSessionFunctions;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class LoginServiceImpl extends AbstractServiceImpl implements LoginService
{
    private static final Logger LOG = LoggerFactory.getLogger(LoginServiceImpl.class.getName());
    private I18NService i18nService;
    private CommonI18NService commonI18NService;
    private UserService userService;
    private String defaultUserPropertyKey;
    private String defaultPasswordPropertyKey;


    public String getDefaultUserPropertyKey()
    {
        return this.defaultUserPropertyKey;
    }


    public void setDefaultUserPropertyKey(String defaultUserPropertyKey)
    {
        this.defaultUserPropertyKey = defaultUserPropertyKey;
    }


    public String getDefaultPasswordPropertyKey()
    {
        return this.defaultPasswordPropertyKey;
    }


    public void setDefaultPasswordPropertyKey(String defaultPasswordPropertyKey)
    {
        this.defaultPasswordPropertyKey = defaultPasswordPropertyKey;
    }


    public void setSessionLanguage(LanguageModel lang)
    {
        getI18nService().setCurrentLocale(getCommonI18NService().getLocaleForLanguage(lang));
    }


    public UserSessionSettings getCurrentSessionSettings()
    {
        return new UserSessionSettings(getUserService().getCurrentUser(), getI18nService().getCurrentLocale(), getI18nService()
                        .getCurrentTimeZone());
    }


    public UserSessionSettings doLogin(String login, String password, LanguageModel selectedLanguage)
    {
        return doLogin(login, password, (selectedLanguage == null) ? null : selectedLanguage.getIsocode());
    }


    public UserSessionSettings doLogin(LoginToken token)
    {
        LanguageModel selectedLanguageModel = (LanguageModel)getModelService().get(token.getLanguage());
        try
        {
            JaloSession session = JaloSession.getCurrentSession();
            Map<Object, Object> props = new HashMap<>();
            props.put("user.principal", null);
            props.put("login.token.url.parameter", token);
            session.transfer(props, (selectedLanguageModel == null));
            if(selectedLanguageModel != null)
            {
                getI18nService().setCurrentLocale(getCommonI18NService().getLocaleForLanguage(selectedLanguageModel));
            }
            return new UserSessionSettings(getUserService().getCurrentUser(), getI18nService().getCurrentLocale(), getI18nService()
                            .getCurrentTimeZone());
        }
        catch(JaloSecurityException e)
        {
            LOG.error("SECURITY EXCEPTION: [ " + e.getMessage() + " ]");
            return null;
        }
    }


    public UserSessionSettings doLogin(String login, String password, String iso)
    {
        Language selectedLanguage = (iso == null) ? null : C2LManager.getInstance().getLanguageByIsoCode(iso);
        try
        {
            JaloSession session = JaloSession.getCurrentSession();
            Map<Object, Object> props = new HashMap<>();
            props.put("user.principal", login);
            props.put("user.credentials", password);
            session.transfer(props, (selectedLanguage == null));
            if(selectedLanguage != null)
            {
                session.getSessionContext().setLanguage(selectedLanguage);
            }
            User user = session.getUser();
            return new UserSessionSettings((UserModel)this.modelService.get(user), session.getSessionContext().getLocale(), session
                            .getSessionContext().getTimeZone());
        }
        catch(JaloSecurityException e)
        {
            LOG.error("SECURITY EXCEPTION: [ " + e.getMessage() + " ]");
            return null;
        }
    }


    public void doLogout(HttpServletRequest request, HttpServletResponse response)
    {
        deleteLoginTokenCookie(request, response);
        WebSessionFunctions.invalidateSession(request.getSession());
    }


    protected UserSessionSettings verifyLoginToken(LoginToken token)
    {
        return (token == null) ? null : doLogin(token);
    }


    public void storeLoginTokenCookie(String uid, String language, String passwd, HttpServletResponse response)
    {
        try
        {
            UserManager.getInstance().storeLoginTokenCookie(uid, language, passwd, response);
        }
        catch(EJBPasswordEncoderNotFoundException e)
        {
            LOG.error(e.getMessage());
        }
    }


    public void deleteLoginTokenCookie(HttpServletRequest request, HttpServletResponse response)
    {
        deletCookies(request, response);
    }


    private void deletCookies(HttpServletRequest request, HttpServletResponse response)
    {
        if(request == null || response == null)
        {
            return;
        }
        Cookie[] cookies = request.getCookies();
        if(cookies != null)
        {
            for(int i = 0; i < cookies.length; i++)
            {
                if(cookies[i] != null && (cookies[i]
                                .getName().equals("JSESSIONID") || cookies[i]
                                .getName().equals(Config.getParameter("login.token.name")) || cookies[i]
                                .getName().equals("sso_cookie__")))
                {
                    cookies[i].setMaxAge(0);
                    if(cookies[i].getName().equals("sso_cookie__"))
                    {
                        cookies[i].setPath("/");
                    }
                    else
                    {
                        cookies[i].setPath(getContextPath(request));
                        if(Config.getParameter("login.token.domain") != null &&
                                        Config.getParameter("login.token.domain").trim().length() > 0)
                        {
                            cookies[i].setDomain(Config.getParameter("login.token.domain"));
                        }
                    }
                    cookies[i].setSecure(Boolean.parseBoolean(Config.getParameter("login.token.secure")));
                    response.addCookie(cookies[i]);
                }
            }
        }
    }


    protected String getContextPath(HttpServletRequest request)
    {
        return request.getContextPath();
    }


    public UserSessionSettings verifyLoginToken(HttpServletRequest request)
    {
        return verifyLoginToken(UserManager.getInstance().getLoginToken(request));
    }


    public Locale getLocale(LanguageModel lang)
    {
        if(lang == null)
        {
            throw new IllegalArgumentException("Language model can not be null.");
        }
        return ((Language)this.modelService.getSource(lang)).getLocale();
    }


    public String getDefaultUsername()
    {
        return "";
    }


    public String getDefaultUserPassword()
    {
        return "";
    }


    protected I18NService getI18nService()
    {
        return this.i18nService;
    }


    @Required
    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
