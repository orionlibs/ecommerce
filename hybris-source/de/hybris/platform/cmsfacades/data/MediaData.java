package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;

public class MediaData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String uuid;
    private String code;
    private String catalogId;
    private String catalogVersion;
    private String mime;
    private String altText;
    private String description;
    private String url;
    private String downloadUrl;


    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }


    public String getUuid()
    {
        return this.uuid;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
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


    public void setMime(String mime)
    {
        this.mime = mime;
    }


    public String getMime()
    {
        return this.mime;
    }


    public void setAltText(String altText)
    {
        this.altText = altText;
    }


    public String getAltText()
    {
        return this.altText;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setUrl(String url)
    {
        this.url = url;
    }


    public String getUrl()
    {
        return this.url;
    }


    public void setDownloadUrl(String downloadUrl)
    {
        this.downloadUrl = downloadUrl;
    }


    public String getDownloadUrl()
    {
        return this.downloadUrl;
    }
}
