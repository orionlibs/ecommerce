package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class PromotionOrderAddFreeGiftActionModel extends AbstractPromotionActionModel
{
    public static final String _TYPECODE = "PromotionOrderAddFreeGiftAction";
    public static final String FREEPRODUCT = "freeProduct";


    public PromotionOrderAddFreeGiftActionModel()
    {
    }


    public PromotionOrderAddFreeGiftActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionOrderAddFreeGiftActionModel(ProductModel _freeProduct)
    {
        setFreeProduct(_freeProduct);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionOrderAddFreeGiftActionModel(ProductModel _freeProduct, ItemModel _owner)
    {
        setFreeProduct(_freeProduct);
        setOwner(_owner);
    }


    @Accessor(qualifier = "freeProduct", type = Accessor.Type.GETTER)
    public ProductModel getFreeProduct()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("freeProduct");
    }


    @Accessor(qualifier = "freeProduct", type = Accessor.Type.SETTER)
    public void setFreeProduct(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("freeProduct", value);
    }
}
