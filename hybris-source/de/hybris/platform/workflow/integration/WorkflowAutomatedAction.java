package de.hybris.platform.workflow.integration;

import de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import java.util.Iterator;
import org.apache.log4j.Logger;

public class WorkflowAutomatedAction implements AutomatedWorkflowTemplateJob
{
    private static final Logger LOG = Logger.getLogger(WorkflowAutomatedAction.class.getName());


    public WorkflowDecisionModel perform(WorkflowActionModel action)
    {
        LOG.info("This will complete the action automatically without any user interaction");
        Iterator<WorkflowDecisionModel> iterator = action.getDecisions().iterator();
        if(iterator.hasNext())
        {
            WorkflowDecisionModel decision = iterator.next();
            return decision;
        }
        return null;
    }
}
