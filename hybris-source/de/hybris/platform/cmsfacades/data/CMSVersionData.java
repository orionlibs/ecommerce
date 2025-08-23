package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.Date;

public class CMSVersionData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String uid;
    private String itemUUID;
    private String label;
    private String description;
    private Date creationtime;


    public void setUid(String uid)
    {
        this.uid = uid;
    }


    public String getUid()
    {
        return this.uid;
    }


    public void setItemUUID(String itemUUID)
    {
        this.itemUUID = itemUUID;
    }


    public String getItemUUID()
    {
        return this.itemUUID;
    }


    public void setLabel(String label)
    {
        this.label = label;
    }


    public String getLabel()
    {
        return this.label;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setCreationtime(Date creationtime)
    {
        this.creationtime = creationtime;
    }


    public Date getCreationtime()
    {
        return this.creationtime;
    }
}
