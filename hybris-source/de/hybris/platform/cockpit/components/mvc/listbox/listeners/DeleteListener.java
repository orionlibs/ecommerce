package de.hybris.platform.cockpit.components.mvc.listbox.listeners;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

public class DeleteListener implements EventListener
{
    public void onEvent(Event event) throws Exception
    {
        if(event instanceof MouseEvent)
        {
            MouseEvent oldEvent = (MouseEvent)event;
            Listcell actionsCell = (Listcell)event.getTarget().getParent();
            Listitem listitem = (Listitem)actionsCell.getParent();
            Listbox listbox = actionsCell.getListbox();
            MouseEvent newEvent = new MouseEvent("onDeleteAtListBox", (Component)listitem, oldEvent.getX(), oldEvent.getY(), oldEvent.getKeys());
            Events.sendEvent((Component)listbox, (Event)newEvent);
        }
    }
}
