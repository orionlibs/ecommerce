package de.hybris.platform.commercefacades.catalog.data;

import de.hybris.platform.commercefacades.product.data.ProductData;
import java.util.List;

public class CategoryHierarchyData extends AbstractCatalogItemData
{
    private Integer pageSize;
    private Integer totalNumber;
    private Integer currentPage;
    private Integer numberOfPages;
    private List<CategoryHierarchyData> subcategories;
    private List<ProductData> products;
    private Integer level;


    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }


    public Integer getPageSize()
    {
        return this.pageSize;
    }


    public void setTotalNumber(Integer totalNumber)
    {
        this.totalNumber = totalNumber;
    }


    public Integer getTotalNumber()
    {
        return this.totalNumber;
    }


    public void setCurrentPage(Integer currentPage)
    {
        this.currentPage = currentPage;
    }


    public Integer getCurrentPage()
    {
        return this.currentPage;
    }


    public void setNumberOfPages(Integer numberOfPages)
    {
        this.numberOfPages = numberOfPages;
    }


    public Integer getNumberOfPages()
    {
        return this.numberOfPages;
    }


    public void setSubcategories(List<CategoryHierarchyData> subcategories)
    {
        this.subcategories = subcategories;
    }


    public List<CategoryHierarchyData> getSubcategories()
    {
        return this.subcategories;
    }


    public void setProducts(List<ProductData> products)
    {
        this.products = products;
    }


    public List<ProductData> getProducts()
    {
        return this.products;
    }


    public void setLevel(Integer level)
    {
        this.level = level;
    }


    public Integer getLevel()
    {
        return this.level;
    }
}
