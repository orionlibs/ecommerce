package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;

public class PageTypeRestrictionTypeData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String pageType;
    private String restrictionType;


    public void setPageType(String pageType)
    {
        this.pageType = pageType;
    }


    public String getPageType()
    {
        return this.pageType;
    }


    public void setRestrictionType(String restrictionType)
    {
        this.restrictionType = restrictionType;
    }


    public String getRestrictionType()
    {
        return this.restrictionType;
    }
}
