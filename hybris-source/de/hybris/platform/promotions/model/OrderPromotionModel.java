package de.hybris.platform.promotions.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OrderPromotionModel extends AbstractPromotionModel
{
    public static final String _TYPECODE = "OrderPromotion";


    public OrderPromotionModel()
    {
    }


    public OrderPromotionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderPromotionModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderPromotionModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }
}
