package de.hybris.platform.cockpit.components.contentbrowser;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Borderlayout;

public class CenterAreaContainer extends Borderlayout
{
    public CenterAreaContainer()
    {
        addEventListener("onCreate", (EventListener)new Object(this));
    }
}
