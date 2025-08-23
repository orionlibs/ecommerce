package de.hybris.bootstrap.osnotify.impl;

import de.hybris.bootstrap.osnotify.SystemTrayNotifier;
import java.io.File;
import java.io.IOException;

public class MacOsNotifyCenterNotifier extends AbstractBuildNotifier implements SystemTrayNotifier
{
    private static final String COMMAND = "terminal-notifier";
    private static final String TITLE_SWITCH = "-title";
    private static final String MESSAGE_SWITCH = "-message";


    public MacOsNotifyCenterNotifier(File _baseResourceDir)
    {
        super(_baseResourceDir);
    }


    public void notify(String msgTitle, String msgBody, SystemTrayNotifier.NotificationLevel level)
    {
        try
        {
            String[] notifyCmd = {"terminal-notifier", "-title", msgTitle, "-message", msgBody};
            getRuntime().exec(notifyCmd);
        }
        catch(IOException iOException)
        {
        }
    }
}
