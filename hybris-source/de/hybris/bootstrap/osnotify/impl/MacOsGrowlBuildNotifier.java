package de.hybris.bootstrap.osnotify.impl;

import de.hybris.bootstrap.osnotify.SystemTrayNotifier;
import java.io.File;
import java.io.IOException;

public class MacOsGrowlBuildNotifier extends AbstractBuildNotifier implements SystemTrayNotifier
{
    private static final String COMMAND = "growlnotify";
    private static final String TITLE_SWITCH = "-t";
    private static final String MESSAGE_SWITCH = "-m";
    private static final String ICON_SWITCH = "--image";


    public MacOsGrowlBuildNotifier(File _baseResourceDir)
    {
        super(_baseResourceDir);
    }


    public void notify(String msgTitle, String msgBody, SystemTrayNotifier.NotificationLevel level)
    {
        try
        {
            String[] notifyCmd = {"growlnotify", "--image", getIconPath(level), "-t", msgTitle, "-m", msgBody};
            getRuntime().exec(notifyCmd);
        }
        catch(IOException iOException)
        {
        }
    }
}
