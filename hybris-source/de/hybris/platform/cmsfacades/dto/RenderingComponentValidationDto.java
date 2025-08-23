package de.hybris.platform.cmsfacades.dto;

import java.io.Serializable;

public class RenderingComponentValidationDto implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String catalogCode;
    private String categoryCode;
    private String productCode;


    public void setCatalogCode(String catalogCode)
    {
        this.catalogCode = catalogCode;
    }


    public String getCatalogCode()
    {
        return this.catalogCode;
    }


    public void setCategoryCode(String categoryCode)
    {
        this.categoryCode = categoryCode;
    }


    public String getCategoryCode()
    {
        return this.categoryCode;
    }


    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }


    public String getProductCode()
    {
        return this.productCode;
    }
}
