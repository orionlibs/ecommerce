package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CatalogData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String catalogId;
    private Map<String, String> name;
    private List<SiteData> sites;


    public void setCatalogId(String catalogId)
    {
        this.catalogId = catalogId;
    }


    public String getCatalogId()
    {
        return this.catalogId;
    }


    public void setName(Map<String, String> name)
    {
        this.name = name;
    }


    public Map<String, String> getName()
    {
        return this.name;
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
