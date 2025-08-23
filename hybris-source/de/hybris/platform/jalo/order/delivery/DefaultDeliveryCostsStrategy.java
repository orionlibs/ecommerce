package de.hybris.platform.jalo.order.delivery;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.util.PriceValue;
import org.apache.log4j.Logger;

public class DefaultDeliveryCostsStrategy implements DeliveryCostsStrategy
{
    private static final Logger LOG = Logger.getLogger(DefaultDeliveryCostsStrategy.class.getName());


    public PriceValue findDeliveryCosts(SessionContext ctx, AbstractOrder order)
    {
        DeliveryMode deliveryMode = order.getDeliveryMode();
        if(deliveryMode != null)
        {
            try
            {
                return deliveryMode.getCost(order);
            }
            catch(JaloDeliveryModeException e)
            {
                LOG.error("Delivery mode error for mode " + deliveryMode.getCode(), (Throwable)e);
            }
        }
        return null;
    }
}
