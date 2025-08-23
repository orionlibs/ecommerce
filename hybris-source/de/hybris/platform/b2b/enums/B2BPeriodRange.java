package de.hybris.platform.b2b.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum B2BPeriodRange implements HybrisEnumValue
{
    DAY("DAY"),
    WEEK("WEEK"),
    MONTH("MONTH"),
    QUARTER("QUARTER"),
    YEAR("YEAR");
    public static final String _TYPECODE = "B2BPeriodRange";
    public static final String SIMPLE_CLASSNAME = "B2BPeriodRange";
    private final String code;


    B2BPeriodRange(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "B2BPeriodRange";
    }
}
