package de.hybris.platform.jalo.order.delivery;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.util.PriceValue;

public interface DeliveryCostsStrategy
{
    PriceValue findDeliveryCosts(SessionContext paramSessionContext, AbstractOrder paramAbstractOrder);
}
