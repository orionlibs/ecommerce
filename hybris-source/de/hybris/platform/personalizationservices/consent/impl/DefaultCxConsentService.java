package de.hybris.platform.personalizationservices.consent.impl;

import de.hybris.platform.commerceservices.consent.CommerceConsentService;
import de.hybris.platform.commerceservices.model.consent.ConsentTemplateModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.consent.CxConsentService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxConsentService implements CxConsentService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCxConsentService.class);
    private BaseSiteService baseSiteService;
    private ConfigurationService configurationService;
    private CommerceConsentService commerceConsentService;
    private CxConfigurationService cxConfigurationService;
    private UserService userService;
    private SessionService sessionService;


    public boolean userHasActiveConsent()
    {
        return userHasActiveConsent(this.userService.getCurrentUser());
    }


    public boolean userHasActiveConsent(UserModel user)
    {
        ServicesUtil.validateParameterNotNull(user, "Parameter user  must not be null");
        if(!(user instanceof CustomerModel))
        {
            LOG.warn("User {} is not a customer. Unable to check consent.", user);
            return false;
        }
        if(this.userService.isAnonymousUser(user))
        {
            return hasActiveConsentAnonymousUser();
        }
        return hasActiveConsentLoggedUser((CustomerModel)user);
    }


    protected boolean hasActiveConsentLoggedUser(CustomerModel user)
    {
        Set<ConsentTemplateModel> consentTemplates = getConsentTemplatesForCurrentBaseSite();
        if(consentTemplates.isEmpty())
        {
            return getIgnoreConsentCheckWhenNoConsentTemplate();
        }
        for(ConsentTemplateModel consentTemplate : consentTemplates)
        {
            if(!this.commerceConsentService.hasEffectivelyActiveConsent(user, consentTemplate))
            {
                return false;
            }
        }
        return true;
    }


    protected boolean hasActiveConsentAnonymousUser()
    {
        Set<ConsentTemplateModel> consentTemplates = getConsentTemplatesForCurrentBaseSiteAndAnonymous();
        if(CollectionUtils.isEmpty(consentTemplates))
        {
            return getIgnoreConsentCheckWhenNoConsentTemplate();
        }
        Object attribute = this.sessionService.getAttribute("user-consents");
        if(!(attribute instanceof Map))
        {
            return false;
        }
        Map<String, String> consentMap = (Map<String, String>)attribute;
        return consentTemplates.stream()
                        .map(ConsentTemplateModel::getId)
                        .allMatch(c -> checkIfExistsAndGiven(c, consentMap));
    }


    protected boolean checkIfExistsAndGiven(String consentId, Map<String, String> consentMap)
    {
        String consentValue = consentMap.get(consentId);
        return "GIVEN".equals(consentValue);
    }


    private Set<ConsentTemplateModel> getConsentTemplatesForCurrentBaseSite()
    {
        return (Set<ConsentTemplateModel>)this.cxConfigurationService.getConsentTemplates().stream()
                        .filter(p -> p.getBaseSite().equals(this.baseSiteService.getCurrentBaseSite()))
                        .collect(Collectors.toSet());
    }


    private Set<ConsentTemplateModel> getConsentTemplatesForCurrentBaseSiteAndAnonymous()
    {
        return (Set<ConsentTemplateModel>)this.cxConfigurationService.getConsentTemplates().stream()
                        .filter(p -> p.getBaseSite().equals(this.baseSiteService.getCurrentBaseSite()))
                        .filter(ConsentTemplateModel::isExposed)
                        .collect(Collectors.toSet());
    }


    private boolean getIgnoreConsentCheckWhenNoConsentTemplate()
    {
        return this.configurationService.getConfiguration()
                        .getBoolean("personalizationservices.consent.ignoreConsentCheckWhenNoConsentTemplate", Boolean.TRUE)
                        .booleanValue();
    }


    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }


    protected BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }


    @Required
    public void setCommerceConsentService(CommerceConsentService commerceConsentService)
    {
        this.commerceConsentService = commerceConsentService;
    }


    protected CommerceConsentService getCommerceConsentService()
    {
        return this.commerceConsentService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setCxConfigurationService(CxConfigurationService cxConfigurationService)
    {
        this.cxConfigurationService = cxConfigurationService;
    }


    protected CxConfigurationService getCxConfigurationService()
    {
        return this.cxConfigurationService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }
}
