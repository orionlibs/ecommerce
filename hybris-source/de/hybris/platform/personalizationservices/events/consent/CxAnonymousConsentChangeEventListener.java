package de.hybris.platform.personalizationservices.events.consent;

import de.hybris.platform.commerceservices.event.AnonymousConsentChangeEvent;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.RecalculateAction;
import de.hybris.platform.personalizationservices.action.CxActionResultService;
import de.hybris.platform.personalizationservices.service.CxCatalogService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.springframework.context.ApplicationListener;

public class CxAnonymousConsentChangeEventListener extends CxBaseConsentEventListener implements ApplicationListener<AnonymousConsentChangeEvent>
{
    private UserService userService;
    private SessionService sessionService;
    private CxActionResultService cxActionResultService;
    private CxCatalogService cxCatalogService;
    private ModelService modelService;


    public void onApplicationEvent(AnonymousConsentChangeEvent event)
    {
        if(event == null)
        {
            return;
        }
        populateSession(event);
        if(!isConsentTemplateValid(event.getConsentTemplateCode()))
        {
            return;
        }
        if(getCxConsentService().userHasActiveConsent((UserModel)getUserService().getAnonymousUser()))
        {
            Set<String> consentGivenActions = getCxConfigurationService().getConsentGivenActions();
            List<RecalculateAction> actions = getActions(consentGivenActions);
            if(actions.contains(RecalculateAction.IGNORE))
            {
                return;
            }
            getCxRecalculationService().recalculate(actions);
        }
        else if("GIVEN".equals(event.getOldConsentState()) && (event
                        .getCurrentConsentState() == null ||
                        !"GIVEN".equals(event.getCurrentConsentState())))
        {
            removeCxResultsForAnonymousUser();
            getCxRecalculationService().recalculate((UserModel)getUserService().getAnonymousUser(),
                            Collections.singletonList(RecalculateAction.RECALCULATE));
        }
    }


    protected void populateSession(AnonymousConsentChangeEvent event)
    {
        if(event.getAdditionalData() != null)
        {
            getConsumers().forEach(c -> c.process(event.getAdditionalData()));
        }
        Map<String, String> consents = new HashMap<>();
        consents.putAll(event.getOtherConsents());
        consents.put(event.getConsentTemplateCode(), event.getCurrentConsentState());
        getSessionService().getOrLoadAttribute("user-consents", () -> consents);
    }


    protected void removeCxResultsForAnonymousUser()
    {
        this.cxCatalogService.getConfiguredCatalogVersions().stream()
                        .map(cv -> this.cxActionResultService.getCxResults((UserModel)getUserService().getAnonymousUser(), cv))
                        .forEach(cv -> {
                            Objects.requireNonNull(this.modelService);
                            cv.ifPresent(this.modelService::remove);
                        });
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    protected CxActionResultService getCxActionResultService()
    {
        return this.cxActionResultService;
    }


    public void setCxActionResultService(CxActionResultService cxActionResultService)
    {
        this.cxActionResultService = cxActionResultService;
    }


    protected CxCatalogService getCxCatalogService()
    {
        return this.cxCatalogService;
    }


    public void setCxCatalogService(CxCatalogService cxCatalogService)
    {
        this.cxCatalogService = cxCatalogService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
