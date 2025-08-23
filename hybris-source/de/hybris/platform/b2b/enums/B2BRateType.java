package de.hybris.platform.b2b.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum B2BRateType implements HybrisEnumValue
{
    CURRENCY("CURRENCY"),
    PERCENTAGE("PERCENTAGE");
    public static final String _TYPECODE = "B2BRateType";
    public static final String SIMPLE_CLASSNAME = "B2BRateType";
    private final String code;


    B2BRateType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "B2BRateType";
    }
}
