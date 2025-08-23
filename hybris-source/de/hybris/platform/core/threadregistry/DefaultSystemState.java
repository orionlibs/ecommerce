package de.hybris.platform.core.threadregistry;

import com.google.common.collect.ImmutableList;
import de.hybris.platform.core.suspend.RunningThread;
import de.hybris.platform.core.suspend.SystemState;
import de.hybris.platform.core.suspend.SystemStatus;

class DefaultSystemState implements SystemState
{
    private final SystemStatus status;
    private final ImmutableList<RunningThread> rootThreads;


    private DefaultSystemState(Builder builder)
    {
        this.status = builder.status;
        this.rootThreads = builder.getRootThreads();
    }


    public SystemStatus getStatus()
    {
        return this.status;
    }


    public Iterable<RunningThread> getRootThreads()
    {
        return (Iterable<RunningThread>)this.rootThreads;
    }


    public static Builder builder()
    {
        return new Builder();
    }
}
