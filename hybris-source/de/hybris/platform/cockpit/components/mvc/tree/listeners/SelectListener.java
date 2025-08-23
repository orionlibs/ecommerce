package de.hybris.platform.cockpit.components.mvc.tree.listeners;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Tree;

public class SelectListener implements EventListener
{
    public void onEvent(Event event) throws Exception
    {
        Tree target = (Tree)event.getTarget();
        Event selectedEvent = new Event("onTreeItemsWereSelected", (Component)target, event.getData());
        Events.sendEvent(selectedEvent);
    }
}
