package de.hybris.platform.ocafacades.order.data;

import java.io.Serializable;
import java.util.List;

public class RecentlyOrderedProductListData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<RecentlyOrderedProductData> recentlyOrderedProducts;


    public void setRecentlyOrderedProducts(List<RecentlyOrderedProductData> recentlyOrderedProducts)
    {
        this.recentlyOrderedProducts = recentlyOrderedProducts;
    }


    public List<RecentlyOrderedProductData> getRecentlyOrderedProducts()
    {
        return this.recentlyOrderedProducts;
    }
}
