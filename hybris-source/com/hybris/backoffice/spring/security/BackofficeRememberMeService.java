package com.hybris.backoffice.spring.security;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.jalo.user.LoginToken;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.spring.security.CoreRememberMeService;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.userdetails.UserDetails;

public class BackofficeRememberMeService extends CoreRememberMeService
{
    protected static final String ORG_ZKOSS_WEB_PREFERRED_LOCALE = "org.zkoss.web.preferred.locale";
    private CommonI18NService commonI18NService;


    public UserDetails processAutoLoginCookie(LoginToken token, HttpServletRequest request, HttpServletResponse response)
    {
        UserDetails userDetails = super.processAutoLoginCookie(token, request, response);
        LanguageModel currentLanguage = this.commonI18NService.getCurrentLanguage();
        Locale locale = Locale.forLanguageTag(currentLanguage.getIsocode());
        request.getSession().setAttribute("org.zkoss.web.preferred.locale", locale);
        return userDetails;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }
}
