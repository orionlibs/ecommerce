package de.hybris.platform.workflow.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class WorkflowActionCommentModel extends ItemModel
{
    public static final String _TYPECODE = "WorkflowActionComment";
    public static final String _WORKFLOWACTIONCOMMENTRELATION = "WorkflowActionCommentRelation";
    public static final String COMMENT = "comment";
    public static final String USER = "user";
    public static final String WORKFLOWACTION = "workflowAction";


    public WorkflowActionCommentModel()
    {
    }


    public WorkflowActionCommentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WorkflowActionCommentModel(String _comment, AbstractWorkflowActionModel _workflowAction)
    {
        setComment(_comment);
        setWorkflowAction(_workflowAction);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WorkflowActionCommentModel(String _comment, ItemModel _owner, AbstractWorkflowActionModel _workflowAction)
    {
        setComment(_comment);
        setOwner(_owner);
        setWorkflowAction(_workflowAction);
    }


    @Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
    public String getComment()
    {
        return (String)getPersistenceContext().getPropertyValue("comment");
    }


    @Accessor(qualifier = "user", type = Accessor.Type.GETTER)
    public UserModel getUser()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("user");
    }


    @Accessor(qualifier = "workflowAction", type = Accessor.Type.GETTER)
    public AbstractWorkflowActionModel getWorkflowAction()
    {
        return (AbstractWorkflowActionModel)getPersistenceContext().getPropertyValue("workflowAction");
    }


    @Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
    public void setComment(String value)
    {
        getPersistenceContext().setPropertyValue("comment", value);
    }


    @Accessor(qualifier = "user", type = Accessor.Type.SETTER)
    public void setUser(UserModel value)
    {
        getPersistenceContext().setPropertyValue("user", value);
    }


    @Accessor(qualifier = "workflowAction", type = Accessor.Type.SETTER)
    public void setWorkflowAction(AbstractWorkflowActionModel value)
    {
        getPersistenceContext().setPropertyValue("workflowAction", value);
    }
}
