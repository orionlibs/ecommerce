package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;

public class ItemData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String itemId;
    private String name;
    private String itemType;


    public void setItemId(String itemId)
    {
        this.itemId = itemId;
    }


    public String getItemId()
    {
        return this.itemId;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setItemType(String itemType)
    {
        this.itemType = itemType;
    }


    public String getItemType()
    {
        return this.itemType;
    }
}
