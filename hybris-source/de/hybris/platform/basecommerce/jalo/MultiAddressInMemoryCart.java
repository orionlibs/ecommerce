package de.hybris.platform.basecommerce.jalo;

import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.delivery.JaloDeliveryModeException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.util.PriceValue;
import org.apache.log4j.Logger;

public class MultiAddressInMemoryCart extends GeneratedMultiAddressInMemoryCart
{
    private static final Logger LOG = Logger.getLogger(MultiAddressInMemoryCart.class);


    protected PriceValue findDeliveryCosts() throws JaloPriceFactoryException
    {
        DeliveryMode deliveryMode = getDeliveryMode();
        if(deliveryMode != null)
        {
            try
            {
                return deliveryMode.getCost((AbstractOrder)this);
            }
            catch(JaloDeliveryModeException e)
            {
                LOG.error("Delivery mode error for mode " + deliveryMode.getCode(), (Throwable)e);
            }
        }
        return null;
    }
}
