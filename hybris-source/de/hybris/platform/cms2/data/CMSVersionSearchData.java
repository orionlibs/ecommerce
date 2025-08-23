package de.hybris.platform.cms2.data;

import java.io.Serializable;

public class CMSVersionSearchData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String mask;
    private String itemUid;
    private String itemCatalogId;
    private String itemCatalogVersion;


    public void setMask(String mask)
    {
        this.mask = mask;
    }


    public String getMask()
    {
        return this.mask;
    }


    public void setItemUid(String itemUid)
    {
        this.itemUid = itemUid;
    }


    public String getItemUid()
    {
        return this.itemUid;
    }


    public void setItemCatalogId(String itemCatalogId)
    {
        this.itemCatalogId = itemCatalogId;
    }


    public String getItemCatalogId()
    {
        return this.itemCatalogId;
    }


    public void setItemCatalogVersion(String itemCatalogVersion)
    {
        this.itemCatalogVersion = itemCatalogVersion;
    }


    public String getItemCatalogVersion()
    {
        return this.itemCatalogVersion;
    }
}
