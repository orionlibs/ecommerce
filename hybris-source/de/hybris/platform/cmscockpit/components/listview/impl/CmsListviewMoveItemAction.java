package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

public class CmsListviewMoveItemAction extends AbstractCmscockpitListViewAction
{
    private static final String MOVE_ROW_ACTION_URL = "/cmscockpit/images/icon_func_move.png";
    private static final String MOVE_TO_TOP_ROW_ACTION_URL = "/cmscockpit/images/icon_func_move-to-top.png";
    private static final String MOVE_TO_BOTTOM_ROW_ACTION_URL = "/cmscockpit/images/icon_func_move-to-bottom.png";


    public String getImageURI(ListViewAction.Context context)
    {
        return "/cmscockpit/images/icon_func_move.png";
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return null;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        Menupopup ret = new Menupopup();
        int rowCount = context.getModel().getListComponentModel().getListModel().getElements().size();
        int rowIndex = context.getModel().getListComponentModel().getListModel().getElements().indexOf(context.getItem());
        Menuitem moveToTop = new Menuitem(Labels.getLabel("cmscockpit.action.moveToTop"), "/cmscockpit/images/icon_func_move-to-top.png");
        moveToTop.addEventListener("onClick", (EventListener)new Object(this, rowIndex, rowCount, context));
        ret.appendChild((Component)moveToTop);
        Menuitem moveToBottom = new Menuitem(Labels.getLabel("cmscockpit.action.moveToBottom"), "/cmscockpit/images/icon_func_move-to-bottom.png");
        moveToBottom.addEventListener("onClick", (EventListener)new Object(this, rowIndex, rowCount, context));
        ret.appendChild((Component)moveToBottom);
        return ret;
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("cmscockpit.action.moveItem");
    }


    protected void doCreateContext(ListViewAction.Context context)
    {
    }
}
