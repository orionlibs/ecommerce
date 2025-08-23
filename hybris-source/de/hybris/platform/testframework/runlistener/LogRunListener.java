package de.hybris.platform.testframework.runlistener;

import de.hybris.platform.util.Utilities;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class LogRunListener extends RunListener
{
    private static final Logger LOG = Logger.getLogger(LogRunListener.class.getName());
    private long testStartTime;
    private String currentTestClass;


    public void testRunStarted(Description description) throws Exception
    {
        if(LOG.isInfoEnabled())
        {
            LOG.info("Starting test class " + description.getDisplayName());
            this.currentTestClass = description.getDisplayName();
        }
    }


    public void testStarted(Description description) throws Exception
    {
        if(LOG.isInfoEnabled())
        {
            LOG.info("Starting test method " + description.getDisplayName());
        }
        this.testStartTime = System.currentTimeMillis();
    }


    public void testFailure(Failure failure) throws Exception
    {
        LOG.error("Test method " + failure.getTestHeader() + " failed!!", failure.getException());
    }


    public void testFinished(Description description) throws Exception
    {
        long timePassed = System.currentTimeMillis() - this.testStartTime;
        if(LOG.isInfoEnabled())
        {
            LOG.info("Finished test method " + description.getDisplayName() + " in " + timePassed / 1000L + " seconds");
        }
    }


    public void testRunFinished(Result result) throws Exception
    {
        if(LOG.isInfoEnabled())
        {
            LOG.info("Finished (" + (result.wasSuccessful() ? "successful" : "failed") + ") test class " + this.currentTestClass);
            LOG.info("  Total run time: " + Utilities.formatTime(result.getRunTime()));
            LOG.info("  Total tests count: " + result.getRunCount());
            LOG.info("  Failed tests count: " + result.getFailureCount());
            LOG.info("  Ignored tests count: " + result.getIgnoreCount());
        }
    }
}
