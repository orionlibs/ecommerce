package de.hybris.platform.apiregistryservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum EventMappingType implements HybrisEnumValue
{
    GENERIC("GENERIC"),
    BEAN("BEAN"),
    PROCESS("PROCESS");
    public static final String _TYPECODE = "EventMappingType";
    public static final String SIMPLE_CLASSNAME = "EventMappingType";
    private final String code;


    EventMappingType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "EventMappingType";
    }
}
