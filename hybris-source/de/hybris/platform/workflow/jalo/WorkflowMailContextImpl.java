package de.hybris.platform.workflow.jalo;

@Deprecated(since = "ages", forRemoval = false)
public class WorkflowMailContextImpl implements WorkflowMailContext
{
    private String fromEmailAddress = null;
    private String toEmailAddress = null;
    private String assigneeName = null;
    private String attachmentName = null;
    private String attachmentPK = null;


    public String getFromEmailAddress()
    {
        return this.fromEmailAddress;
    }


    public String getToEmailAddresses()
    {
        return this.toEmailAddress;
    }


    public String getAssigneeName()
    {
        return this.assigneeName;
    }


    public String getAttachmentName()
    {
        return this.attachmentName;
    }


    public String getAttachmentPK()
    {
        return this.attachmentPK;
    }


    public void setFromEmailAddress(String fromAddress)
    {
        this.fromEmailAddress = fromAddress;
    }


    public void setToEmailAddress(String toAddress)
    {
        this.toEmailAddress = toAddress;
    }


    public void setAssigneeName(String assigneeName)
    {
        this.assigneeName = assigneeName;
    }


    public void setAttachmentName(String attachmentName)
    {
        this.attachmentName = attachmentName;
    }


    public void setAttachmentPK(String attachmentPK)
    {
        this.attachmentPK = attachmentPK;
    }
}
