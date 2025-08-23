package de.hybris.platform.cockpit.components.mvc.listbox.listeners;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

public class KeyStrokeListener implements EventListener
{
    public void onEvent(Event event) throws Exception
    {
        KeyEvent orygEvent = (KeyEvent)event;
        Listbox listBox = ((Listitem)orygEvent.getTarget()).getListbox();
        KeyEvent keyEvent = new KeyEvent("onKeyAtListBoxPressed", orygEvent.getTarget(), orygEvent.getKeyCode(), orygEvent.isCtrlKey(), orygEvent.isShiftKey(), orygEvent.isAltKey());
        Events.sendEvent((Component)listBox, (Event)keyEvent);
    }
}
