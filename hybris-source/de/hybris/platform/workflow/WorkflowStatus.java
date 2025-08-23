package de.hybris.platform.workflow;

import java.util.EnumSet;

public enum WorkflowStatus
{
    PLANNED, RUNNING, FINISHED, TERMINATED;


    public static EnumSet<WorkflowStatus> getAll()
    {
        return EnumSet.of(PLANNED, RUNNING, FINISHED, TERMINATED);
    }
}
