package de.hybris.platform.testframework.runlistener;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.mockito.internal.progress.ThreadSafeMockingProgress;

public class ResetMockitoRunListener extends RunListener
{
    public void testRunStarted(Description description) throws Exception
    {
        resetMockito();
    }


    public void testRunFinished(Result result) throws Exception
    {
        resetMockito();
    }


    public static void resetMockito()
    {
        ThreadSafeMockingProgress.mockingProgress().reset();
    }
}
