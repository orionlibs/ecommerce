package de.hybris.platform.cronjob.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum JobLogLevel implements HybrisEnumValue
{
    DEBUG("DEBUG"),
    INFO("INFO"),
    WARNING("WARNING"),
    ERROR("ERROR"),
    FATAL("FATAL"),
    UNKNOWN("UNKNOWN");
    public static final String _TYPECODE = "JobLogLevel";
    public static final String SIMPLE_CLASSNAME = "JobLogLevel";
    private final String code;


    JobLogLevel(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "JobLogLevel";
    }
}
