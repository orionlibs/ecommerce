package de.hybris.platform.cronjob.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum DayOfWeek implements HybrisEnumValue
{
    SUNDAY("SUNDAY"),
    MONDAY("MONDAY"),
    TUESDAY("TUESDAY"),
    WEDNESDAY("WEDNESDAY"),
    THURSDAY("THURSDAY"),
    FRIDAY("FRIDAY"),
    SATURDAY("SATURDAY");
    public static final String _TYPECODE = "DayOfWeek";
    public static final String SIMPLE_CLASSNAME = "DayOfWeek";
    private final String code;


    DayOfWeek(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "DayOfWeek";
    }
}
