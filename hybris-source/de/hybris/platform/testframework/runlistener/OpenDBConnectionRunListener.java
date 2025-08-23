package de.hybris.platform.testframework.runlistener;

import de.hybris.platform.core.Registry;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class OpenDBConnectionRunListener extends RunListener
{
    private static final Logger LOG = Logger.getLogger(OpenDBConnectionRunListener.class.getName());


    public void testFinished(Description description) throws Exception
    {
        LOG.info("Currently still open DB connection: " + Registry.getCurrentTenant().getDataSource().getNumInUse());
    }
}
