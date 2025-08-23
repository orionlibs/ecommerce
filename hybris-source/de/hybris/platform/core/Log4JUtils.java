package de.hybris.platform.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Log4JUtils
{
    private static final String LOG4J_GUARD = "LOG4J_GUARD".intern();
    private static final Object STARTUP_LOCK = new Object();
    private static volatile boolean loaded = false;
    private static final Logger LOG = LoggerFactory.getLogger(Log4JUtils.class);


    public static void startup()
    {
        if(!loaded)
        {
            synchronized(STARTUP_LOCK)
            {
                if(!loaded)
                {
                    loaded = true;
                }
            }
        }
        LOG.info("Log4j initialisation has been finished");
    }


    public static synchronized void shutdown()
    {
    }
}
