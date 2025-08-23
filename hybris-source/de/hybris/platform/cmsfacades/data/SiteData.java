package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SiteData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String uid;
    private String previewUrl;
    private Map<String, String> name;
    private List<String> contentCatalogs;


    public void setUid(String uid)
    {
        this.uid = uid;
    }


    public String getUid()
    {
        return this.uid;
    }


    public void setPreviewUrl(String previewUrl)
    {
        this.previewUrl = previewUrl;
    }


    public String getPreviewUrl()
    {
        return this.previewUrl;
    }


    public void setName(Map<String, String> name)
    {
        this.name = name;
    }


    public Map<String, String> getName()
    {
        return this.name;
    }


    public void setContentCatalogs(List<String> contentCatalogs)
    {
        this.contentCatalogs = contentCatalogs;
    }


    public List<String> getContentCatalogs()
    {
        return this.contentCatalogs;
    }
}
