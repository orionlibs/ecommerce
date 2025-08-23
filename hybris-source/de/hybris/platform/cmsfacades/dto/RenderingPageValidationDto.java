package de.hybris.platform.cmsfacades.dto;

import java.io.Serializable;

public class RenderingPageValidationDto implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String pageTypeCode;
    private String code;
    private String pageLabelOrId;


    public void setPageTypeCode(String pageTypeCode)
    {
        this.pageTypeCode = pageTypeCode;
    }


    public String getPageTypeCode()
    {
        return this.pageTypeCode;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setPageLabelOrId(String pageLabelOrId)
    {
        this.pageLabelOrId = pageLabelOrId;
    }


    public String getPageLabelOrId()
    {
        return this.pageLabelOrId;
    }
}
