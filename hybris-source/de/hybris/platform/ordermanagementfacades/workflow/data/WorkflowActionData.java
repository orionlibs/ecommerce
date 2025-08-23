package de.hybris.platform.ordermanagementfacades.workflow.data;

import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class WorkflowActionData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private String comment;
    private String description;
    private Date creationTime;
    private String workflowCode;
    private WorkflowActionStatus status;
    private List<WorkflowActionAttachmentItemData> attachmentItems;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setComment(String comment)
    {
        this.comment = comment;
    }


    public String getComment()
    {
        return this.comment;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setCreationTime(Date creationTime)
    {
        this.creationTime = creationTime;
    }


    public Date getCreationTime()
    {
        return this.creationTime;
    }


    public void setWorkflowCode(String workflowCode)
    {
        this.workflowCode = workflowCode;
    }


    public String getWorkflowCode()
    {
        return this.workflowCode;
    }


    public void setStatus(WorkflowActionStatus status)
    {
        this.status = status;
    }


    public WorkflowActionStatus getStatus()
    {
        return this.status;
    }


    public void setAttachmentItems(List<WorkflowActionAttachmentItemData> attachmentItems)
    {
        this.attachmentItems = attachmentItems;
    }


    public List<WorkflowActionAttachmentItemData> getAttachmentItems()
    {
        return this.attachmentItems;
    }
}
