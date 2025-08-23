package de.hybris.platform.servicelayer.cronjob;

import java.util.Set;

public class CronJobHistoryInclude
{
    private Set<String> jobCodes;
    private String jobTypeCode;
    private String cronJobTypeCode;


    public CronJobHistoryInclude(Set<String> jobCodes, String jobTypeCode, String cronJobTypeCode)
    {
        this.jobCodes = jobCodes;
        this.jobTypeCode = jobTypeCode;
        this.cronJobTypeCode = cronJobTypeCode;
    }


    public CronJobHistoryInclude()
    {
    }


    public Set<String> getJobCodes()
    {
        return this.jobCodes;
    }


    public void setJobCodes(Set<String> jobCodes)
    {
        this.jobCodes = jobCodes;
    }


    public String getJobTypeCode()
    {
        return this.jobTypeCode;
    }


    public void setJobTypeCode(String jobTypeCode)
    {
        this.jobTypeCode = jobTypeCode;
    }


    public String getCronJobTypeCode()
    {
        return this.cronJobTypeCode;
    }


    public void setCronJobTypeCode(String cronJobTypeCode)
    {
        this.cronJobTypeCode = cronJobTypeCode;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof CronJobHistoryInclude))
        {
            return false;
        }
        CronJobHistoryInclude include = (CronJobHistoryInclude)o;
        if((this.jobCodes != null) ? !this.jobCodes.equals(include.jobCodes) : (include.jobCodes != null))
        {
            return false;
        }
        if((this.jobTypeCode != null) ? !this.jobTypeCode.equals(include.jobTypeCode) : (include.jobTypeCode != null))
        {
            return false;
        }
        return (this.cronJobTypeCode != null) ? this.cronJobTypeCode.equals(include.cronJobTypeCode) : ((include.cronJobTypeCode == null));
    }


    public int hashCode()
    {
        int result = (this.jobCodes != null) ? this.jobCodes.hashCode() : 0;
        result = 31 * result + ((this.jobTypeCode != null) ? this.jobTypeCode.hashCode() : 0);
        result = 31 * result + ((this.cronJobTypeCode != null) ? this.cronJobTypeCode.hashCode() : 0);
        return result;
    }
}
