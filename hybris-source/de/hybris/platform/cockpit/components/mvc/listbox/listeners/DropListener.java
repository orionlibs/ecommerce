package de.hybris.platform.cockpit.components.mvc.listbox.listeners;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

public class DropListener implements EventListener
{
    public void onEvent(Event event) throws Exception
    {
        Component dragged = ((DropEvent)event).getDragged();
        if(dragged instanceof Listitem)
        {
            if(event instanceof DropEvent)
            {
                DropEvent orygEvent = (DropEvent)event;
                Listbox listbox = ((Listitem)event.getTarget()).getListbox();
                DropEvent dropEvent = new DropEvent("onDropAtListBox", orygEvent.getTarget(), orygEvent.getDragged(), orygEvent.getX(), orygEvent.getY(), orygEvent.getKeys());
                Events.sendEvent((Component)listbox, (Event)dropEvent);
            }
        }
    }
}
