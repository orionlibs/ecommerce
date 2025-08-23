package de.hybris.platform.personalizationservices.process;

import de.hybris.platform.personalizationservices.model.process.CxPersonalizationProcessModel;
import de.hybris.platform.personalizationservices.service.CxCatalogService;
import de.hybris.platform.personalizationservices.service.CxService;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CalculatePersonalizationForUserAction extends AbstractSimpleDecisionAction<CxPersonalizationProcessModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(CalculatePersonalizationForUserAction.class);
    private CxService cxService;
    private CxProcessService cxProcessService;
    private CxCatalogService cxCatalogService;


    public AbstractSimpleDecisionAction.Transition executeAction(CxPersonalizationProcessModel process) throws Exception
    {
        if(!(process.getUser() instanceof de.hybris.platform.core.model.user.CustomerModel))
        {
            LOG.error("personalization should only be calculated for customers");
            return AbstractSimpleDecisionAction.Transition.NOK;
        }
        this.cxProcessService.loadAllParametersFromProcess(process);
        Objects.requireNonNull(this.cxCatalogService);
        process.getCatalogVersions().stream().filter(this.cxCatalogService::isPersonalizationInCatalog)
                        .forEach(cv -> this.cxService.calculateAndStorePersonalization(process.getUser(), cv));
        LOG.debug("personalization calculated for user {}", process.getUser().getUid());
        return AbstractSimpleDecisionAction.Transition.OK;
    }


    @Required
    public void setCxService(CxService cxService)
    {
        this.cxService = cxService;
    }


    @Required
    public void setCxProcessService(CxProcessService cxProcessService)
    {
        this.cxProcessService = cxProcessService;
    }


    @Required
    public void setCxCatalogService(CxCatalogService cxCatalogService)
    {
        this.cxCatalogService = cxCatalogService;
    }


    protected CxService getCxService()
    {
        return this.cxService;
    }


    protected CxProcessService getCxProcessService()
    {
        return this.cxProcessService;
    }


    protected CxCatalogService getCxCatalogService()
    {
        return this.cxCatalogService;
    }
}
