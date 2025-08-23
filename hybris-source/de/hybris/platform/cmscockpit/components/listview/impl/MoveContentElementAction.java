package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class MoveContentElementAction extends AbstractListViewAction
{
    private static final String MOVE_CONTENT_ELEMENT_ACTION_URL = "/cmscockpit/images/cnt_elem_move_action.png";


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getImageURI(ListViewAction.Context context)
    {
        return "/cmscockpit/images/cnt_elem_move_action.png";
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("general.move");
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return null;
    }
}
