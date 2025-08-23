package de.hybris.platform.hac.data.dto;

public class CronJobData
{
    private final String cronJobCode;
    private final String jobCode;
    private final String progress;


    public CronJobData(String cronJobCode, String jobCode, String progress)
    {
        this.cronJobCode = cronJobCode;
        this.jobCode = jobCode;
        this.progress = progress;
    }


    public String getCronJobCode()
    {
        return this.cronJobCode;
    }


    public String getJobCode()
    {
        return this.jobCode;
    }


    public String getProgress()
    {
        return this.progress;
    }
}
