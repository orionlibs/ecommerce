package de.hybris.bootstrap.osnotify.impl;

import de.hybris.bootstrap.osnotify.SystemTrayNotifier;
import java.io.File;
import java.io.IOException;

public class NotifySendBuildNotifier extends AbstractBuildNotifier implements SystemTrayNotifier
{
    private static final String COMMAND = "notify-send";
    private static final String ICON_SWITCH = "-i";
    private static final String EXPIRE_TIME_SWITCH = "-t";
    private static final String EXPIRE_TIME_IN_MILLIS = "5000";


    public NotifySendBuildNotifier(File _baseResourceDir)
    {
        super(_baseResourceDir);
    }


    public void notify(String msgTitle, String msgBody, SystemTrayNotifier.NotificationLevel level)
    {
        try
        {
            String[] notifyCmd = {"notify-send", "-t", "5000", "-i", getIconPath(level), msgTitle, msgBody};
            getRuntime().exec(notifyCmd);
        }
        catch(IOException iOException)
        {
        }
    }
}
