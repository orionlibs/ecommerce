package de.hybris.platform.workflow.mail;

public interface WorkflowMailContext extends Cloneable
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


    Object clone() throws CloneNotSupportedException;
}
