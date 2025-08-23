package de.hybris.platform.cronjob.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ErrorMode implements HybrisEnumValue
{
    FAIL("FAIL"),
    PAUSE("PAUSE"),
    IGNORE("IGNORE");
    public static final String _TYPECODE = "ErrorMode";
    public static final String SIMPLE_CLASSNAME = "ErrorMode";
    private final String code;


    ErrorMode(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ErrorMode";
    }
}
