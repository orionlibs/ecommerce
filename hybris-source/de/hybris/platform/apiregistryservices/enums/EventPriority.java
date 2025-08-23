package de.hybris.platform.apiregistryservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum EventPriority implements HybrisEnumValue
{
    CRITICAL("CRITICAL"),
    HIGH("HIGH"),
    MEDIUM("MEDIUM"),
    LOW("LOW");
    public static final String _TYPECODE = "EventPriority";
    public static final String SIMPLE_CLASSNAME = "EventPriority";
    private final String code;


    EventPriority(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "EventPriority";
    }
}
