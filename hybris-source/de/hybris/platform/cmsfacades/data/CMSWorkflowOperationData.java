package de.hybris.platform.cmsfacades.data;

import de.hybris.platform.cmsfacades.enums.CMSWorkflowOperation;

public class CMSWorkflowOperationData extends CMSCreateVersionData
{
    private CMSWorkflowOperation operation;
    private String actionCode;
    private String decisionCode;
    private String comment;


    public void setOperation(CMSWorkflowOperation operation)
    {
        this.operation = operation;
    }


    public CMSWorkflowOperation getOperation()
    {
        return this.operation;
    }


    public void setActionCode(String actionCode)
    {
        this.actionCode = actionCode;
    }


    public String getActionCode()
    {
        return this.actionCode;
    }


    public void setDecisionCode(String decisionCode)
    {
        this.decisionCode = decisionCode;
    }


    public String getDecisionCode()
    {
        return this.decisionCode;
    }


    public void setComment(String comment)
    {
        this.comment = comment;
    }


    public String getComment()
    {
        return this.comment;
    }
}
