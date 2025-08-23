package de.hybris.platform.workflow.jalo;

@Deprecated(since = "ages", forRemoval = false)
public interface WorkflowMailContext
{
    String getFromEmailAddress();


    String getToEmailAddresses();


    String getAssigneeName();


    String getAttachmentName();


    String getAttachmentPK();


    void setFromEmailAddress(String paramString);


    void setToEmailAddress(String paramString);


    void setAssigneeName(String paramString);


    void setAttachmentName(String paramString);


    void setAttachmentPK(String paramString);
}
