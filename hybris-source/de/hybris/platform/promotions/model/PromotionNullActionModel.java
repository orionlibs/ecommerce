package de.hybris.platform.promotions.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class PromotionNullActionModel extends AbstractPromotionActionModel
{
    public static final String _TYPECODE = "PromotionNullAction";


    public PromotionNullActionModel()
    {
    }


    public PromotionNullActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionNullActionModel(ItemModel _owner)
    {
        setOwner(_owner);
    }
}
