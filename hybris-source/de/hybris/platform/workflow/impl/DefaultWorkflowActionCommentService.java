package de.hybris.platform.workflow.impl;

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.WorkflowActionCommentService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.exceptions.WorkflowTerminatedException;
import de.hybris.platform.workflow.model.WorkflowActionCommentModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import java.util.ArrayList;
import java.util.List;

public class DefaultWorkflowActionCommentService implements WorkflowActionCommentService
{
    private WorkflowService workflowService;
    private ModelService modelService;
    private UserService userService;


    public boolean isAutomatedComment(WorkflowActionCommentModel comment)
    {
        return (comment.getUser() == null);
    }


    public WorkflowActionCommentModel addCommentToAction(String commentValue, WorkflowActionModel action)
    {
        if(this.workflowService.isTerminated(action.getWorkflow()))
        {
            throw new WorkflowTerminatedException("The workflow " + action.getWorkflow().getName() + " is terminated.");
        }
        WorkflowActionCommentModel comment = (WorkflowActionCommentModel)this.modelService.create(WorkflowActionCommentModel.class);
        comment.setUser(this.userService.getCurrentUser());
        comment.setComment(commentValue);
        List<WorkflowActionCommentModel> comments = new ArrayList<>(action.getWorkflowActionComments());
        comments.add(comment);
        action.setWorkflowActionComments(comments);
        return comment;
    }


    public void setWorkflowService(WorkflowService workflowService)
    {
        this.workflowService = workflowService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
