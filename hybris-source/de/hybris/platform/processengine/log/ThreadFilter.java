package de.hybris.platform.processengine.log;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

public class ThreadFilter extends Filter
{
    private final String threadName;


    public ThreadFilter(Thread thread)
    {
        this.threadName = thread.getName();
    }


    public int decide(LoggingEvent event)
    {
        if(event.getThreadName().equals(this.threadName))
        {
            return 1;
        }
        return -1;
    }
}
