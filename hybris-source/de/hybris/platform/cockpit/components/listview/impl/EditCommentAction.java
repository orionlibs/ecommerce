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

public class EditCommentAction extends AbstractCommentAction
{
    protected static final String COCKPIT_IMAGES_EDIT_ACTION_PNG = "/cockpit/css/images/icon_func_edit_available.png";
    protected static final String COCKPIT_IMAGES_EDIT_ACTION_UNAVAILABLE_PNG = "/cockpit/css/images/icon_func_edit_unavailable.png";


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
        if(object instanceof AbstractCommentModel &&
                        UISessionUtils.getCurrentSession().getUser().equals(((AbstractCommentModel)object).getAuthor()) && replies
                        .isEmpty())
        {
            return "/cockpit/css/images/icon_func_edit_available.png";
        }
        return null;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("general.edit");
    }
}
