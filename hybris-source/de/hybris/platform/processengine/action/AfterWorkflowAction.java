package de.hybris.platform.processengine.action;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.helpers.ProcessParameterHelper;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.jalo.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.jalo.WorkflowAction;
import de.hybris.platform.workflow.jalo.WorkflowDecision;
import java.util.Iterator;

public class AfterWorkflowAction implements AutomatedWorkflowTemplateJob
{
    public WorkflowDecision perform(WorkflowAction action)
    {
        ModelService modelService = (ModelService)Registry.getApplicationContext().getBean("modelService");
        ProcessParameterHelper processParameterHelper = (ProcessParameterHelper)Registry.getApplicationContext().getBean("processParameterHelper");
        BusinessProcessService businessProcessService = (BusinessProcessService)Registry.getApplicationContext().getBean("businessProcessService");
        for(Item it : action.getAttachmentItems())
        {
            if(it instanceof de.hybris.platform.processengine.jalo.BusinessProcess)
            {
                BusinessProcessModel process = (BusinessProcessModel)modelService.toModelLayer(it);
                String eventName = (String)processParameterHelper.getProcessParameterByName(process, "EVENT_AFTER_WORKFLOW_PARAM").getValue();
                if(eventName != null)
                {
                    businessProcessService.triggerEvent(process.getCode() + "_" + process.getCode());
                }
            }
        }
        Iterator<WorkflowDecision> iterator = action.getDecisions().iterator();
        if(iterator.hasNext())
        {
            WorkflowDecision decision = iterator.next();
            return decision;
        }
        return null;
    }
}
