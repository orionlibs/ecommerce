package de.hybris.platform.cockpit.components.menu;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menuitem;

public class ManageMenuitem extends Menuitem
{
    public ManageMenuitem(String label, EventListener listener)
    {
        setLabel(label);
        addEventListener("onClick", listener);
        setImage("/cockpit/images/icon_contextmenu_add.png");
    }
}
