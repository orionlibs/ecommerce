package de.hybris.platform.cms2.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ABTestScopes implements HybrisEnumValue
{
    REQUEST("request"),
    SESSION("session");
    public static final String _TYPECODE = "ABTestScopes";
    public static final String SIMPLE_CLASSNAME = "ABTestScopes";
    private final String code;


    ABTestScopes(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ABTestScopes";
    }
}
