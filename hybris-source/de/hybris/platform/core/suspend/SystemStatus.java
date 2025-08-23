package de.hybris.platform.core.suspend;

public enum SystemStatus
{
    RUNNING, WAITING_FOR_UPDATE, WAITING_FOR_SUSPEND, SUSPENDED;


    public boolean isRunningOrWaitingForUpdate()
    {
        return (RUNNING == this || WAITING_FOR_UPDATE == this);
    }
}
