package de.hybris.platform.productcockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class DecisionAction extends AbstractListViewAction
{
    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return null;
    }


    public String getImageURI(ListViewAction.Context context)
    {
        return null;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("gridview.item.decision.tooltip");
    }
}
