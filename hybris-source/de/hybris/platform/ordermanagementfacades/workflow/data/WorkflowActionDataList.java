package de.hybris.platform.ordermanagementfacades.workflow.data;

import java.io.Serializable;
import java.util.List;

public class WorkflowActionDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<WorkflowActionData> workflowActions;


    public void setWorkflowActions(List<WorkflowActionData> workflowActions)
    {
        this.workflowActions = workflowActions;
    }


    public List<WorkflowActionData> getWorkflowActions()
    {
        return this.workflowActions;
    }
}
