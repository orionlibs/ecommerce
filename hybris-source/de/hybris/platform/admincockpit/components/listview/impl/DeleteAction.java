package de.hybris.platform.admincockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class DeleteAction extends AbstractListViewAction
{
    protected String BUTTON_ICON = "cockpit/images/trash.png";
    protected String BUTTON_ICON_DISABLED = "cockpit/images/icon_func_delete_unavailable.png";
    private ModelService modelService = null;


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


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
        return this.BUTTON_ICON;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("ba.delete_tooltip");
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(selectedItems != null && !selectedItems.isEmpty() && selectedItems.size() >= 1)
        {
            return this.BUTTON_ICON;
        }
        return this.BUTTON_ICON_DISABLED;
    }


    public EventListener getMultiSelectEventListener(ListViewAction.Context context)
    {
        Object object = null;
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(selectedItems != null && !selectedItems.isEmpty() && selectedItems.size() >= 1)
        {
            object = new Object(this, selectedItems);
        }
        return (EventListener)object;
    }
}
