package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.Map;

public class MediaContainerData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Map<String, String> formatMediaCodeMap;
    private String qualifier;
    private String catalogVersion;
    private String thumbnailUrl;
    private String mediaContainerUuid;


    public void setFormatMediaCodeMap(Map<String, String> formatMediaCodeMap)
    {
        this.formatMediaCodeMap = formatMediaCodeMap;
    }


    public Map<String, String> getFormatMediaCodeMap()
    {
        return this.formatMediaCodeMap;
    }


    public void setQualifier(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public void setCatalogVersion(String catalogVersion)
    {
        this.catalogVersion = catalogVersion;
    }


    public String getCatalogVersion()
    {
        return this.catalogVersion;
    }


    public void setThumbnailUrl(String thumbnailUrl)
    {
        this.thumbnailUrl = thumbnailUrl;
    }


    public String getThumbnailUrl()
    {
        return this.thumbnailUrl;
    }


    public void setMediaContainerUuid(String mediaContainerUuid)
    {
        this.mediaContainerUuid = mediaContainerUuid;
    }


    public String getMediaContainerUuid()
    {
        return this.mediaContainerUuid;
    }
}
