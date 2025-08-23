package de.hybris.bootstrap.osnotify.impl;

import de.hybris.bootstrap.osnotify.SystemTrayNotifier;
import java.io.File;

public class SystemTrayNotifierFactory
{
    private static final String WINDOWS_NAME = "windows";
    private static final String MAC_OS_X_NAME = "mac os x";


    public SystemTrayNotifier createNotifier(File platformBaseDir)
    {
        NotifySendBuildNotifier notifySendBuildNotifier;
        String osName = getLowercaseOsName();
        if(isSayNotifierEnabled(osName))
        {
            return (SystemTrayNotifier)new MacOsSayNotifier();
        }
        File baseResourcePath = new File("" + platformBaseDir + "/resources/notifications/");
        if(osName.startsWith("mac os x"))
        {
            if(isNotifyCenterEnabled())
            {
                MacOsNotifyCenterNotifier macOsNotifyCenterNotifier = new MacOsNotifyCenterNotifier(baseResourcePath);
            }
            else
            {
                MacOsGrowlBuildNotifier macOsGrowlBuildNotifier = new MacOsGrowlBuildNotifier(baseResourcePath);
            }
        }
        else if(osName.startsWith("windows"))
        {
            WindowsGrowlBuildNotifer windowsGrowlBuildNotifer = new WindowsGrowlBuildNotifer(baseResourcePath);
        }
        else
        {
            notifySendBuildNotifier = new NotifySendBuildNotifier(baseResourcePath);
        }
        return (SystemTrayNotifier)notifySendBuildNotifier;
    }


    private String getLowercaseOsName()
    {
        return System.getProperty("os.name").toLowerCase();
    }


    private boolean isNotifyCenterEnabled()
    {
        String version = System.getProperty("os.version");
        String[] splitResult = version.split("\\.");
        if(splitResult.length >= 2)
        {
            return (Integer.parseInt(splitResult[1]) >= 8);
        }
        return false;
    }


    private boolean isSayNotifierEnabled(String osName)
    {
        String enabled = System.getenv("JAVA8_HIGH_PERFORMANCE");
        return (osName.startsWith("mac os x") && Boolean.parseBoolean(enabled));
    }
}
