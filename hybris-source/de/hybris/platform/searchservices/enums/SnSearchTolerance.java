package de.hybris.platform.searchservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum SnSearchTolerance implements HybrisEnumValue
{
    BASIC("BASIC"),
    MEDIUM("MEDIUM"),
    RELAXED("RELAXED");
    public static final String _TYPECODE = "SnSearchTolerance";
    public static final String SIMPLE_CLASSNAME = "SnSearchTolerance";
    private final String code;


    SnSearchTolerance(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SnSearchTolerance";
    }
}
