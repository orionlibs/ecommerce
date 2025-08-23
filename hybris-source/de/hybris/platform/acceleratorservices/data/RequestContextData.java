package de.hybris.platform.acceleratorservices.data;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;
import java.io.Serializable;

public class RequestContextData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private ProductModel product;
    private CategoryModel category;
    private SearchPageData search;


    public void setProduct(ProductModel product)
    {
        this.product = product;
    }


    public ProductModel getProduct()
    {
        return this.product;
    }


    public void setCategory(CategoryModel category)
    {
        this.category = category;
    }


    public CategoryModel getCategory()
    {
        return this.category;
    }


    public void setSearch(SearchPageData search)
    {
        this.search = search;
    }


    public SearchPageData getSearch()
    {
        return this.search;
    }
}
