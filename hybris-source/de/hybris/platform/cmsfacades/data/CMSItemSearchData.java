package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;

public class CMSItemSearchData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String mask;
    private String typeCode;
    private String typeCodes;
    private String catalogId;
    private String catalogVersion;
    private String itemSearchParams;
    private String fields;


    public void setMask(String mask)
    {
        this.mask = mask;
    }


    public String getMask()
    {
        return this.mask;
    }


    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public void setTypeCodes(String typeCodes)
    {
        this.typeCodes = typeCodes;
    }


    public String getTypeCodes()
    {
        return this.typeCodes;
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


    public void setItemSearchParams(String itemSearchParams)
    {
        this.itemSearchParams = itemSearchParams;
    }


    public String getItemSearchParams()
    {
        return this.itemSearchParams;
    }


    public void setFields(String fields)
    {
        this.fields = fields;
    }


    public String getFields()
    {
        return this.fields;
    }
}
