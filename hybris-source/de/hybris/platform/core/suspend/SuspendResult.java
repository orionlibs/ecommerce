package de.hybris.platform.core.suspend;

public interface SuspendResult
{
    SystemStatus getCurrentStatus();


    String getResumeToken();
}
