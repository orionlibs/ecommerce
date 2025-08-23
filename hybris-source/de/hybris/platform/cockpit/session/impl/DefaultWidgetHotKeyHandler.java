package de.hybris.platform.cockpit.session.impl;

public class DefaultWidgetHotKeyHandler extends AbstractWidgetHotKeyHandler
{
    public void handleHotKey(Object data)
    {
        boolean done = false;
        if(data instanceof String)
        {
            String action = (String)data;
            String[] tmp = action.split("focuswidget_");
            if(tmp.length == 2)
            {
                focusWidget(tmp[1]);
                done = true;
            }
        }
        if(!done)
        {
            super.handleHotKey(data);
        }
    }


    public boolean isBusyListener()
    {
        return false;
    }
}
