package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CatalogVersionData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Map<String, String> name;
    private Boolean active;
    private List<DisplayConditionData> pageDisplayConditions;
    private String version;
    private String thumbnailUrl;
    private String uuid;
    private HomePageData homepage;


    public void setName(Map<String, String> name)
    {
        this.name = name;
    }


    public Map<String, String> getName()
    {
        return this.name;
    }


    public void setActive(Boolean active)
    {
        this.active = active;
    }


    public Boolean getActive()
    {
        return this.active;
    }


    public void setPageDisplayConditions(List<DisplayConditionData> pageDisplayConditions)
    {
        this.pageDisplayConditions = pageDisplayConditions;
    }


    public List<DisplayConditionData> getPageDisplayConditions()
    {
        return this.pageDisplayConditions;
    }


    public void setVersion(String version)
    {
        this.version = version;
    }


    public String getVersion()
    {
        return this.version;
    }


    public void setThumbnailUrl(String thumbnailUrl)
    {
        this.thumbnailUrl = thumbnailUrl;
    }


    public String getThumbnailUrl()
    {
        return this.thumbnailUrl;
    }


    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }


    public String getUuid()
    {
        return this.uuid;
    }


    public void setHomepage(HomePageData homepage)
    {
        this.homepage = homepage;
    }


    public HomePageData getHomepage()
    {
        return this.homepage;
    }
}
