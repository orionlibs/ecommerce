package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;

public class NavigationEntryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String name;
    private String itemId;
    private String itemType;
    private String itemSuperType;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setItemId(String itemId)
    {
        this.itemId = itemId;
    }


    public String getItemId()
    {
        return this.itemId;
    }


    public void setItemType(String itemType)
    {
        this.itemType = itemType;
    }


    public String getItemType()
    {
        return this.itemType;
    }


    public void setItemSuperType(String itemSuperType)
    {
        this.itemSuperType = itemSuperType;
    }


    public String getItemSuperType()
    {
        return this.itemSuperType;
    }
}
