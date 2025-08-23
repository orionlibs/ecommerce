package de.hybris.platform.promotions.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CachedPromotionOrderEntryConsumedModel extends PromotionOrderEntryConsumedModel
{
    public static final String _TYPECODE = "CachedPromotionOrderEntryConsumed";


    public CachedPromotionOrderEntryConsumedModel()
    {
    }


    public CachedPromotionOrderEntryConsumedModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CachedPromotionOrderEntryConsumedModel(ItemModel _owner)
    {
        setOwner(_owner);
    }
}
