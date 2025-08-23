package de.hybris.platform.apiregistryservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum DestinationChannel implements HybrisEnumValue
{
    DEFAULT("DEFAULT");
    private final String code;
    public static final String SIMPLE_CLASSNAME = "DestinationChannel";
    public static final String _TYPECODE = "DestinationChannel";


    DestinationChannel(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "DestinationChannel";
    }
}
