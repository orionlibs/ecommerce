package de.hybris.platform.basecommerce.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum WeekDay implements HybrisEnumValue
{
    SUNDAY("SUNDAY"),
    MONDAY("MONDAY"),
    TUESDAY("TUESDAY"),
    WEDNESDAY("WEDNESDAY"),
    THURSDAY("THURSDAY"),
    FRIDAY("FRIDAY"),
    SATURDAY("SATURDAY");
    public static final String _TYPECODE = "WeekDay";
    public static final String SIMPLE_CLASSNAME = "WeekDay";
    private final String code;


    WeekDay(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "WeekDay";
    }
}
