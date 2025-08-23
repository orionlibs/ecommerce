package de.hybris.platform.cronjob.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum CronJobResult implements HybrisEnumValue
{
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE"),
    ERROR("ERROR"),
    UNKNOWN("UNKNOWN");
    public static final String _TYPECODE = "CronJobResult";
    public static final String SIMPLE_CLASSNAME = "CronJobResult";
    private final String code;


    CronJobResult(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "CronJobResult";
    }
}
