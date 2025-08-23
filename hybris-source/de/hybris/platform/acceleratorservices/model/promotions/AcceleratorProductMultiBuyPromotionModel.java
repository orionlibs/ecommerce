package de.hybris.platform.acceleratorservices.model.promotions;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.promotions.model.ProductMultiBuyPromotionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AcceleratorProductMultiBuyPromotionModel extends ProductMultiBuyPromotionModel
{
    public static final String _TYPECODE = "AcceleratorProductMultiBuyPromotion";


    public AcceleratorProductMultiBuyPromotionModel()
    {
    }


    public AcceleratorProductMultiBuyPromotionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AcceleratorProductMultiBuyPromotionModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AcceleratorProductMultiBuyPromotionModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }
}
