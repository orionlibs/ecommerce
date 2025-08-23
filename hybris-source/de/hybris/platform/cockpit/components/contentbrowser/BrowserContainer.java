package de.hybris.platform.cockpit.components.contentbrowser;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;

public class BrowserContainer extends Div
{
    public BrowserContainer()
    {
        addEventListener("onClick", (EventListener)new Object(this));
    }
}
