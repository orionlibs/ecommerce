package de.hybris.platform.ruleengine.concurrency;

public interface SuspendResumeTaskManager
{
    void registerAsNonSuspendableTask(Thread paramThread, String paramString);


    void registerAsSuspendableTask(Thread paramThread, String paramString);


    boolean isSystemRunning();
}
