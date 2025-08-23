package de.hybris.platform.personalizationservices.process;

import de.hybris.platform.personalizationservices.CxCalculationContext;
import de.hybris.platform.personalizationservices.model.process.CxPersonalizationProcessModel;
import de.hybris.platform.personalizationservices.process.strategies.CxProcessParameterType;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class UpdateUserSegmentsAction extends AbstractSimpleDecisionAction<CxPersonalizationProcessModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(UpdateUserSegmentsAction.class);
    private CxSegmentService cxSegmentService;
    private CxProcessService cxProcessService;


    public AbstractSimpleDecisionAction.Transition executeAction(CxPersonalizationProcessModel process) throws Exception
    {
        if(!(process.getUser() instanceof de.hybris.platform.core.model.user.CustomerModel))
        {
            LOG.error("personalization should only be calculated for customers");
            return AbstractSimpleDecisionAction.Transition.NOK;
        }
        this.cxProcessService.loadAllParametersFromProcess(process);
        updateUserSegments(process);
        this.cxProcessService.storeParametersForProcess(process, new CxProcessParameterType[] {CxProcessParameterType.SEGMENTATION});
        return AbstractSimpleDecisionAction.Transition.OK;
    }


    protected void updateUserSegments(CxPersonalizationProcessModel process)
    {
        if(this.processParameterHelper.containsParameter((BusinessProcessModel)process, "calcContextProcessParameter"))
        {
            this.cxSegmentService.updateUserSegments(process.getUser(), getCalculationContext(process));
        }
        else
        {
            this.cxSegmentService.updateUserSegments(process.getUser());
        }
    }


    protected CxCalculationContext getCalculationContext(CxPersonalizationProcessModel process)
    {
        return (CxCalculationContext)getProcessParameterHelper()
                        .getProcessParameterByName((BusinessProcessModel)process, "calcContextProcessParameter").getValue();
    }


    @Required
    public void setCxSegmentService(CxSegmentService cxSegmentService)
    {
        this.cxSegmentService = cxSegmentService;
    }


    @Required
    public void setCxProcessService(CxProcessService cxProcessService)
    {
        this.cxProcessService = cxProcessService;
    }


    protected CxSegmentService getCxSegmentService()
    {
        return this.cxSegmentService;
    }


    protected CxProcessService getCxProcessService()
    {
        return this.cxProcessService;
    }
}
