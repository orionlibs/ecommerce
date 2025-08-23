package de.hybris.platform.cockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.comments.CommentLayerService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.comments.model.AbstractCommentModel;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.event.EventListener;

public class AnswerCommentFromCommenLayerAction extends AnswerCommentAction
{
    private CommentLayerService commentLayerService;


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
        return this.commentLayerService.canUserReplyToComment(UISessionUtils.getCurrentSession().getUser(), (AbstractCommentModel)object) ?
                        "/cockpit/css/images/icon_func_reply_available.png" : "/cockpit/css/images/icon_func_reply_unavailable.png";
    }


    @Required
    public void setCommentLayerService(CommentLayerService commentLayerService)
    {
        this.commentLayerService = commentLayerService;
    }
}
