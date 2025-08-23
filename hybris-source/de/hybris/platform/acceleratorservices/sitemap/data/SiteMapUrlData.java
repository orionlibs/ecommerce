package de.hybris.platform.acceleratorservices.sitemap.data;

import java.io.Serializable;
import java.util.List;

public class SiteMapUrlData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String loc;
    private List<String> images;


    public void setLoc(String loc)
    {
        this.loc = loc;
    }


    public String getLoc()
    {
        return this.loc;
    }


    public void setImages(List<String> images)
    {
        this.images = images;
    }


    public List<String> getImages()
    {
        return this.images;
    }
}
