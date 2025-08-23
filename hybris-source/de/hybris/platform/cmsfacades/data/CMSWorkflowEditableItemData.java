package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;

public class CMSWorkflowEditableItemData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String uid;
    private String uuid;
    private boolean editableByUser;
    private String editableInWorkflow;


    public void setUid(String uid)
    {
        this.uid = uid;
    }


    public String getUid()
    {
        return this.uid;
    }


    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }


    public String getUuid()
    {
        return this.uuid;
    }


    public void setEditableByUser(boolean editableByUser)
    {
        this.editableByUser = editableByUser;
    }


    public boolean isEditableByUser()
    {
        return this.editableByUser;
    }


    public void setEditableInWorkflow(String editableInWorkflow)
    {
        this.editableInWorkflow = editableInWorkflow;
    }


    public String getEditableInWorkflow()
    {
        return this.editableInWorkflow;
    }
}
