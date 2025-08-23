package de.hybris.platform.cockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class NewItemAction extends AbstractListViewAction
{
    protected String ADD_BUTTON_ICON = "cockpit/images/icon_func_new_available.png";


    public String getImageURI(ListViewAction.Context context)
    {
        return this.ADD_BUTTON_ICON;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return (EventListener)new Object(this);
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("editorarea.button.createnewitem.tooltip");
    }


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }
}
