package de.hybris.platform.cockpit.components.mvc.tree.listeners;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

public class AddNodeListener implements EventListener
{
    public void onEvent(Event event) throws Exception
    {
        Treecell actionsCell = (Treecell)event.getTarget().getParent().getParent();
        Tree tree = actionsCell.getTree();
        Treerow treerow = (Treerow)actionsCell.getParent();
        Treeitem treeitem = (Treeitem)treerow.getParent();
        Treeitem parentItem = treeitem.getParentItem();
        if(parentItem == null)
        {
            treerow = null;
        }
        else
        {
            treerow = parentItem.getTreerow();
        }
        Event newEvent = new Event("onCreateNode", (Component)treerow);
        Events.sendEvent((Component)tree, newEvent);
    }
}
