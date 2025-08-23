package de.hybris.platform.cockpit.components.mvc.tree.listeners;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treerow;

public class DoubleClickListener implements EventListener
{
    public void onEvent(Event event) throws Exception
    {
        if(event instanceof MouseEvent)
        {
            MouseEvent oldEvent = (MouseEvent)event;
            Treerow treerow = (Treerow)event.getTarget();
            Tree tree = treerow.getTree();
            MouseEvent newEvent = new MouseEvent("onDoubleClicked", (Component)treerow, oldEvent.getX(), oldEvent.getY(), oldEvent.getKeys());
            Events.sendEvent((Component)tree, (Event)newEvent);
        }
    }
}
