package de.hybris.platform.workflow.strategies.impl;

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionCommentModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.strategies.DecideActionStrategy;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class DefaultDecideActionStrategy implements DecideActionStrategy
{
    private ModelService modelService;


    public void doAfterActivationOfAndLink(WorkflowActionModel action)
    {
        writeAutomatedComment(action, "text.automatedcomments.activatedthroughandlink", new String[0]);
    }


    public void doAfterActivationOfOrLink(WorkflowActionModel action)
    {
        writeAutomatedComment(action, "text.automatedcomments.activatedthroughorlink", new String[0]);
    }


    public void doAfterDecisionMade(WorkflowActionModel action, WorkflowDecisionModel selDec)
    {
        writeAutomatedComment(action, "text.automatedcomments.completed", new String[] {selDec
                        .getName()});
    }


    protected void writeAutomatedComment(WorkflowActionModel action, String message, String... messageParams)
    {
        List<WorkflowActionCommentModel> comments = new ArrayList<>(action.getWorkflowActionComments());
        WorkflowActionCommentModel comment = (WorkflowActionCommentModel)this.modelService.create(WorkflowActionCommentModel.class);
        comment.setComment(MessageFormat.format(Localization.getLocalizedString(message, (Object[])messageParams), (Object[])null));
        comment.setUser(null);
        comment.setWorkflowAction((AbstractWorkflowActionModel)action);
        comments.add(comment);
        action.setWorkflowActionComments(comments);
        this.modelService.save(action);
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
