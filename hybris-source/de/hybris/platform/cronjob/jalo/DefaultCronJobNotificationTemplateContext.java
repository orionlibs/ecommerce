package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.util.Utilities;
import java.util.Date;

public class DefaultCronJobNotificationTemplateContext implements CronJobNotificationTemplateContext
{
    private final CronJob cronJob;


    public DefaultCronJobNotificationTemplateContext(CronJob cronJob)
    {
        this.cronJob = cronJob;
    }


    public String getCronJobName()
    {
        return this.cronJob.getCode();
    }


    public String getEndDate()
    {
        Date end = this.cronJob.getEndTime();
        return (end != null) ? Utilities.getDateTimeInstance().format(end) : "n/a";
    }


    public String getDuration()
    {
        Date start = this.cronJob.getStartTime();
        Date end = this.cronJob.getEndTime();
        return (start != null && end != null) ? Utilities.formatTime(end.getTime() - start.getTime()) : "n/a";
    }


    public String getResult()
    {
        EnumerationValue res = this.cronJob.getResult();
        return (res != null) ? res.getCode() : "n/a";
    }


    public String getStartDate()
    {
        Date start = this.cronJob.getStartTime();
        return (start != null) ? Utilities.getDateTimeInstance().format(start) : null;
    }


    public String getStatus()
    {
        EnumerationValue status = this.cronJob.getStatus();
        return (status != null) ? status.getCode() : "n/a";
    }
}
