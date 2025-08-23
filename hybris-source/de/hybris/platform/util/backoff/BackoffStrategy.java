package de.hybris.platform.util.backoff;

import java.time.Duration;

public interface BackoffStrategy
{
    Duration errorOccurred();


    void resetBackOffState();


    boolean shouldRetry();
}
