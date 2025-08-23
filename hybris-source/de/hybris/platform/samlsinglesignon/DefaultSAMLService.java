package de.hybris.platform.samlsinglesignon;

import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;

public class DefaultSAMLService implements SAMLService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSAMLService.class);
    private static final String SPLIT_REGEX = ",";


    public String getUserId(Saml2AuthenticatedPrincipal authenticatedPrincipal)
    {
        String userId = "";
        String userIdAttributeKey = StringUtils.defaultIfEmpty(Config.getParameter("sso.userid.attribute.key"), null);
        String userIdAttribute = getAttributeFromAuthenticatedPrincipal(authenticatedPrincipal, userIdAttributeKey);
        if(userIdAttribute != null)
        {
            userId = userIdAttribute;
        }
        return userId;
    }


    public String getUserName(Saml2AuthenticatedPrincipal authenticatedPrincipal)
    {
        String firstName = "";
        String lastName = "";
        String firstNameAttributeKey = Config.getString("sso.firstname.attribute.key", null);
        String lastNameAttributeKey = Config.getString("sso.lastname.attribute.key", null);
        String firstNameAttribute = getAttributeFromAuthenticatedPrincipal(authenticatedPrincipal, firstNameAttributeKey);
        String lastNameAttribute = getAttributeFromAuthenticatedPrincipal(authenticatedPrincipal, lastNameAttributeKey);
        if(firstNameAttribute != null)
        {
            firstName = firstNameAttribute;
        }
        if(lastNameAttribute != null)
        {
            lastName = lastNameAttribute;
        }
        return firstName + " " + firstName;
    }


    public String getCustomAttribute(Saml2AuthenticatedPrincipal authenticatedPrincipal, String attributeName)
    {
        String customAttrValue = "";
        String customAttribute = getAttributeFromAuthenticatedPrincipal(authenticatedPrincipal, attributeName);
        if(customAttribute != null)
        {
            customAttrValue = customAttribute;
        }
        return customAttrValue;
    }


    public List<String> getCustomAttributes(Saml2AuthenticatedPrincipal authenticatedPrincipal, String attributeName)
    {
        List<String> groups = new ArrayList<>();
        List<String> samlGroups = getAttributesFromAuthenticatedPrincipal(authenticatedPrincipal, attributeName);
        if(samlGroups != null)
        {
            groups = (List<String>)samlGroups.stream().map(group -> group.split(",")).flatMap(Arrays::stream).collect(Collectors.toList());
        }
        return groups;
    }


    private String getAttributeFromAuthenticatedPrincipal(Saml2AuthenticatedPrincipal authenticatedPrincipal, String attributeKey)
    {
        return (attributeKey != null) ? (String)authenticatedPrincipal.getFirstAttribute(attributeKey) : null;
    }


    private List<String> getAttributesFromAuthenticatedPrincipal(Saml2AuthenticatedPrincipal authenticatedPrincipal, String attributeKey)
    {
        return (attributeKey != null) ? authenticatedPrincipal.getAttribute(attributeKey) : null;
    }


    public String getLanguage(Saml2AuthenticatedPrincipal authenticatedPrincipal, HttpServletRequest request, CommonI18NService commonI18NService)
    {
        String credentialLanguageResult = getLanguageFromAuthenticatedPrincipal(authenticatedPrincipal, commonI18NService);
        if(StringUtils.isNotEmpty(credentialLanguageResult))
        {
            return credentialLanguageResult;
        }
        String httpRequestLanguageResult = getLanguageFromHttpRequest(request, commonI18NService);
        if(StringUtils.isNotEmpty(httpRequestLanguageResult))
        {
            return httpRequestLanguageResult;
        }
        return getCurrentLanguage(commonI18NService);
    }


    private String getLanguageFromAuthenticatedPrincipal(Saml2AuthenticatedPrincipal authenticatedPrincipal, CommonI18NService commonI18NService)
    {
        String languageAttributeKey = Config.getString("sso.language.attribute.key", null);
        String languageAttribute = getCustomAttribute(authenticatedPrincipal, languageAttributeKey);
        LOG.debug("Getting language from SAMLCredential, given ISO Code: '{}'", languageAttribute);
        return getLanguageWhenAvailable(languageAttribute, commonI18NService);
    }


    private String getLanguageFromHttpRequest(HttpServletRequest request, CommonI18NService commonI18NService)
    {
        if(request.getHeader("accept-language") == null)
        {
            return null;
        }
        String httpRequestLanguage = request.getLocale().getLanguage();
        LOG.debug("Getting language from HttpRequest, given ISO Code: '{}'", httpRequestLanguage);
        return getLanguageWhenAvailable(httpRequestLanguage, commonI18NService);
    }


    private String getLanguageWhenAvailable(String language, CommonI18NService commonI18NService)
    {
        if(StringUtils.isEmpty(language))
        {
            return null;
        }
        Locale localeForIsoCode = commonI18NService.getLocaleForIsoCode(language);
        if(localeForIsoCode == null)
        {
            LOG.debug("No locale found for iso code: {}", language);
            return null;
        }
        String normalizedLanguage = localeForIsoCode.getLanguage();
        String languageResult = commonI18NService.getAllLanguages().stream().map(C2LItemModel::getIsocode).filter(isoCode -> isoCode.equals(normalizedLanguage)).findFirst().orElse(null);
        if(StringUtils.isEmpty(languageResult))
        {
            LOG.debug("No language installed for iso code: {}", language);
        }
        return languageResult;
    }


    private String getCurrentLanguage(CommonI18NService commonI18NService)
    {
        String languageResult = commonI18NService.getCurrentLanguage().getIsocode();
        LOG.debug("Getting fallback language : {}", languageResult);
        return languageResult;
    }
}
