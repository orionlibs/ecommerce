package de.hybris.platform.cockpit.components.mvc.tree.listeners;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treerow;

public class DropListener implements EventListener
{
    public void onEvent(Event event) throws Exception
    {
        if(event instanceof DropEvent)
        {
            DropEvent orygEvent = (DropEvent)event;
            Tree tree = ((Treerow)orygEvent.getTarget()).getTree();
            DropEvent dropEvent = new DropEvent("onSomethingWasDropped", orygEvent.getTarget(), orygEvent.getDragged(), orygEvent.getX(), orygEvent.getY(), orygEvent.getKeys());
            Events.sendEvent((Component)tree, (Event)dropEvent);
        }
    }
}
