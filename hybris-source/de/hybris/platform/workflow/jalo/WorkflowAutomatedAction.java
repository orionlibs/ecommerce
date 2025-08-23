package de.hybris.platform.workflow.jalo;

import java.util.Iterator;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class WorkflowAutomatedAction implements AutomatedWorkflowTemplateJob
{
    private static final Logger LOG = Logger.getLogger(WorkflowAutomatedAction.class.getName());


    public WorkflowDecision perform(WorkflowAction action)
    {
        LOG.info("This will complete the action automatically without any user interaction");
        Iterator<WorkflowDecision> iterator = action.getDecisions().iterator();
        if(iterator.hasNext())
        {
            WorkflowDecision decision = iterator.next();
            return decision;
        }
        return null;
    }
}
