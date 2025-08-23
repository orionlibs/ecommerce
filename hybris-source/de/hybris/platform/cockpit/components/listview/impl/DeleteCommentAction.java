package de.hybris.platform.cockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.ReplyModel;
import de.hybris.platform.core.model.ItemModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class DeleteCommentAction extends AbstractCommentAction
{
    protected static final String TRASH_ICON_URI = "/cockpit/css/images/icon_func_delete_available.png";
    protected static final String TRASH_ICON_UNAVAILABLE_URI = "/cockpit/css/images/icon_func_delete_unavailable.png";


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


    protected void sendUpdateEvent(TypedObject typedObject, ListViewAction.Context context)
    {
        Object object = typedObject.getObject();
        if(object instanceof AbstractCommentModel)
        {
            List<ItemModel> relatedItems = new ArrayList<>();
            if(object instanceof CommentModel)
            {
                relatedItems.addAll(((CommentModel)object).getRelatedItems());
            }
            if(object instanceof ReplyModel)
            {
                relatedItems.addAll(((ReplyModel)object).getComment().getRelatedItems());
            }
            for(ItemModel item : relatedItems)
            {
                UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this,
                                UISessionUtils.getCurrentSession().getTypeService().wrapItem(item), Collections.EMPTY_LIST, ItemChangedEvent.ChangeType.REMOVED));
            }
        }
        else if(object instanceof ItemModel)
        {
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, typedObject, Collections.EMPTY_LIST, ItemChangedEvent.ChangeType.REMOVED));
        }
        sendUpdateItemsEvent(context);
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
            return "/cockpit/css/images/icon_func_delete_available.png";
        }
        return null;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("general.remove");
    }
}
