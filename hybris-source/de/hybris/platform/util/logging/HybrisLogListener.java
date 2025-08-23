package de.hybris.platform.util.logging;

import org.apache.log4j.Level;

public interface HybrisLogListener
{
    boolean isEnabledFor(Level paramLevel);


    void log(HybrisLoggingEvent paramHybrisLoggingEvent);
}
