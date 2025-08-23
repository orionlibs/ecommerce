package de.hybris.platform.cronjob.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum CronJobStatus implements HybrisEnumValue
{
    RUNNINGRESTART("RUNNINGRESTART"),
    RUNNING("RUNNING"),
    PAUSED("PAUSED"),
    FINISHED("FINISHED"),
    ABORTED("ABORTED"),
    UNKNOWN("UNKNOWN");
    public static final String _TYPECODE = "CronJobStatus";
    public static final String SIMPLE_CLASSNAME = "CronJobStatus";
    private final String code;


    CronJobStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "CronJobStatus";
    }
}
