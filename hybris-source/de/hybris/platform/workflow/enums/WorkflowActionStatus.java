package de.hybris.platform.workflow.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum WorkflowActionStatus implements HybrisEnumValue
{
    PENDING("pending"),
    IN_PROGRESS("in_progress"),
    PAUSED("paused"),
    COMPLETED("completed"),
    DISABLED("disabled"),
    ENDED_THROUGH_END_OF_WORKFLOW("ended_through_end_of_workflow"),
    TERMINATED("terminated");
    public static final String _TYPECODE = "WorkflowActionStatus";
    public static final String SIMPLE_CLASSNAME = "WorkflowActionStatus";
    private final String code;


    WorkflowActionStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "WorkflowActionStatus";
    }
}
