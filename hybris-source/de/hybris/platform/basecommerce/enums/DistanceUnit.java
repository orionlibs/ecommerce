package de.hybris.platform.basecommerce.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum DistanceUnit implements HybrisEnumValue
{
    MILES("miles"),
    KM("km");
    public static final String _TYPECODE = "DistanceUnit";
    public static final String SIMPLE_CLASSNAME = "DistanceUnit";
    private final String code;


    DistanceUnit(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "DistanceUnit";
    }
}
