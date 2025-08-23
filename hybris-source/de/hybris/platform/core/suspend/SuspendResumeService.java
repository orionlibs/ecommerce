package de.hybris.platform.core.suspend;

public interface SuspendResumeService
{
    SuspendResult suspend(SuspendOptions paramSuspendOptions);


    void resume(ResumeOptions paramResumeOptions);


    SystemState getSystemState();


    SystemStatus getSystemStatus();


    String getResumeToken();
}
