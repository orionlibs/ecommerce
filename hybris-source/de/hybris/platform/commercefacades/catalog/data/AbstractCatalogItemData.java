package de.hybris.platform.commercefacades.catalog.data;

import java.io.Serializable;
import java.util.Date;

public class AbstractCatalogItemData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private Date lastModified;
    private String name;
    private String url;


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setLastModified(Date lastModified)
    {
        this.lastModified = lastModified;
    }


    public Date getLastModified()
    {
        return this.lastModified;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setUrl(String url)
    {
        this.url = url;
    }


    public String getUrl()
    {
        return this.url;
    }
}
