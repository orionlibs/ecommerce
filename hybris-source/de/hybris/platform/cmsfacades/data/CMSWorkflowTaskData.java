package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.List;

public class CMSWorkflowTaskData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private CMSWorkflowActionData action;
    private List<CMSWorkflowAttachmentData> attachments;


    public void setAction(CMSWorkflowActionData action)
    {
        this.action = action;
    }


    public CMSWorkflowActionData getAction()
    {
        return this.action;
    }


    public void setAttachments(List<CMSWorkflowAttachmentData> attachments)
    {
        this.attachments = attachments;
    }


    public List<CMSWorkflowAttachmentData> getAttachments()
    {
        return this.attachments;
    }
}
