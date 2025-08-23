package de.hybris.platform.basecommerce.jalo;

import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.delivery.DeliveryCostsStrategy;

public interface MultiAddressDeliveryCostsStrategy extends DeliveryCostsStrategy
{
    Cart getCartFactory();


    void setCartFactory(CartFactory paramCartFactory);
}
