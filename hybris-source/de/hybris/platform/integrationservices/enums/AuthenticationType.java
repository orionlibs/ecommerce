package de.hybris.platform.integrationservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum AuthenticationType implements HybrisEnumValue
{
    BASIC("BASIC"),
    OAUTH("OAUTH");
    public static final String _TYPECODE = "AuthenticationType";
    public static final String SIMPLE_CLASSNAME = "AuthenticationType";
    private final String code;


    AuthenticationType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "AuthenticationType";
    }
}
