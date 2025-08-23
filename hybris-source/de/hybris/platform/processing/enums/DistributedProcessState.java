package de.hybris.platform.processing.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum DistributedProcessState implements HybrisEnumValue
{
    CREATED("CREATED"),
    INITIALIZING("INITIALIZING"),
    SCHEDULING_EXECUTION("SCHEDULING_EXECUTION"),
    WAITING_FOR_EXECUTION("WAITING_FOR_EXECUTION"),
    SUCCEEDED("SUCCEEDED"),
    FAILED("FAILED"),
    STOPPED("STOPPED");
    public static final String _TYPECODE = "DistributedProcessState";
    public static final String SIMPLE_CLASSNAME = "DistributedProcessState";
    private final String code;


    DistributedProcessState(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "DistributedProcessState";
    }
}
