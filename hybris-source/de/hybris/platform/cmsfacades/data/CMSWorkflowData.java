package de.hybris.platform.cmsfacades.data;

import java.util.List;

public class CMSWorkflowData extends CMSCreateVersionData
{
    private String workflowCode;
    private String templateCode;
    private String description;
    private List<String> attachments;
    private String status;
    private List<String> statuses;
    private Boolean isAvailableForCurrentPrincipal;
    private String originalWorkflowCode;
    private List<CMSWorkflowActionData> actions;
    private Boolean canModifyItemInWorkflow;


    public void setWorkflowCode(String workflowCode)
    {
        this.workflowCode = workflowCode;
    }


    public String getWorkflowCode()
    {
        return this.workflowCode;
    }


    public void setTemplateCode(String templateCode)
    {
        this.templateCode = templateCode;
    }


    public String getTemplateCode()
    {
        return this.templateCode;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setAttachments(List<String> attachments)
    {
        this.attachments = attachments;
    }


    public List<String> getAttachments()
    {
        return this.attachments;
    }


    public void setStatus(String status)
    {
        this.status = status;
    }


    public String getStatus()
    {
        return this.status;
    }


    public void setStatuses(List<String> statuses)
    {
        this.statuses = statuses;
    }


    public List<String> getStatuses()
    {
        return this.statuses;
    }


    public void setIsAvailableForCurrentPrincipal(Boolean isAvailableForCurrentPrincipal)
    {
        this.isAvailableForCurrentPrincipal = isAvailableForCurrentPrincipal;
    }


    public Boolean getIsAvailableForCurrentPrincipal()
    {
        return this.isAvailableForCurrentPrincipal;
    }


    public void setOriginalWorkflowCode(String originalWorkflowCode)
    {
        this.originalWorkflowCode = originalWorkflowCode;
    }


    public String getOriginalWorkflowCode()
    {
        return this.originalWorkflowCode;
    }


    public void setActions(List<CMSWorkflowActionData> actions)
    {
        this.actions = actions;
    }


    public List<CMSWorkflowActionData> getActions()
    {
        return this.actions;
    }


    public void setCanModifyItemInWorkflow(Boolean canModifyItemInWorkflow)
    {
        this.canModifyItemInWorkflow = canModifyItemInWorkflow;
    }


    public Boolean getCanModifyItemInWorkflow()
    {
        return this.canModifyItemInWorkflow;
    }
}
