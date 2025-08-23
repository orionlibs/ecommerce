package de.hybris.platform.workflow.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum WorkflowActionType implements HybrisEnumValue
{
    START("start"),
    END("end"),
    NORMAL("normal");
    public static final String _TYPECODE = "WorkflowActionType";
    public static final String SIMPLE_CLASSNAME = "WorkflowActionType";
    private final String code;


    WorkflowActionType(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "WorkflowActionType";
    }
}
