package de.hybris.platform.promotions.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CachedPromotionOrderAdjustTotalActionModel extends PromotionOrderAdjustTotalActionModel
{
    public static final String _TYPECODE = "CachedPromotionOrderAdjustTotalAction";


    public CachedPromotionOrderAdjustTotalActionModel()
    {
    }


    public CachedPromotionOrderAdjustTotalActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CachedPromotionOrderAdjustTotalActionModel(ItemModel _owner)
    {
        setOwner(_owner);
    }
}
