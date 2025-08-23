package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.Map;

public class CMSWorkflowAttachmentData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String pageUid;
    private String pageName;
    private String catalogId;
    private Map<String, String> catalogName;
    private String catalogVersion;


    public void setPageUid(String pageUid)
    {
        this.pageUid = pageUid;
    }


    public String getPageUid()
    {
        return this.pageUid;
    }


    public void setPageName(String pageName)
    {
        this.pageName = pageName;
    }


    public String getPageName()
    {
        return this.pageName;
    }


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


    public void setCatalogVersion(String catalogVersion)
    {
        this.catalogVersion = catalogVersion;
    }


    public String getCatalogVersion()
    {
        return this.catalogVersion;
    }
}
