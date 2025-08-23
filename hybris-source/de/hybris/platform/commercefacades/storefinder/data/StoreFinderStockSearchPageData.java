package de.hybris.platform.commercefacades.storefinder.data;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;

public class StoreFinderStockSearchPageData<RESULT> extends StoreFinderSearchPageData<RESULT>
{
    private ProductData product;


    public void setProduct(ProductData product)
    {
        this.product = product;
    }


    public ProductData getProduct()
    {
        return this.product;
    }
}
