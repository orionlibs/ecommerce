package de.hybris.platform.servicelayer.event.events;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import java.io.Serializable;

public class AfterCronJobFinishedEvent extends AbstractCronJobPerformEvent
{
    private CronJobResult result;
    private CronJobStatus status;


    public AfterCronJobFinishedEvent()
    {
    }


    public AfterCronJobFinishedEvent(Serializable source)
    {
        super(source);
    }


    public void setResult(CronJobResult result)
    {
        this.result = result;
    }


    public CronJobResult getResult()
    {
        return this.result;
    }


    public void setStatus(CronJobStatus status)
    {
        this.status = status;
    }


    public CronJobStatus getStatus()
    {
        return this.status;
    }
}
