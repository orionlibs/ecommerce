package de.hybris.platform.servicelayer.event.events;

import de.hybris.platform.core.PK;
import java.io.Serializable;

public abstract class AbstractCronJobPerformEvent extends AbstractCronJobEvent
{
    private boolean scheduled;
    private PK scheduledByTriggerPk;
    private boolean synchronous;


    public AbstractCronJobPerformEvent()
    {
    }


    public AbstractCronJobPerformEvent(Serializable source)
    {
        super(source);
    }


    public void setScheduled(boolean scheduled)
    {
        this.scheduled = scheduled;
    }


    public boolean isScheduled()
    {
        return this.scheduled;
    }


    public void setScheduledByTriggerPk(PK scheduledByTriggerPk)
    {
        this.scheduledByTriggerPk = scheduledByTriggerPk;
    }


    public PK getScheduledByTriggerPk()
    {
        return this.scheduledByTriggerPk;
    }


    public void setSynchronous(boolean synchronous)
    {
        this.synchronous = synchronous;
    }


    public boolean isSynchronous()
    {
        return this.synchronous;
    }
}
