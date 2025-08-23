package de.hybris.platform.util.logging;

public interface HybrisLogFilter
{
    HybrisLoggingEvent filterEvent(HybrisLoggingEvent paramHybrisLoggingEvent);
}
