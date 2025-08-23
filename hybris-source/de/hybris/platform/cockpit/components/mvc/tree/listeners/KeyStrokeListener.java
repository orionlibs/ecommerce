package de.hybris.platform.cockpit.components.mvc.tree.listeners;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treerow;

public class KeyStrokeListener implements EventListener
{
    public void onEvent(Event event) throws Exception
    {
        KeyEvent orygEvent = (KeyEvent)event;
        Tree tree = ((Treerow)orygEvent.getTarget()).getTree();
        KeyEvent keyEvent = new KeyEvent("onKeyPressed", orygEvent.getTarget(), orygEvent.getKeyCode(), orygEvent.isCtrlKey(), orygEvent.isShiftKey(), orygEvent.isAltKey());
        Events.sendEvent((Component)tree, (Event)keyEvent);
    }
}
