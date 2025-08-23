package de.hybris.platform.validation.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum Severity implements HybrisEnumValue
{
    ERROR("ERROR"),
    WARN("WARN"),
    INFO("INFO");
    public static final String _TYPECODE = "Severity";
    public static final String SIMPLE_CLASSNAME = "Severity";
    private final String code;


    Severity(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "Severity";
    }
}
