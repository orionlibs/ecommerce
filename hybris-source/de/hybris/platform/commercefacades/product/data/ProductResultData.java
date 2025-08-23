package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;
import java.util.List;

public class ProductResultData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<ProductData> products;
    private int totalCount;
    private int count;
    private int requestedCount;
    private int requestedStart;


    public void setProducts(List<ProductData> products)
    {
        this.products = products;
    }


    public List<ProductData> getProducts()
    {
        return this.products;
    }


    public void setTotalCount(int totalCount)
    {
        this.totalCount = totalCount;
    }


    public int getTotalCount()
    {
        return this.totalCount;
    }


    public void setCount(int count)
    {
        this.count = count;
    }


    public int getCount()
    {
        return this.count;
    }


    public void setRequestedCount(int requestedCount)
    {
        this.requestedCount = requestedCount;
    }


    public int getRequestedCount()
    {
        return this.requestedCount;
    }


    public void setRequestedStart(int requestedStart)
    {
        this.requestedStart = requestedStart;
    }


    public int getRequestedStart()
    {
        return this.requestedStart;
    }
}
