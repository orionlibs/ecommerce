package de.hybris.platform.ordersplitting.strategy.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.strategy.AbstractSplittingStrategy;

public class SplitByEntryDeliveryAddress extends AbstractSplittingStrategy
{
    public Object getGroupingObject(AbstractOrderEntryModel orderEntry)
    {
        return orderEntry.getDeliveryAddress();
    }


    public void afterSplitting(Object groupingObject, ConsignmentModel createdOne)
    {
        createdOne.setShippingAddress((AddressModel)groupingObject);
    }
}
