package de.hybris.platform.testframework.runlistener;

import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class VMBlockTimeRunListener extends RunListener
{
    private static final Logger LOG = Logger.getLogger(VMBlockTimeRunListener.class.getName());
    private static final long maxAllowedBlockTime = 20000L;


    public void testRunStarted(Description description) throws Exception
    {
        VMBlockTimeRecorder.ensureRunning();
    }


    public void testStarted(Description description) throws Exception
    {
        VMBlockTimeRecorder.reset();
    }


    public void testFinished(Description description) throws Exception
    {
        long blockTimeMillis = VMBlockTimeRecorder.getMaxBlockTimeMillis();
        if(blockTimeMillis > 20000L)
        {
            LOG.error("Max allowed JVM blocking time of 20 seconds exceeded by " + (blockTimeMillis - 20000L) / 1000L + " seconds in test " + description
                            .getDisplayName() + " !");
        }
    }
}
