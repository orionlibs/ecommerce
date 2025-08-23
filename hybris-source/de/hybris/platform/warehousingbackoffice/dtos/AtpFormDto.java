package de.hybris.platform.warehousingbackoffice.dtos;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

public class AtpFormDto
{
    private ProductModel product;
    private BaseStoreModel baseStore;
    private PointOfServiceModel pointOfService;


    public AtpFormDto(ProductModel product, BaseStoreModel baseStore, PointOfServiceModel pointOfService)
    {
        this.product = product;
        this.baseStore = baseStore;
        this.pointOfService = pointOfService;
    }


    public ProductModel getProduct()
    {
        return this.product;
    }


    public void setProduct(ProductModel product)
    {
        this.product = product;
    }


    public BaseStoreModel getBaseStore()
    {
        return this.baseStore;
    }


    public void setBaseStore(BaseStoreModel baseStore)
    {
        this.baseStore = baseStore;
    }


    public PointOfServiceModel getPointOfService()
    {
        return this.pointOfService;
    }


    public void setPointOfService(PointOfServiceModel pointOfService)
    {
        this.pointOfService = pointOfService;
    }
}
