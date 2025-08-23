package de.hybris.platform.searchservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum SnIndexerOperationType implements HybrisEnumValue
{
    FULL("FULL"),
    INCREMENTAL("INCREMENTAL");
    public static final String _TYPECODE = "SnIndexerOperationType";
    public static final String SIMPLE_CLASSNAME = "SnIndexerOperationType";
    private final String code;


    SnIndexerOperationType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SnIndexerOperationType";
    }
}
