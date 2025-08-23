package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;

public class PageTemplateDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String pageTypeCode;
    private Boolean active;


    public void setPageTypeCode(String pageTypeCode)
    {
        this.pageTypeCode = pageTypeCode;
    }


    public String getPageTypeCode()
    {
        return this.pageTypeCode;
    }


    public void setActive(Boolean active)
    {
        this.active = active;
    }


    public Boolean getActive()
    {
        return this.active;
    }
}
