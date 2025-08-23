package de.hybris.platform.cms2.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CMSItemSearchData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String mask;
    private String typeCode;
    private List<String> typeCodes;
    private String catalogId;
    private String catalogVersion;
    private Map<String, String> itemSearchParams;


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


    public void setTypeCodes(List<String> typeCodes)
    {
        this.typeCodes = typeCodes;
    }


    public List<String> getTypeCodes()
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


    public void setItemSearchParams(Map<String, String> itemSearchParams)
    {
        this.itemSearchParams = itemSearchParams;
    }


    public Map<String, String> getItemSearchParams()
    {
        return this.itemSearchParams;
    }
}
