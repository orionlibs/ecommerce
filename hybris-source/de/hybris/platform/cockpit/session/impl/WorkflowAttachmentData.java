package de.hybris.platform.cockpit.session.impl;

import java.util.List;

public class WorkflowAttachmentData
{
    private List<String> attachmentTypes;


    public void setAttachmentTypes(List<String> attachmentTypes)
    {
        this.attachmentTypes = attachmentTypes;
    }


    public List<String> getAttachmentTypes()
    {
        return this.attachmentTypes;
    }
}
