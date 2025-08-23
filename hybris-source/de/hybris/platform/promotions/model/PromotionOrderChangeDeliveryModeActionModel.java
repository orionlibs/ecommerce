package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class PromotionOrderChangeDeliveryModeActionModel extends AbstractPromotionActionModel
{
    public static final String _TYPECODE = "PromotionOrderChangeDeliveryModeAction";
    public static final String DELIVERYMODE = "deliveryMode";


    public PromotionOrderChangeDeliveryModeActionModel()
    {
    }


    public PromotionOrderChangeDeliveryModeActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionOrderChangeDeliveryModeActionModel(DeliveryModeModel _deliveryMode)
    {
        setDeliveryMode(_deliveryMode);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionOrderChangeDeliveryModeActionModel(DeliveryModeModel _deliveryMode, ItemModel _owner)
    {
        setDeliveryMode(_deliveryMode);
        setOwner(_owner);
    }


    @Accessor(qualifier = "deliveryMode", type = Accessor.Type.GETTER)
    public DeliveryModeModel getDeliveryMode()
    {
        return (DeliveryModeModel)getPersistenceContext().getPropertyValue("deliveryMode");
    }


    @Accessor(qualifier = "deliveryMode", type = Accessor.Type.SETTER)
    public void setDeliveryMode(DeliveryModeModel value)
    {
        getPersistenceContext().setPropertyValue("deliveryMode", value);
    }
}
