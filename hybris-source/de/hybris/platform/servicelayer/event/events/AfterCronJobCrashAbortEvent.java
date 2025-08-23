package de.hybris.platform.servicelayer.event.events;

import java.io.Serializable;

public class AfterCronJobCrashAbortEvent extends AbstractCronJobEvent
{
    public AfterCronJobCrashAbortEvent()
    {
    }


    public AfterCronJobCrashAbortEvent(Serializable source)
    {
        super(source);
    }
}
