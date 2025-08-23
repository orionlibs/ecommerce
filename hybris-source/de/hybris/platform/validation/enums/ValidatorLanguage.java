package de.hybris.platform.validation.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ValidatorLanguage implements HybrisEnumValue
{
    BEANSHELL("BEANSHELL");
    private final String code;
    public static final String SIMPLE_CLASSNAME = "ValidatorLanguage";
    public static final String _TYPECODE = "ValidatorLanguage";


    ValidatorLanguage(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ValidatorLanguage";
    }
}
