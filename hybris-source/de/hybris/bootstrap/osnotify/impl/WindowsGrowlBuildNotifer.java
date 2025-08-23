package de.hybris.bootstrap.osnotify.impl;

import de.hybris.bootstrap.osnotify.SystemTrayNotifier;
import java.io.File;
import java.io.IOException;

public class WindowsGrowlBuildNotifer extends AbstractBuildNotifier implements SystemTrayNotifier
{
    private static final String COMMAND = "growlnotify";
    private static final String APPLICATION = "growlnotify";
    private static final String APP_SWITCH = "/a:";
    private static final String TITLE_SWITCH = "/t:";
    private static final String SILENT_SWITCH = "/silent:true";


    public WindowsGrowlBuildNotifer(File _baseResourceDir)
    {
        super(_baseResourceDir);
    }


    public void notify(String msgTitle, String msgBody, SystemTrayNotifier.NotificationLevel level)
    {
        try
        {
            String application = pinSwitchAndValue("/a:", "growlnotify");
            String title = pinSwitchAndValue("/t:", msgTitle);
            String[] notifyCmd = {"growlnotify", "/silent:true", application, title, msgBody};
            getRuntime().exec(notifyCmd);
        }
        catch(IOException iOException)
        {
        }
    }


    private String pinSwitchAndValue(String pinSwitch, String value)
    {
        StringBuilder builder = new StringBuilder(pinSwitch);
        builder.append("\\\"").append(value).append("\\\"");
        return builder.toString();
    }
}
