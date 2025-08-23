package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;

public class PageRestrictionData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String pageId;
    private String restrictionId;


    public void setPageId(String pageId)
    {
        this.pageId = pageId;
    }


    public String getPageId()
    {
        return this.pageId;
    }


    public void setRestrictionId(String restrictionId)
    {
        this.restrictionId = restrictionId;
    }


    public String getRestrictionId()
    {
        return this.restrictionId;
    }
}
