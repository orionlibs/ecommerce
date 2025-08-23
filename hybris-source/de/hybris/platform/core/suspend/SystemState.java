package de.hybris.platform.core.suspend;

public interface SystemState
{
    SystemStatus getStatus();


    Iterable<RunningThread> getRootThreads();
}
