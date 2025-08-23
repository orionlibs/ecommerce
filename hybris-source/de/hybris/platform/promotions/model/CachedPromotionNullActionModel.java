package de.hybris.platform.promotions.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CachedPromotionNullActionModel extends PromotionNullActionModel
{
    public static final String _TYPECODE = "CachedPromotionNullAction";


    public CachedPromotionNullActionModel()
    {
    }


    public CachedPromotionNullActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CachedPromotionNullActionModel(ItemModel _owner)
    {
        setOwner(_owner);
    }
}
