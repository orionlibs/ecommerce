package de.hybris.platform.cockpit.components.contentbrowser;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zul.Menupopup;

public class AdvanceMenupopup extends Menupopup
{
    public AdvanceMenupopup()
    {
        setClass("advanceMenu_userList");
    }


    public void beforeChildAdded(Component child, Component refChild)
    {
        if(!(child instanceof org.zkoss.zk.ui.HtmlBasedComponent))
        {
            throw new UiException("Unsupported child for menupopup: " + child);
        }
    }
}
