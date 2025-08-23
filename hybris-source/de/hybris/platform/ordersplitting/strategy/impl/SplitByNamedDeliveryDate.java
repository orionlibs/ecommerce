package de.hybris.platform.ordersplitting.strategy.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.strategy.AbstractSplittingStrategy;
import java.util.Date;

public class SplitByNamedDeliveryDate extends AbstractSplittingStrategy
{
    public Object getGroupingObject(AbstractOrderEntryModel orderEntry)
    {
        return orderEntry.getNamedDeliveryDate();
    }


    public void afterSplitting(Object groupingObject, ConsignmentModel createdOne)
    {
        createdOne.setNamedDeliveryDate((Date)groupingObject);
    }
}
