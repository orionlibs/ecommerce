package de.hybris.platform.processengine.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum ProcessState implements HybrisEnumValue
{
    CREATED("CREATED"),
    RUNNING("RUNNING"),
    WAITING("WAITING"),
    SUCCEEDED("SUCCEEDED"),
    FAILED("FAILED"),
    ERROR("ERROR");
    public static final String _TYPECODE = "ProcessState";
    public static final String SIMPLE_CLASSNAME = "ProcessState";
    private final String code;


    ProcessState(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "ProcessState";
    }
}
