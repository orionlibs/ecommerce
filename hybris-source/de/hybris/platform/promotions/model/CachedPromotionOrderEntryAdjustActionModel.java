package de.hybris.platform.promotions.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CachedPromotionOrderEntryAdjustActionModel extends PromotionOrderEntryAdjustActionModel
{
    public static final String _TYPECODE = "CachedPromotionOrderEntryAdjustAction";


    public CachedPromotionOrderEntryAdjustActionModel()
    {
    }


    public CachedPromotionOrderEntryAdjustActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CachedPromotionOrderEntryAdjustActionModel(ItemModel _owner)
    {
        setOwner(_owner);
    }
}
