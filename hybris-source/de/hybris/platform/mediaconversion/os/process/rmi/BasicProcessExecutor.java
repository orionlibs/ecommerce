package de.hybris.platform.mediaconversion.os.process.rmi;

import de.hybris.platform.mediaconversion.os.Drain;
import de.hybris.platform.mediaconversion.os.ProcessContext;
import de.hybris.platform.mediaconversion.os.process.AbstractProcessExecutor;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BasicProcessExecutor extends AbstractProcessExecutor
{
    private static final long TERMINATION_TIMEOUT = 5L;
    private final ExecutorService threadExecutor = Executors.newCachedThreadPool();


    public int execute(ProcessContext context) throws IOException
    {
        try
        {
            Process process = Runtime.getRuntime().exec(context.getCommand(), context.getEnvironment(), context.getDirectory());
            CountDownLatch countDown = new CountDownLatch(2);
            pipe(countDown, process.getErrorStream(), context.getStdError(), context.getStdError());
            pipe(countDown, process.getInputStream(), context.getStdOutput(), context.getStdError());
            int ret = process.waitFor();
            countDown.await();
            return ret;
        }
        catch(InterruptedException e)
        {
            throw new IOException("Failed to wait for process termination.", e);
        }
    }


    private void pipe(CountDownLatch countDown, InputStream inStream, Drain drain, Drain error)
    {
        this.threadExecutor.execute((Runnable)new Object(this, inStream, error, countDown, drain));
    }


    public void quit() throws IOException
    {
        this.threadExecutor.shutdown();
        try
        {
            this.threadExecutor.awaitTermination(5L, TimeUnit.SECONDS);
            int stillRunning = this.threadExecutor.shutdownNow().size();
            if(stillRunning > 0)
            {
                throw new IOException("Failed to shutdown. There are still " + stillRunning + " threads running.");
            }
        }
        catch(InterruptedException e)
        {
            throw new IOException("Failed to await termination.", e);
        }
    }
}
