package de.hybris.platform.cockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class AddMultiItemCommentAction extends AbstractCommentAction
{
    private TypeService typeService;
    private String commentTypeCode;
    private List<TypedObject> selectedItems;


    public AddMultiItemCommentAction()
    {
    }


    public AddMultiItemCommentAction(List<TypedObject> selectedItems)
    {
        this.selectedItems = selectedItems;
    }


    public EventListener getMultiSelectEventListener(ListViewAction.Context context)
    {
        Object object = null;
        if(BooleanUtils.toBoolean(UITools.getCockpitParameter("default.comments.enabled", Executions.getCurrent())) &&
                        CollectionUtils.isNotEmpty(context.getBrowserModel().getSelectedItems()) && context
                        .getBrowserModel().getSelectedItems().size() >= 1)
        {
            object = new Object(this);
        }
        return (EventListener)object;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return null;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("comment.addMultiItemComment");
    }


    public String getImageURI(ListViewAction.Context context)
    {
        if(BooleanUtils.toBoolean(UITools.getCockpitParameter("default.comments.enabled", Executions.getCurrent())))
        {
            return "cockpit/images/icon_func_comment_available.png";
        }
        return null;
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        if(BooleanUtils.toBoolean(UITools.getCockpitParameter("default.comments.enabled", Executions.getCurrent())) &&
                        CollectionUtils.isNotEmpty(context.getBrowserModel().getSelectedItems()) && context
                        .getBrowserModel().getSelectedItems().size() >= 1)
        {
            return "cockpit/images/icon_func_comment_available.png";
        }
        return "cockpit/images/icon_func_comment_unavailable.png";
    }


    public TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    public String getCommentTypeCode()
    {
        return this.commentTypeCode;
    }


    public void setCommentTypeCode(String commentTypeCode)
    {
        this.commentTypeCode = commentTypeCode;
    }
}
