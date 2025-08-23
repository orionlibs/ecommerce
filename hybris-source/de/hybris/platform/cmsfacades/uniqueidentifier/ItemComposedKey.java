package de.hybris.platform.cmsfacades.uniqueidentifier;

import java.io.Serializable;

public class ItemComposedKey implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String itemId;
    private String catalogId;
    private String catalogVersion;


    public void setItemId(String itemId)
    {
        this.itemId = itemId;
    }


    public String getItemId()
    {
        return this.itemId;
    }


    public void setCatalogId(String catalogId)
    {
        this.catalogId = catalogId;
    }


    public String getCatalogId()
    {
        return this.catalogId;
    }


    public void setCatalogVersion(String catalogVersion)
    {
        this.catalogVersion = catalogVersion;
    }


    public String getCatalogVersion()
    {
        return this.catalogVersion;
    }
}
