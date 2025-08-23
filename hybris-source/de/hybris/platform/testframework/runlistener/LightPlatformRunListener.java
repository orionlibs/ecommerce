package de.hybris.platform.testframework.runlistener;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.Utilities;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class LightPlatformRunListener extends RunListener
{
    private static final Logger log = Logger.getLogger(LightPlatformRunListener.class.getName());


    public void testRunStarted(Description description) throws Exception
    {
        Registry.activateStandaloneMode();
        Utilities.setJUnitTenant();
        if(log.isDebugEnabled())
        {
            log.debug("Setting Cluster and Tenant");
        }
    }
}
