package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.Map;

public class CategoryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private Map<String, String> name;
    private Map<String, String> description;
    private String thumbnailMediaCode;
    private String catalogId;
    private String catalogVersion;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setName(Map<String, String> name)
    {
        this.name = name;
    }


    public Map<String, String> getName()
    {
        return this.name;
    }


    public void setDescription(Map<String, String> description)
    {
        this.description = description;
    }


    public Map<String, String> getDescription()
    {
        return this.description;
    }


    public void setThumbnailMediaCode(String thumbnailMediaCode)
    {
        this.thumbnailMediaCode = thumbnailMediaCode;
    }


    public String getThumbnailMediaCode()
    {
        return this.thumbnailMediaCode;
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
