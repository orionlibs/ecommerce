package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPromotionOrderChangeDeliveryModeAction extends AbstractPromotionAction
{
    public static final String DELIVERYMODE = "deliveryMode";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractPromotionAction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("deliveryMode", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
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
}
