package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;

public class PageTemplateData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String uid;
    private String uuid;
    private String name;
    private String frontEndName;
    private String previewIcon;
    private String catalogVersion;


    public void setUid(String uid)
    {
        this.uid = uid;
    }


    public String getUid()
    {
        return this.uid;
    }


    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }


    public String getUuid()
    {
        return this.uuid;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setFrontEndName(String frontEndName)
    {
        this.frontEndName = frontEndName;
    }


    public String getFrontEndName()
    {
        return this.frontEndName;
    }


    public void setPreviewIcon(String previewIcon)
    {
        this.previewIcon = previewIcon;
    }


    public String getPreviewIcon()
    {
        return this.previewIcon;
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
