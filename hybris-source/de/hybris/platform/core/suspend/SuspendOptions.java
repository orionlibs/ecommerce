package de.hybris.platform.core.suspend;

import java.util.Optional;

public class SuspendOptions
{
    private final boolean shutdownWhenSuspended;
    private final boolean waitForSuspend;
    private final Optional<Long> suspendingThreadId;


    private SuspendOptions(Builder builder)
    {
        this.shutdownWhenSuspended = builder.shutdownWhenSuspended;
        this.waitForSuspend = builder.waitForSuspend;
        this.suspendingThreadId = this.waitForSuspend ? Optional.<Long>of(Long.valueOf(builder.suspendingThreadId)) : Optional.<Long>empty();
    }


    public static SuspendOptions defaultOptions()
    {
        return builder().build();
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public boolean isShutdownWhenSuspended()
    {
        return this.shutdownWhenSuspended;
    }


    public boolean isWaitForSuspend()
    {
        return this.waitForSuspend;
    }


    public Optional<Long> getSuspendingThreadId()
    {
        return this.suspendingThreadId;
    }
}
