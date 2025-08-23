package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CatalogHierarchyData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String catalogId;
    private Map<String, String> catalogName;
    private List<CatalogVersionData> versions;
    private List<SiteData> sites;


    public void setCatalogId(String catalogId)
    {
        this.catalogId = catalogId;
    }


    public String getCatalogId()
    {
        return this.catalogId;
    }


    public void setCatalogName(Map<String, String> catalogName)
    {
        this.catalogName = catalogName;
    }


    public Map<String, String> getCatalogName()
    {
        return this.catalogName;
    }


    public void setVersions(List<CatalogVersionData> versions)
    {
        this.versions = versions;
    }


    public List<CatalogVersionData> getVersions()
    {
        return this.versions;
    }


    public void setSites(List<SiteData> sites)
    {
        this.sites = sites;
    }


    public List<SiteData> getSites()
    {
        return this.sites;
    }
}
