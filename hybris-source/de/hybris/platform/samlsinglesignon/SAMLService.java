package de.hybris.platform.samlsinglesignon;

import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;

public interface SAMLService
{
    String getUserId(Saml2AuthenticatedPrincipal paramSaml2AuthenticatedPrincipal);


    String getUserName(Saml2AuthenticatedPrincipal paramSaml2AuthenticatedPrincipal);


    String getCustomAttribute(Saml2AuthenticatedPrincipal paramSaml2AuthenticatedPrincipal, String paramString);


    List<String> getCustomAttributes(Saml2AuthenticatedPrincipal paramSaml2AuthenticatedPrincipal, String paramString);


    String getLanguage(Saml2AuthenticatedPrincipal paramSaml2AuthenticatedPrincipal, HttpServletRequest paramHttpServletRequest, CommonI18NService paramCommonI18NService);
}
