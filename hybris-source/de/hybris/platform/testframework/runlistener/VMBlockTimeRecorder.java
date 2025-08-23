package de.hybris.platform.testframework.runlistener;

import de.hybris.platform.core.threadregistry.RegistrableThread;

public final class VMBlockTimeRecorder extends RegistrableThread
{
    private final long tickDelay;
    private volatile long lastTickMS = -1L;
    private volatile long maxDelta = -1L;
    private static volatile VMBlockTimeRecorder SINGLETON = new VMBlockTimeRecorder(1000L);

    static
    {
        if(Boolean.TRUE.toString().equalsIgnoreCase(System.getenv("platform.blocktime.recorder")))
        {
            SINGLETON.start();
        }
    }

    public static void ensureRunning()
    {
        if(!SINGLETON.isAlive())
        {
            SINGLETON.start();
        }
    }


    public static void ensureNotRunning()
    {
        if(SINGLETON.isAlive())
        {
            SINGLETON.interrupt();
        }
    }


    public static long getMaxBlockTimeMillis()
    {
        long max = SINGLETON.maxDelta;
        if(max > -1L)
        {
            return max - SINGLETON.tickDelay;
        }
        return -1L;
    }


    public static void reset()
    {
        SINGLETON.resetStats();
    }


    private VMBlockTimeRecorder(long tickDelay)
    {
        super("VMBlockTimeRecorder");
        setDaemon(true);
        this.tickDelay = tickDelay;
    }


    public void internalRun()
    {
        init();
        do
        {
            tick();
            sleep();
        }
        while(!isInterrupted());
        resetStats();
    }


    private void resetStats()
    {
        this.lastTickMS = -1L;
        this.maxDelta = -1L;
    }


    private void init()
    {
        this.lastTickMS = System.currentTimeMillis();
    }


    private void tick()
    {
        long tick = System.currentTimeMillis();
        if(this.lastTickMS > -1L)
        {
            long delta = tick - this.lastTickMS;
            this.maxDelta = Math.max(this.maxDelta, delta);
        }
        this.lastTickMS = tick;
    }


    private void sleep()
    {
        try
        {
            sleep(this.tickDelay);
        }
        catch(InterruptedException e)
        {
            interrupt();
        }
    }
}
