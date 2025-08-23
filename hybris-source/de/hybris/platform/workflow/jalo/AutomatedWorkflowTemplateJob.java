package de.hybris.platform.workflow.jalo;

@Deprecated(since = "ages", forRemoval = false)
public interface AutomatedWorkflowTemplateJob
{
    WorkflowDecision perform(WorkflowAction paramWorkflowAction);
}
