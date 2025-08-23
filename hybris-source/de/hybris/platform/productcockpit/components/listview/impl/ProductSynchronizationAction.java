package de.hybris.platform.productcockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Window;

public class ProductSynchronizationAction extends ProductSynchronizationStatus
{
    private static final Logger LOG = LoggerFactory.getLogger(ProductSynchronizationAction.class);
    protected static final String ICON_SYNC_ACTION_AVAILABLE = "cockpit/images/icon_func_sync.png";
    protected static final String ICON_SYNC_ACTION_UNAVAILABLE = "cockpit/images/icon_func_sync_unavailable.png";


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return (EventListener)new Object(this, context);
    }


    private void detachDialog(Window dialog)
    {
        dialog.addEventListener("onOpen", (EventListener)new Object(this, dialog));
    }


    public EventListener getMultiSelectEventListener(ListViewAction.Context context)
    {
        Object object = null;
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(selectedItems != null && !selectedItems.isEmpty() && selectedItems.size() >= 1)
        {
            object = new Object(this, context, selectedItems);
        }
        return (EventListener)object;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("gridview.item.synchronized.action.tooltip");
    }


    protected String getSyncNotOKImg()
    {
        return "cockpit/images/icon_func_sync.png";
    }


    protected String getSyncInitImg()
    {
        return "cockpit/images/icon_func_sync.png";
    }


    protected String getSyncOKImg()
    {
        return "cockpit/images/icon_func_sync.png";
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(selectedItems != null && !selectedItems.isEmpty() && selectedItems.size() >= 1)
        {
            return "cockpit/images/icon_func_sync.png";
        }
        return "cockpit/images/icon_func_sync_unavailable.png";
    }
}
