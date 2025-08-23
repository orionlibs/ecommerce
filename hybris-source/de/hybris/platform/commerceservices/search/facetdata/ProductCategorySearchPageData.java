package de.hybris.platform.commerceservices.search.facetdata;

import java.util.List;

public class ProductCategorySearchPageData<STATE, RESULT, CATEGORY> extends ProductSearchPageData<STATE, RESULT>
{
    private List<CATEGORY> subCategories;


    public void setSubCategories(List<CATEGORY> subCategories)
    {
        this.subCategories = subCategories;
    }


    public List<CATEGORY> getSubCategories()
    {
        return this.subCategories;
    }
}
