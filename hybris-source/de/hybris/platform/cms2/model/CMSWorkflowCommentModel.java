package de.hybris.platform.cms2.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;

public class CMSWorkflowCommentModel extends CommentModel
{
    public static final String _TYPECODE = "CMSWorkflowComment";
    public static final String DECISION = "decision";


    public CMSWorkflowCommentModel()
    {
    }


    public CMSWorkflowCommentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSWorkflowCommentModel(UserModel _author, CommentTypeModel _commentType, ComponentModel _component, String _text)
    {
        setAuthor(_author);
        setCommentType(_commentType);
        setComponent(_component);
        setText(_text);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSWorkflowCommentModel(UserModel _author, CommentTypeModel _commentType, ComponentModel _component, ItemModel _owner, String _text)
    {
        setAuthor(_author);
        setCommentType(_commentType);
        setComponent(_component);
        setOwner(_owner);
        setText(_text);
    }


    @Accessor(qualifier = "decision", type = Accessor.Type.GETTER)
    public WorkflowDecisionModel getDecision()
    {
        return (WorkflowDecisionModel)getPersistenceContext().getPropertyValue("decision");
    }


    @Accessor(qualifier = "decision", type = Accessor.Type.SETTER)
    public void setDecision(WorkflowDecisionModel value)
    {
        getPersistenceContext().setPropertyValue("decision", value);
    }
}
