package de.hybris.platform.cockpit.components.dialog;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.impl.MessageboxDlg;

public class WarnDialog extends MessageboxDlg
{
    private int _result;
    private EventListener _listener;


    public void setEventListener(EventListener listener)
    {
        this._listener = listener;
    }


    public void setFocus(int buttonNumber)
    {
        if(buttonNumber > 0)
        {
            Toolbarbutton btn = (Toolbarbutton)getFellowIfAny("toolbarbutton" + buttonNumber);
            if(btn != null)
            {
                btn.focus();
            }
        }
    }


    public void endModal(int button)
    {
        this._result = button;
        detach();
    }


    public int getResult()
    {
        return this._result;
    }
}
