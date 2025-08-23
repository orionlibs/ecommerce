package de.hybris.platform.workflow;

import de.hybris.platform.workflow.model.WorkflowActionCommentModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;

public interface WorkflowActionCommentService
{
    WorkflowActionCommentModel addCommentToAction(String paramString, WorkflowActionModel paramWorkflowActionModel);


    boolean isAutomatedComment(WorkflowActionCommentModel paramWorkflowActionCommentModel);
}
