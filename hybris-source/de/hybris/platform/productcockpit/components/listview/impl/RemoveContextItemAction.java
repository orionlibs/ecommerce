package de.hybris.platform.productcockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class RemoveContextItemAction extends AbstractProductAction
{
    protected String BUTTON_ICON = "cockpit/images/icon_func_remove_listitem_available.png";
    protected String BUTTON_ICON_UNAVAILABLE = "cockpit/images/icon_func_remove_listitem_unavailable.png";


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("general.deleteItem");
    }


    public EventListener getMultiSelectEventListener(ListViewAction.Context context)
    {
        Object object = null;
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(CollectionUtils.isNotEmpty(selectedItems))
        {
            object = new Object(this, context);
        }
        return (EventListener)object;
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(CollectionUtils.isNotEmpty(selectedItems))
        {
            return this.BUTTON_ICON;
        }
        return this.BUTTON_ICON_UNAVAILABLE;
    }


    public String getImageURI(ListViewAction.Context context)
    {
        return null;
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
}
