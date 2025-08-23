package de.hybris.platform.productcockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class EditItemAction extends AbstractListViewAction
{
    private static final String IMAGE_URI = "cockpit/images/icon_func_edit_22.png";
    private static final String IMAGE_URI_UNAVAILABLE = "cockpit/images/icon_func_edit_unavailable_22.png";


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
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(selectedItems != null && !selectedItems.isEmpty() && selectedItems.size() == 1)
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
        if(context.getListModel() != null && CollectionUtils.isNotEmpty(context.getListModel().getSelectedIndexes()) && context
                        .getListModel().getSelectedIndexes().size() == 1)
        {
            return "cockpit/images/icon_func_edit_22.png";
        }
        if(context.getBrowserModel() != null && CollectionUtils.isNotEmpty(context.getBrowserModel().getSelectedIndexes()) && context
                        .getBrowserModel().getSelectedIndexes().size() == 1)
        {
            return "cockpit/images/icon_func_edit_22.png";
        }
        return "cockpit/images/icon_func_edit_unavailable_22.png";
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("cockpit.inspector.edit");
    }
}
