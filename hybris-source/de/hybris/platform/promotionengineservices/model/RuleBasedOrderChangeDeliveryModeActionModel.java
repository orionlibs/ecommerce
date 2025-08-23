package de.hybris.platform.promotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.math.BigDecimal;

public class RuleBasedOrderChangeDeliveryModeActionModel extends AbstractRuleBasedPromotionActionModel
{
    public static final String _TYPECODE = "RuleBasedOrderChangeDeliveryModeAction";
    public static final String DELIVERYMODE = "deliveryMode";
    public static final String DELIVERYCOST = "deliveryCost";
    public static final String REPLACEDDELIVERYMODE = "replacedDeliveryMode";
    public static final String REPLACEDDELIVERYCOST = "replacedDeliveryCost";


    public RuleBasedOrderChangeDeliveryModeActionModel()
    {
    }


    public RuleBasedOrderChangeDeliveryModeActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RuleBasedOrderChangeDeliveryModeActionModel(BigDecimal _deliveryCost, DeliveryModeModel _deliveryMode)
    {
        setDeliveryCost(_deliveryCost);
        setDeliveryMode(_deliveryMode);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RuleBasedOrderChangeDeliveryModeActionModel(BigDecimal _deliveryCost, DeliveryModeModel _deliveryMode, ItemModel _owner)
    {
        setDeliveryCost(_deliveryCost);
        setDeliveryMode(_deliveryMode);
        setOwner(_owner);
    }


    @Accessor(qualifier = "deliveryCost", type = Accessor.Type.GETTER)
    public BigDecimal getDeliveryCost()
    {
        return (BigDecimal)getPersistenceContext().getPropertyValue("deliveryCost");
    }


    @Accessor(qualifier = "deliveryMode", type = Accessor.Type.GETTER)
    public DeliveryModeModel getDeliveryMode()
    {
        return (DeliveryModeModel)getPersistenceContext().getPropertyValue("deliveryMode");
    }


    @Accessor(qualifier = "replacedDeliveryCost", type = Accessor.Type.GETTER)
    public BigDecimal getReplacedDeliveryCost()
    {
        return (BigDecimal)getPersistenceContext().getPropertyValue("replacedDeliveryCost");
    }


    @Accessor(qualifier = "replacedDeliveryMode", type = Accessor.Type.GETTER)
    public DeliveryModeModel getReplacedDeliveryMode()
    {
        return (DeliveryModeModel)getPersistenceContext().getPropertyValue("replacedDeliveryMode");
    }


    @Accessor(qualifier = "deliveryCost", type = Accessor.Type.SETTER)
    public void setDeliveryCost(BigDecimal value)
    {
        getPersistenceContext().setPropertyValue("deliveryCost", value);
    }


    @Accessor(qualifier = "deliveryMode", type = Accessor.Type.SETTER)
    public void setDeliveryMode(DeliveryModeModel value)
    {
        getPersistenceContext().setPropertyValue("deliveryMode", value);
    }


    @Accessor(qualifier = "replacedDeliveryCost", type = Accessor.Type.SETTER)
    public void setReplacedDeliveryCost(BigDecimal value)
    {
        getPersistenceContext().setPropertyValue("replacedDeliveryCost", value);
    }


    @Accessor(qualifier = "replacedDeliveryMode", type = Accessor.Type.SETTER)
    public void setReplacedDeliveryMode(DeliveryModeModel value)
    {
        getPersistenceContext().setPropertyValue("replacedDeliveryMode", value);
    }
}
