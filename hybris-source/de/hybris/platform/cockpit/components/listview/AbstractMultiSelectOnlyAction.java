package de.hybris.platform.cockpit.components.listview;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public abstract class AbstractMultiSelectOnlyAction extends AbstractListViewAction
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


    public abstract String getTooltip(ListViewAction.Context paramContext);


    public abstract EventListener getMultiSelectEventListener(ListViewAction.Context paramContext);


    public abstract String getMultiSelectImageURI(ListViewAction.Context paramContext);


    public abstract Menupopup getMultiSelectPopup(ListViewAction.Context paramContext);
}
