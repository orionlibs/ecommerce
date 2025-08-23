package de.hybris.platform.cockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.components.mvc.commentlayer.CommentLayerUtils;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.comments.constants.GeneratedCommentsConstants;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.ReplyModel;
import de.hybris.platform.core.model.ItemModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Textbox;

public class AnswerCommentAction extends AbstractCommentAction
{
    protected static final String ANSWER_ICON_URI = "/cockpit/css/images/icon_func_reply_available.png";
    protected static final String ANSWER_ICON_UNAVAILABLE_URI = "/cockpit/css/images/icon_func_reply_unavailable.png";


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


    private boolean onSaveButtonCommonAction(ListViewAction.Context context, TypedObject typedObject, Textbox textbox)
    {
        if(getComponent() != null && getCommentType() != null && typedObject != null && textbox.getValue() != null)
        {
            String textValue = textbox.getValue();
            if(!CommentLayerUtils.isValidCommentText(textValue))
            {
                UITools.modifySClass((HtmlBasedComponent)textbox, "error", true);
                return false;
            }
            UITools.modifySClass((HtmlBasedComponent)textbox, "error", false);
            Object object = typedObject.getObject();
            if(object instanceof AbstractCommentModel)
            {
                getCockpitCommentService().createReply(UISessionUtils.getCurrentSession().getUser(), (AbstractCommentModel)object, textbox
                                .getValue().trim());
            }
            else if(object instanceof ItemModel)
            {
                getCockpitCommentService().createItemComment(UISessionUtils.getCurrentSession().getUser(), getComponent(),
                                getCommentType(), Collections.singletonList((ItemModel)object), "", textbox.getValue().trim());
            }
            if(context.getMap().get(UPDATELISTENER) instanceof EditorListener)
            {
                EditorListener updateListener = (EditorListener)context.getMap().get(UPDATELISTENER);
                updateListener.valueChanged(null);
            }
            sendUpdateEvent(typedObject, context);
            return true;
        }
        return false;
    }


    private void sendUpdateEvent(TypedObject typedObject, ListViewAction.Context context)
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
                                UISessionUtils.getCurrentSession().getTypeService().wrapItem(item),
                                Collections.singletonList(UISessionUtils.getCurrentSession().getTypeService()
                                                .getPropertyDescriptor("Item.comments")), ItemChangedEvent.ChangeType.CREATED));
            }
        }
        else if(object instanceof ItemModel)
        {
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, typedObject,
                            Collections.singletonList(UISessionUtils.getCurrentSession()
                                            .getTypeService().getPropertyDescriptor("Item.comments")), ItemChangedEvent.ChangeType.CREATED));
        }
        sendUpdateItemsEvent(context);
    }


    public String getImageURI(ListViewAction.Context context)
    {
        boolean permission = UISessionUtils.getCurrentSession().getSystemService().checkPermissionOn(
                        UISessionUtils.getCurrentSession().getTypeService().getObjectType(GeneratedCommentsConstants.TC.REPLY).getCode(), "create");
        if(permission)
        {
            return "/cockpit/css/images/icon_func_reply_available.png";
        }
        return null;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("comment.reply");
    }
}
