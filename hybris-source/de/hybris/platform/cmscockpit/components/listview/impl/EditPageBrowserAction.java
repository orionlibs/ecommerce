package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class EditPageBrowserAction extends AbstractCmscockpitListViewAction
{
    private static final Logger LOG = Logger.getLogger(EditPageBrowserAction.class);
    private static final String EDIT_IMAGE_URI = "/cockpit/images/icon_func_edit.png";
    private static final String MULTISELECT_EDIT_IMAGE_URI = "/cockpit/images/icon_func_edit_22.png";
    private static final String MULTISELECT_EDIT_IMAGE_URI_UNAVAILABLE = "/cockpit/images/icon_func_edit_unavailable_22.png";


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        Object object = null;
        if(!isComponentLockedForUser(context.getItem()))
        {
            object = new Object(this, context);
        }
        return (EventListener)object;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getImageURI(ListViewAction.Context context)
    {
        if(!isEnabled() || isComponentLockedForUser(context.getItem()))
        {
            return null;
        }
        return "/cockpit/images/icon_func_edit.png";
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("general.edit");
    }


    public EventListener getMultiSelectEventListener(ListViewAction.Context context)
    {
        EventListener ret = null;
        if(CollectionUtils.isNotEmpty(context.getBrowserModel().getSelectedIndexes()) && context
                        .getBrowserModel().getSelectedIndexes().size() == 1)
        {
            if(context.getItem() == null && context.getBrowserModel().getSelectedItems().iterator().next() != null)
            {
                context.setItem(context.getBrowserModel().getSelectedItems().iterator().next());
            }
            ret = getEventListener(context);
        }
        return ret;
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        if(CollectionUtils.isNotEmpty(context.getBrowserModel().getSelectedIndexes()) && context
                        .getBrowserModel().getSelectedIndexes().size() == 1)
        {
            return "/cockpit/images/icon_func_edit_22.png";
        }
        return "/cockpit/images/icon_func_edit_unavailable_22.png";
    }
}
