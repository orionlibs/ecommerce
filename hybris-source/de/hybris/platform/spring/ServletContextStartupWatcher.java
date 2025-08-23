package de.hybris.platform.spring;

import java.util.concurrent.atomic.AtomicInteger;

public class ServletContextStartupWatcher
{
    private static final ServletContextStartupWatcher STARTUP_HOLDER = new ServletContextStartupWatcher();
    private final AtomicInteger contextsInitializationInProgressCounter = new AtomicInteger(0);


    public static ServletContextStartupWatcher getStartupServletContextWatcher()
    {
        return STARTUP_HOLDER;
    }


    StartingContext newServletContextStarted()
    {
        StartingContext sCtx = new StartingContext(this);
        this.contextsInitializationInProgressCounter.incrementAndGet();
        return sCtx;
    }


    public boolean isAnyServletContextInitializationInProgress()
    {
        return (this.contextsInitializationInProgressCounter.get() > 0);
    }
}
