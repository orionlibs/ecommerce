package de.hybris.platform.acceleratorservices.model.promotions;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.promotions.model.ProductBOGOFPromotionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AcceleratorProductBOGOFPromotionModel extends ProductBOGOFPromotionModel
{
    public static final String _TYPECODE = "AcceleratorProductBOGOFPromotion";


    public AcceleratorProductBOGOFPromotionModel()
    {
    }


    public AcceleratorProductBOGOFPromotionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AcceleratorProductBOGOFPromotionModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AcceleratorProductBOGOFPromotionModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }
}
