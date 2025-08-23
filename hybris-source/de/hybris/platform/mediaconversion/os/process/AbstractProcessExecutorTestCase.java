package de.hybris.platform.mediaconversion.os.process;

import de.hybris.platform.mediaconversion.os.Drain;
import de.hybris.platform.mediaconversion.os.ProcessContext;
import de.hybris.platform.mediaconversion.os.ProcessExecutor;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public abstract class AbstractProcessExecutorTestCase
{
    private static final Logger LOG = Logger.getLogger(AbstractProcessExecutorTestCase.class);
    private ProcessExecutor executor;


    @Before
    public void setupExecutor() throws Exception
    {
        this.executor = createExecutor();
    }


    protected abstract ProcessExecutor createExecutor() throws Exception;


    protected abstract int amountOfThreads();


    @After
    public void cleanUp() throws IOException
    {
        if(this.executor != null)
        {
            LOG.debug("Quitting executor.");
            this.executor.quit();
        }
    }


    @Test
    public void testEcho() throws IOException
    {
        String message = "nemeses";
        StringDrain out = new StringDrain(false);
        StringDrain err = new StringDrain(false);
        int extVal = this.executor.execute(new ProcessContext(EchoCommandFactory.buildCommand(new String[] {"nemeses"}, ), null, null, (Drain)out, (Drain)err));
        Assert.assertEquals("Normal process termination.", 0L, extVal);
        Assert.assertEquals("No error out.", "", err.toString());
        Assert.assertEquals("Stdout speaks..", "nemeses", out.toString());
    }


    @Test
    public void testMultithreadedEcho() throws IOException, InterruptedException
    {
        execMultithreaded(EchoCommandFactory.buildCommand(new String[] {"Hallo welt!"}));
    }


    private void execMultithreaded(String... command) throws IOException, InterruptedException
    {
        execMultithreaded(this.executor, amountOfThreads(), command);
    }


    static void execMultithreaded(ProcessExecutor executor, int amountOfThreads, String... command) throws IOException, InterruptedException
    {
        Handle<Throwable> handle = new Handle();
        Semaphore lock = new Semaphore(1 - amountOfThreads);
        for(int i = 0; i < amountOfThreads; i++)
        {
            (new Object("test_" + i, executor, command, handle, lock))
                            .start();
        }
        lock.acquire();
        Assert.assertNull(handle.get());
    }
}
