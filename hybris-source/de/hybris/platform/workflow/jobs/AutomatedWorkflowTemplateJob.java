package de.hybris.platform.workflow.jobs;

import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;

public interface AutomatedWorkflowTemplateJob
{
    WorkflowDecisionModel perform(WorkflowActionModel paramWorkflowActionModel);
}
