package de.hybris.platform.commercewebservices.core.product.data;

import de.hybris.platform.commercefacades.product.data.ProductData;
import java.io.Serializable;
import java.util.List;

public class ProductDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<ProductData> products;
    private String catalog;
    private String version;
    private int totalProductCount;
    private int totalPageCount;
    private int currentPage;


    public void setProducts(List<ProductData> products)
    {
        this.products = products;
    }


    public List<ProductData> getProducts()
    {
        return this.products;
    }


    public void setCatalog(String catalog)
    {
        this.catalog = catalog;
    }


    public String getCatalog()
    {
        return this.catalog;
    }


    public void setVersion(String version)
    {
        this.version = version;
    }


    public String getVersion()
    {
        return this.version;
    }


    public void setTotalProductCount(int totalProductCount)
    {
        this.totalProductCount = totalProductCount;
    }


    public int getTotalProductCount()
    {
        return this.totalProductCount;
    }


    public void setTotalPageCount(int totalPageCount)
    {
        this.totalPageCount = totalPageCount;
    }


    public int getTotalPageCount()
    {
        return this.totalPageCount;
    }


    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
    }


    public int getCurrentPage()
    {
        return this.currentPage;
    }
}
