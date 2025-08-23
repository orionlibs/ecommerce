package de.hybris.platform.core.suspend;

public interface RunningThread
{
    long getThreadId();


    String getThreadName();


    String getCategory();


    boolean isSuspendable();


    String getStatusInfo();


    Iterable<RunningThread> getChildThreads();
}
