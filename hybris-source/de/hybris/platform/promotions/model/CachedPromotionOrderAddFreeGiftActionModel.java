package de.hybris.platform.promotions.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CachedPromotionOrderAddFreeGiftActionModel extends PromotionOrderAddFreeGiftActionModel
{
    public static final String _TYPECODE = "CachedPromotionOrderAddFreeGiftAction";


    public CachedPromotionOrderAddFreeGiftActionModel()
    {
    }


    public CachedPromotionOrderAddFreeGiftActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CachedPromotionOrderAddFreeGiftActionModel(ProductModel _freeProduct)
    {
        setFreeProduct(_freeProduct);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CachedPromotionOrderAddFreeGiftActionModel(ProductModel _freeProduct, ItemModel _owner)
    {
        setFreeProduct(_freeProduct);
        setOwner(_owner);
    }
}
