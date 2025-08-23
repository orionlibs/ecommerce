package de.hybris.platform.commerceservices.product.data;

import java.io.Serializable;

public class SolrFirstVariantCategoryEntryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String categoryName;
    private String variantCode;


    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }


    public String getCategoryName()
    {
        return this.categoryName;
    }


    public void setVariantCode(String variantCode)
    {
        this.variantCode = variantCode;
    }


    public String getVariantCode()
    {
        return this.variantCode;
    }
}
