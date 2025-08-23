package de.hybris.platform.admincockpit.buttons;

import de.hybris.platform.cockpit.components.CreateNewComponent;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import org.zkoss.zk.ui.event.EventListener;

public class CreateNewItemButton extends CreateNewComponent
{
    public CreateNewItemButton(UIBrowserArea browserArea)
    {
        addEventListener("onClick", (EventListener)new Object(this, browserArea));
    }
}
