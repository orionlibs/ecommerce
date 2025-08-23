package de.hybris.platform.commercewebservicescommons.dto.order;

import java.io.Serializable;
import java.util.List;

public class RecentlyOrderedProductListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<RecentlyOrderedProductWsDTO> recentlyOrderedProducts;


    public void setRecentlyOrderedProducts(List<RecentlyOrderedProductWsDTO> recentlyOrderedProducts)
    {
        this.recentlyOrderedProducts = recentlyOrderedProducts;
    }


    public List<RecentlyOrderedProductWsDTO> getRecentlyOrderedProducts()
    {
        return this.recentlyOrderedProducts;
    }
}
