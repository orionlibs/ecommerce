package de.hybris.platform.solrfacetsearch.config;

import java.io.Serializable;
import java.util.Date;

public class EndpointURL implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String url;
    private boolean master;
    private Date modifiedTime;


    public void setUrl(String url)
    {
        this.url = url;
    }


    public String getUrl()
    {
        return this.url;
    }


    public void setMaster(boolean master)
    {
        this.master = master;
    }


    public boolean isMaster()
    {
        return this.master;
    }


    public void setModifiedTime(Date modifiedTime)
    {
        this.modifiedTime = modifiedTime;
    }


    public Date getModifiedTime()
    {
        return this.modifiedTime;
    }
}
