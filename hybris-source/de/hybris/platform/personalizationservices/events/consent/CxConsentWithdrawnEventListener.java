package de.hybris.platform.personalizationservices.events.consent;

import de.hybris.platform.commerceservices.event.ConsentWithdrawnEvent;
import de.hybris.platform.commerceservices.model.consent.ConsentModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.RecalculateAction;
import de.hybris.platform.personalizationservices.action.CxActionResultService;
import de.hybris.platform.personalizationservices.segment.CxUserSegmentService;
import de.hybris.platform.personalizationservices.service.CxCatalogService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collections;
import java.util.Objects;
import org.springframework.context.ApplicationListener;

public class CxConsentWithdrawnEventListener extends CxBaseConsentEventListener implements ApplicationListener<ConsentWithdrawnEvent>
{
    private CxUserSegmentService cxUserSegmentService;
    private CxActionResultService cxActionResultService;
    private CxCatalogService cxCatalogService;
    private ModelService modelService;


    public void onApplicationEvent(ConsentWithdrawnEvent event)
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
        this.cxUserSegmentService.setUserSegments((UserModel)customer, Collections.emptyList());
        removeCxResults(customer);
        getCxRecalculationService().recalculate((UserModel)customer, Collections.singletonList(RecalculateAction.RECALCULATE));
    }


    protected void removeCxResults(CustomerModel customer)
    {
        this.cxCatalogService.getConfiguredCatalogVersions().stream()
                        .map(cv -> this.cxActionResultService.getCxResults((UserModel)customer, cv))
                        .forEach(cv -> {
                            Objects.requireNonNull(this.modelService);
                            cv.ifPresent(this.modelService::remove);
                        });
    }


    protected CxUserSegmentService getCxUserSegmentService()
    {
        return this.cxUserSegmentService;
    }


    public void setCxUserSegmentService(CxUserSegmentService cxUserSegmentService)
    {
        this.cxUserSegmentService = cxUserSegmentService;
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
