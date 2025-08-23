package de.hybris.platform.commercefacades.storelocator.data;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.io.Serializable;

public class StoreStockHolder implements Serializable
{
    private static final long serialVersionUID = 1L;
    private ProductModel product;
    private PointOfServiceModel pointOfService;


    public void setProduct(ProductModel product)
    {
        this.product = product;
    }


    public ProductModel getProduct()
    {
        return this.product;
    }


    public void setPointOfService(PointOfServiceModel pointOfService)
    {
        this.pointOfService = pointOfService;
    }


    public PointOfServiceModel getPointOfService()
    {
        return this.pointOfService;
    }
}
