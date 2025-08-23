package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;

@Deprecated(since = "1811", forRemoval = true)
public class NavigationEntryTypeData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String itemType;


    public void setItemType(String itemType)
    {
        this.itemType = itemType;
    }


    public String getItemType()
    {
        return this.itemType;
    }
}
