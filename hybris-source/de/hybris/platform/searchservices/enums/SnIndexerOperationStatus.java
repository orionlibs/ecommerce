package de.hybris.platform.searchservices.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum SnIndexerOperationStatus implements HybrisEnumValue
{
    RUNNING("RUNNING"),
    COMPLETED("COMPLETED"),
    ABORTED("ABORTED"),
    FAILED("FAILED");
    public static final String _TYPECODE = "SnIndexerOperationStatus";
    public static final String SIMPLE_CLASSNAME = "SnIndexerOperationStatus";
    private final String code;


    SnIndexerOperationStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SnIndexerOperationStatus";
    }
}
