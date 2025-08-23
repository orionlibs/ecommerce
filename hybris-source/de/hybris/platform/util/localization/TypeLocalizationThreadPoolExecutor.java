package de.hybris.platform.util.localization;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TypeLocalizationThreadPoolExecutor extends ThreadPoolExecutor
{
    private final ThreadFactory factory = (ThreadFactory)new Object(this);


    protected TypeLocalizationThreadPoolExecutor(int corePoolSize, int maximumPoolSize)
    {
        super(corePoolSize, maximumPoolSize, 100L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    }


    public ThreadFactory getThreadFactory()
    {
        return this.factory;
    }
}
