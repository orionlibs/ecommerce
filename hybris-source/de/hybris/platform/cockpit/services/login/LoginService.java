package de.hybris.platform.cockpit.services.login;

import de.hybris.platform.cockpit.model.login.UserSessionSettings;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginService
{
    void setSessionLanguage(LanguageModel paramLanguageModel);


    UserSessionSettings getCurrentSessionSettings();


    UserSessionSettings doLogin(String paramString1, String paramString2, LanguageModel paramLanguageModel) throws SecurityException;


    void doLogout(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);


    UserSessionSettings verifyLoginToken(HttpServletRequest paramHttpServletRequest);


    void storeLoginTokenCookie(String paramString1, String paramString2, String paramString3, HttpServletResponse paramHttpServletResponse);


    void deleteLoginTokenCookie(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);


    Locale getLocale(LanguageModel paramLanguageModel);


    @Deprecated(since = "2005")
    String getDefaultUsername();


    @Deprecated(since = "2005")
    String getDefaultUserPassword();
}
