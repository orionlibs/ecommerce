package de.hybris.bootstrap.osnotify.impl;

import de.hybris.bootstrap.osnotify.SystemTrayNotifier;

public class MacOsSayNotifier implements SystemTrayNotifier
{
    public void notify(String msgTitle, String msgBody, SystemTrayNotifier.NotificationLevel level)
    {
        try
        {
            String[] notifyCmd = {"say", "-v", "Alex", "Hello Axel, dear father, " + msgBody};
            getRuntime().exec(notifyCmd);
        }
        catch(Exception exception)
        {
        }
    }


    protected Runtime getRuntime()
    {
        return Runtime.getRuntime();
    }
}
