package de.hybris.bootstrap.osnotify.impl;

import de.hybris.bootstrap.osnotify.SystemTrayNotifier;
import java.io.File;

public abstract class AbstractBuildNotifier
{
    private static final String NOTICE_ICON = "NotificationIcon_notice.gif";
    private static final String ERROR_ICON = "NotificationIcon_error.gif";
    protected final String baseResourceDir;


    public AbstractBuildNotifier(File _baseResourceDir)
    {
        this.baseResourceDir = _baseResourceDir.getAbsolutePath();
    }


    protected Runtime getRuntime()
    {
        return Runtime.getRuntime();
    }


    protected String getIconPath(SystemTrayNotifier.NotificationLevel severity)
    {
        return this.baseResourceDir + this.baseResourceDir + File.separator;
    }
}
