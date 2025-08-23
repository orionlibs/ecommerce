package de.hybris.platform.cockpit.components;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Timer;

public class NotifierComponent extends Div
{
    public NotifierComponent()
    {
        Timer timer = new Timer();
        timer.setParent((Component)this);
        timer.setId("timer");
        timer.setRepeats(false);
        timer.setRunning(false);
        Div div = new Div();
        div.setParent((Component)this);
        addEventListener("onCreate", (EventListener)new Object(this, timer, div));
    }
}
