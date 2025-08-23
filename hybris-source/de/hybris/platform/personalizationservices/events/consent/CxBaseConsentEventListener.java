package de.hybris.platform.personalizationservices.events.consent;

import de.hybris.platform.commerceservices.consent.AnonymousConsentChangeEventDataConsumer;
import de.hybris.platform.commerceservices.model.consent.ConsentTemplateModel;
import de.hybris.platform.personalizationservices.RecalculateAction;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.consent.CxConsentService;
import de.hybris.platform.personalizationservices.service.CxRecalculationService;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CxBaseConsentEventListener
{
    private static final Logger LOG = LoggerFactory.getLogger(CxBaseConsentEventListener.class);
    private CxConfigurationService cxConfigurationService;
    private CxRecalculationService cxRecalculationService;
    private CxConsentService cxConsentService;
    private List<AnonymousConsentChangeEventDataConsumer> consumers = Collections.emptyList();


    protected List<RecalculateAction> getActions(Set<String> actions)
    {
        return (List<RecalculateAction>)actions.stream()
                        .filter(this::actionExist)
                        .map(RecalculateAction::valueOf)
                        .collect(Collectors.toList());
    }


    protected boolean actionExist(String actionName)
    {
        try
        {
            RecalculateAction.valueOf(actionName);
        }
        catch(IllegalArgumentException e)
        {
            LOG.warn("Recalculate action doesn't exist :" + actionName, e);
            return false;
        }
        return true;
    }


    protected boolean isConsentTemplateValid(String consentTemplateId)
    {
        Set<ConsentTemplateModel> consentTemplates = getCxConfigurationService().getConsentTemplates();
        if(consentTemplates != null)
        {
            return consentTemplates.stream()
                            .map(ConsentTemplateModel::getId)
                            .anyMatch(id -> id.equals(consentTemplateId));
        }
        return false;
    }


    public void setCxConfigurationService(CxConfigurationService cxConfigurationService)
    {
        this.cxConfigurationService = cxConfigurationService;
    }


    protected CxConfigurationService getCxConfigurationService()
    {
        return this.cxConfigurationService;
    }


    public void setCxConsentService(CxConsentService cxConsentService)
    {
        this.cxConsentService = cxConsentService;
    }


    protected CxConsentService getCxConsentService()
    {
        return this.cxConsentService;
    }


    public void setCxRecalculationService(CxRecalculationService cxRecalculationService)
    {
        this.cxRecalculationService = cxRecalculationService;
    }


    protected CxRecalculationService getCxRecalculationService()
    {
        return this.cxRecalculationService;
    }


    @Autowired(required = false)
    public void setConsumers(List<AnonymousConsentChangeEventDataConsumer> consumers)
    {
        this.consumers = CollectionUtils.isNotEmpty(consumers) ? consumers : Collections.<AnonymousConsentChangeEventDataConsumer>emptyList();
    }


    protected List<AnonymousConsentChangeEventDataConsumer> getConsumers()
    {
        return this.consumers;
    }
}
