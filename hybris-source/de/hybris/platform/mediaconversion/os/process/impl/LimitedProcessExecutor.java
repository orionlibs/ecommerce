package de.hybris.platform.mediaconversion.os.process.impl;

import de.hybris.platform.mediaconversion.os.ProcessContext;
import de.hybris.platform.mediaconversion.os.ProcessExecutor;
import de.hybris.platform.mediaconversion.os.process.AbstractProcessExecutor;
import java.io.IOException;
import java.util.concurrent.Semaphore;

public class LimitedProcessExecutor extends AbstractProcessExecutor
{
    private final ProcessExecutor executor;
    private final Semaphore semaphore;
    private volatile boolean terminated;


    public LimitedProcessExecutor(int limit, ProcessExecutor executor)
    {
        this.semaphore = new Semaphore(limit, true);
        this.executor = executor;
        if(this.executor == null)
        {
            throw new IllegalArgumentException("Process executor to delegate to must not be null.");
        }
    }


    public ProcessExecutor getExecutor()
    {
        return this.executor;
    }


    public int execute(ProcessContext context) throws IOException
    {
        if(this.terminated)
        {
            throw new IllegalStateException("Must not use terminated process executor.");
        }
        try
        {
            this.semaphore.acquire();
        }
        catch(InterruptedException e)
        {
            throw new IOException("Failed to acquire semaphore.", e);
        }
        try
        {
            return this.executor.execute(context);
        }
        finally
        {
            this.semaphore.release();
        }
    }


    public void quit() throws IOException
    {
        this.executor.quit();
        this.terminated = true;
    }
}
