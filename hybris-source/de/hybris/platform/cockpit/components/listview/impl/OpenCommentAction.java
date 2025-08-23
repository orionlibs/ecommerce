package de.hybris.platform.cockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.impl.CommunicationBrowserModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.core.model.ItemModel;
import java.util.Collections;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Menupopup;

public class OpenCommentAction extends AbstractCommentAction
{
    private String commentTypeCode;
    private CommunicationBrowserModel communicationBrowserModel;


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
        TypedObject item = context.getItem();
        List<CommentModel> comments = getCockpitCommentService().getItemComments((ItemModel)item.getObject(), null,
                        Collections.singleton(getComponent()), Collections.singletonList(getCommentType()), 0, -1);
        if(!comments.isEmpty())
        {
            return "/cockpit/images/icon_func_comment_available.png";
        }
        return null;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("comment.open");
    }


    public String getCommentTypeCode()
    {
        return this.commentTypeCode;
    }


    public void setCommentTypeCode(String commentTypeCode)
    {
        this.commentTypeCode = commentTypeCode;
        if(getDomain() != null && getComponent() != null && !getCommentTypeCode().isEmpty())
        {
            setCommentType(getCommentService().getCommentTypeForCode(getComponent(), getCommentTypeCode()));
        }
    }


    public CommunicationBrowserModel getCommunicationBrowserModel()
    {
        if(this.communicationBrowserModel == null)
        {
            this.communicationBrowserModel = (CommunicationBrowserModel)SpringUtil.getBean("CommunicationBrowserModel");
        }
        return this.communicationBrowserModel;
    }
}
