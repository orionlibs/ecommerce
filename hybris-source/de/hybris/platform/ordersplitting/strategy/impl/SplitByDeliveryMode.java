package de.hybris.platform.ordersplitting.strategy.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.strategy.AbstractSplittingStrategy;

public class SplitByDeliveryMode extends AbstractSplittingStrategy
{
    public Object getGroupingObject(AbstractOrderEntryModel orderEntry)
    {
        return orderEntry.getDeliveryMode();
    }


    public void afterSplitting(Object groupingObject, ConsignmentModel createdOne)
    {
        createdOne.setDeliveryMode((DeliveryModeModel)groupingObject);
    }
}
