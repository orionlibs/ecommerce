package de.hybris.platform.promotions.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CachedPromotionOrderChangeDeliveryModeActionModel extends PromotionOrderChangeDeliveryModeActionModel
{
    public static final String _TYPECODE = "CachedPromotionOrderChangeDeliveryModeAction";


    public CachedPromotionOrderChangeDeliveryModeActionModel()
    {
    }


    public CachedPromotionOrderChangeDeliveryModeActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CachedPromotionOrderChangeDeliveryModeActionModel(DeliveryModeModel _deliveryMode)
    {
        setDeliveryMode(_deliveryMode);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CachedPromotionOrderChangeDeliveryModeActionModel(DeliveryModeModel _deliveryMode, ItemModel _owner)
    {
        setDeliveryMode(_deliveryMode);
        setOwner(_owner);
    }
}
