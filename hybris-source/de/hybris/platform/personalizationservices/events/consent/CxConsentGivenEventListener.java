package de.hybris.platform.personalizationservices.events.consent;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.ConsentGivenEvent;
import de.hybris.platform.commerceservices.model.consent.ConsentModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.RecalculateAction;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import java.util.List;
import java.util.Set;
import org.springframework.context.ApplicationListener;
import org.springframework.util.CollectionUtils;

public class CxConsentGivenEventListener extends CxBaseConsentEventListener implements ApplicationListener<ConsentGivenEvent>
{
    private BaseSiteService baseSiteService;
    private UserService userService;


    public void onApplicationEvent(ConsentGivenEvent event)
    {
        if(event == null || event.getConsent() == null)
        {
            return;
        }
        ConsentModel consent = event.getConsent();
        if(!isConsentTemplateValid(consent.getConsentTemplate().getId()))
        {
            return;
        }
        CustomerModel customer = consent.getCustomer();
        if(getCxConsentService().userHasActiveConsent((UserModel)customer))
        {
            BaseSiteModel baseSite = consent.getConsentTemplate().getBaseSite();
            if(baseSite == null)
            {
                return;
            }
            Set<String> consentGivenActions = getCxConfigurationService().getConsentGivenActions(baseSite);
            List<RecalculateAction> actions = getActions(consentGivenActions);
            if(CollectionUtils.isEmpty(actions) || actions.contains(RecalculateAction.IGNORE))
            {
                return;
            }
            populateSession(customer, baseSite);
            getCxRecalculationService().recalculate((UserModel)customer, actions);
        }
    }


    private void populateSession(CustomerModel customer, BaseSiteModel baseSite)
    {
        BaseSiteModel currentBaseSite = this.baseSiteService.getCurrentBaseSite();
        if(currentBaseSite == null || !currentBaseSite.getUid().equals(baseSite.getUid()))
        {
            this.baseSiteService.setCurrentBaseSite(baseSite, true);
        }
        this.userService.setCurrentUser((UserModel)customer);
    }


    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }


    protected BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }
}
