package de.hybris.platform.util;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.threadpool.ThreadPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Perf
{
    private final AtomicLong cnt = new AtomicLong(0L);
    private final AtomicInteger msRun = new AtomicInteger(0);
    private final ThreadPool pool;
    private final PerfThread[] threads;


    public abstract void body() throws Exception;


    public Perf(int poolThreads)
    {
        this(poolThreads, true);
    }


    public Perf(int poolThreads, boolean useJalo)
    {
        if(useJalo)
        {
            this.pool = (ThreadPool)new TestThreadPool(Registry.getCurrentTenant().getTenantID(), poolThreads);
        }
        else
        {
            this.pool = (ThreadPool)new TestThreadPool(null, poolThreads);
        }
        this.threads = new PerfThread[poolThreads];
        for(int i = 0; i < poolThreads; i++)
        {
            this.threads[i] = new PerfThread(this, useJalo);
        }
    }


    public void go(int ms) throws Exception
    {
        go(ms, this.threads.length);
    }


    public void go(int ms, int threadCount) throws Exception
    {
        int maxThreads = this.threads.length;
        if(threadCount > maxThreads)
        {
            throw new IllegalArgumentException("thread count " + threadCount + " exceeds available threads of " + maxThreads);
        }
        this.msRun.set(ms);
        int i;
        for(i = 0; i < threadCount; i++)
        {
            this.pool.borrowThread().execute((Runnable)this.threads[i]);
        }
        for(i = 0; i < threadCount; i++)
        {
            while(!this.threads[i].isFinished())
            {
                Thread.sleep(10L);
            }
            if((this.threads[i]).throwable != null)
            {
                throw (this.threads[i]).throwable;
            }
        }
    }


    public void close()
    {
        try
        {
            this.pool.close();
        }
        catch(Exception e)
        {
            throw new RuntimeException();
        }
    }


    public long getExecutions()
    {
        return this.cnt.get();
    }


    public void reset()
    {
        this.cnt.set(0L);
    }


    int getMsPerRun()
    {
        return this.msRun.get();
    }


    void addExecution()
    {
        this.cnt.incrementAndGet();
    }
}
