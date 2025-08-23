package de.hybris.platform.cockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class ActivateItemAction extends AbstractListViewAction
{
    private static final Logger LOG = LoggerFactory.getLogger(ActivateItemAction.class);
    private static final String IMAGE_URI = "/cockpit/images/item_edit_action.png";
    private static final String IMAGE_MULTISELECT_URI_AVAILABLE = "/cockpit/images/icon_func_edit_22.png";
    private static final String IMAGE_MULTISELECT_URI_UNAVAILABLE = "cockpit/images/icon_func_edit_unavailable_22.png";


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


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getImageURI(ListViewAction.Context context)
    {
        return "/cockpit/images/item_edit_action.png";
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("general.edit");
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(selectedItems != null && !selectedItems.isEmpty() && selectedItems.size() == 1)
        {
            return "/cockpit/images/icon_func_edit_22.png";
        }
        return "cockpit/images/icon_func_edit_unavailable_22.png";
    }


    public EventListener getMultiSelectEventListener(ListViewAction.Context context)
    {
        EventListener ret = null;
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(selectedItems != null && !selectedItems.isEmpty() && selectedItems.size() == 1)
        {
            context.setItem(selectedItems.get(0));
            ret = getEventListener(context);
        }
        return ret;
    }
}
