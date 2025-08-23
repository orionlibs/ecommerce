package de.hybris.platform.cockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class SplitterDummyAction extends AbstractListViewAction
{
    public String getImageURI(ListViewAction.Context context)
    {
        return "cockpit/images/splitter_grey_space.gif";
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        return getImageURI(context);
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


    public String getTooltip(ListViewAction.Context context)
    {
        return null;
    }


    protected void doCreateContext(ListViewAction.Context context)
    {
    }
}
