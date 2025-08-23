package de.hybris.platform.workflow.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.SessionContext;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class WorkflowDecisionTemplate extends GeneratedWorkflowDecisionTemplate
{
    private static final Logger LOG = Logger.getLogger(WorkflowDecisionTemplate.class.getName());


    @ForceJALO(reason = "abstract method implementation")
    public WorkflowTemplate getParentWorkflowTemplate(SessionContext ctx)
    {
        return getActionTemplate().getWorkflow();
    }
}
