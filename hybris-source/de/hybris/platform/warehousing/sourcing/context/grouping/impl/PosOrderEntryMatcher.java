package de.hybris.platform.warehousing.sourcing.context.grouping.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.sourcing.context.grouping.OrderEntryMatcher;

public class PosOrderEntryMatcher implements OrderEntryMatcher<PointOfServiceModel>
{
    public PointOfServiceModel getMatchingObject(AbstractOrderEntryModel orderEntry)
    {
        return orderEntry.getDeliveryPointOfService();
    }
}
