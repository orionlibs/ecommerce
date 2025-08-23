package de.hybris.platform.productcockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class EditAction extends AbstractProductAction
{
    protected String ICON_FUNC_EDIT_ACTION_AVAILABLE = "cockpit/images/icon_func_edit_available.png";
    protected String ICON_FUNC_EDIT_ACTION_UNAVAILABLE = "cockpit/images/icon_func_edit_unavailable.png";


    public String getImageURI(ListViewAction.Context context)
    {
        if(Boolean.TRUE.equals(context.getMap().get(this.IN_EDITOR_AREA_SECTION_PANEL)))
        {
            return null;
        }
        return this.ICON_FUNC_EDIT_ACTION_AVAILABLE;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return getEditActionEventListener(context.getItem());
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("cockpit.inspector.edit");
    }


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public EventListener getMultiSelectEventListener(ListViewAction.Context context)
    {
        EventListener ret = null;
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(selectedItems != null && !selectedItems.isEmpty() && selectedItems.size() == 1)
        {
            ret = getEditActionEventListener(selectedItems.get(0));
        }
        return ret;
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(selectedItems != null && !selectedItems.isEmpty() && selectedItems.size() == 1)
        {
            return this.ICON_FUNC_EDIT_ACTION_AVAILABLE;
        }
        return this.ICON_FUNC_EDIT_ACTION_UNAVAILABLE;
    }


    private EventListener getEditActionEventListener(TypedObject item)
    {
        return (EventListener)new Object(this, item);
    }
}
