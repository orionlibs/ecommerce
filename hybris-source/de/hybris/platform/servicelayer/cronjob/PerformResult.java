package de.hybris.platform.servicelayer.cronjob;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;

public class PerformResult
{
    private final CronJobResult result;
    private final CronJobStatus status;


    public PerformResult(CronJobResult result, CronJobStatus status)
    {
        this.result = result;
        this.status = status;
    }


    public CronJobResult getResult()
    {
        return this.result;
    }


    public CronJobStatus getStatus()
    {
        return this.status;
    }


    public String toString()
    {
        return "PerformResult[Cronjob Result: " + getResult() + " Cronjob Status: " + getStatus() + "]";
    }
}
