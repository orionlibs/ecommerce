package de.hybris.platform.sap.core.configuration.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum HTTPAuthenticationType implements HybrisEnumValue
{
    NO_AUTHENTICATION("NO_AUTHENTICATION"),
    BASIC_AUTHENTICATION("BASIC_AUTHENTICATION");
    public static final String _TYPECODE = "HTTPAuthenticationType";
    public static final String SIMPLE_CLASSNAME = "HTTPAuthenticationType";
    private final String code;


    HTTPAuthenticationType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "HTTPAuthenticationType";
    }
}
