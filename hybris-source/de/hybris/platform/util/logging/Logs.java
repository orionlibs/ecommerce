package de.hybris.platform.util.logging;

import java.util.function.Supplier;
import org.apache.log4j.Logger;

public final class Logs
{
    public static void debug(Logger log, Supplier logLineSupplier)
    {
        if(log.isDebugEnabled())
        {
            log.debug(logLineSupplier.get());
        }
    }


    public static void debug(Logger log, Supplier logLineSupplier, Throwable throwable)
    {
        if(log.isDebugEnabled())
        {
            log.debug(logLineSupplier.get(), throwable);
        }
    }
}
