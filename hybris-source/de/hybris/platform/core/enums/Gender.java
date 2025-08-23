package de.hybris.platform.core.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum Gender implements HybrisEnumValue
{
    MALE("MALE"),
    FEMALE("FEMALE");
    public static final String _TYPECODE = "Gender";
    public static final String SIMPLE_CLASSNAME = "Gender";
    private final String code;


    Gender(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "Gender";
    }
}
