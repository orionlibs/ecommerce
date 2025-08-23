package de.hybris.platform.testframework.runlistener;

import org.junit.runner.notification.RunListener;

public class CustomRunListener extends RunListener
{
    public int getPriority()
    {
        return 0;
    }
}
