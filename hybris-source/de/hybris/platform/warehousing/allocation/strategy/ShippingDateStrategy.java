package de.hybris.platform.warehousing.allocation.strategy;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import java.util.Date;

public interface ShippingDateStrategy
{
    Date getExpectedShippingDate(ConsignmentModel paramConsignmentModel);
}
