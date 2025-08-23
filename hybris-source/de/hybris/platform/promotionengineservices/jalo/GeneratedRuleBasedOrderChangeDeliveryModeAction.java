package de.hybris.platform.promotionengineservices.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRuleBasedOrderChangeDeliveryModeAction extends AbstractRuleBasedPromotionAction
{
    public static final String DELIVERYMODE = "deliveryMode";
    public static final String DELIVERYCOST = "deliveryCost";
    public static final String REPLACEDDELIVERYMODE = "replacedDeliveryMode";
    public static final String REPLACEDDELIVERYCOST = "replacedDeliveryCost";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRuleBasedPromotionAction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("deliveryMode", Item.AttributeMode.INITIAL);
        tmp.put("deliveryCost", Item.AttributeMode.INITIAL);
        tmp.put("replacedDeliveryMode", Item.AttributeMode.INITIAL);
        tmp.put("replacedDeliveryCost", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public BigDecimal getDeliveryCost(SessionContext ctx)
    {
        return (BigDecimal)getProperty(ctx, "deliveryCost");
    }


    public BigDecimal getDeliveryCost()
    {
        return getDeliveryCost(getSession().getSessionContext());
    }


    public void setDeliveryCost(SessionContext ctx, BigDecimal value)
    {
        setProperty(ctx, "deliveryCost", value);
    }


    public void setDeliveryCost(BigDecimal value)
    {
        setDeliveryCost(getSession().getSessionContext(), value);
    }


    public DeliveryMode getDeliveryMode(SessionContext ctx)
    {
        return (DeliveryMode)getProperty(ctx, "deliveryMode");
    }


    public DeliveryMode getDeliveryMode()
    {
        return getDeliveryMode(getSession().getSessionContext());
    }


    public void setDeliveryMode(SessionContext ctx, DeliveryMode value)
    {
        setProperty(ctx, "deliveryMode", value);
    }


    public void setDeliveryMode(DeliveryMode value)
    {
        setDeliveryMode(getSession().getSessionContext(), value);
    }


    public BigDecimal getReplacedDeliveryCost(SessionContext ctx)
    {
        return (BigDecimal)getProperty(ctx, "replacedDeliveryCost");
    }


    public BigDecimal getReplacedDeliveryCost()
    {
        return getReplacedDeliveryCost(getSession().getSessionContext());
    }


    public void setReplacedDeliveryCost(SessionContext ctx, BigDecimal value)
    {
        setProperty(ctx, "replacedDeliveryCost", value);
    }


    public void setReplacedDeliveryCost(BigDecimal value)
    {
        setReplacedDeliveryCost(getSession().getSessionContext(), value);
    }


    public DeliveryMode getReplacedDeliveryMode(SessionContext ctx)
    {
        return (DeliveryMode)getProperty(ctx, "replacedDeliveryMode");
    }


    public DeliveryMode getReplacedDeliveryMode()
    {
        return getReplacedDeliveryMode(getSession().getSessionContext());
    }


    public void setReplacedDeliveryMode(SessionContext ctx, DeliveryMode value)
    {
        setProperty(ctx, "replacedDeliveryMode", value);
    }


    public void setReplacedDeliveryMode(DeliveryMode value)
    {
        setReplacedDeliveryMode(getSession().getSessionContext(), value);
    }
}
