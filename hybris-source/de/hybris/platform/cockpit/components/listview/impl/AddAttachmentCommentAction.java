package de.hybris.platform.cockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.ReplyModel;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class AddAttachmentCommentAction extends AbstractCommentAction
{
    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return (EventListener)new Object(this, context);
    }


    public String getImageURI(ListViewAction.Context context)
    {
        boolean permission = UISessionUtils.getCurrentSession().getSystemService().checkPermissionOn(
                        UISessionUtils.getCurrentSession().getTypeService().getObjectType("CommentAttachment").getCode(), "create");
        TypedObject typedObject = context.getItem();
        Object object = null;
        if(typedObject != null)
        {
            object = typedObject.getObject();
        }
        List<ReplyModel> replies = new ArrayList<>();
        if(object instanceof CommentModel)
        {
            replies.addAll(((CommentModel)object).getReplies());
        }
        else if(object instanceof ReplyModel)
        {
            replies.addAll(((ReplyModel)object).getReplies());
        }
        if(permission && object instanceof AbstractCommentModel &&
                        UISessionUtils.getCurrentSession().getUser().equals(((AbstractCommentModel)object).getAuthor()) && replies
                        .isEmpty())
        {
            return "/cockpit/images/icon_add_attachement.png";
        }
        return null;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("comment.attachment.add");
    }
}
