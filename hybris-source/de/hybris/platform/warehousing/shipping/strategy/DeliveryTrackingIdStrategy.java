package de.hybris.platform.warehousing.shipping.strategy;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public interface DeliveryTrackingIdStrategy
{
    String generateTrackingId(ConsignmentModel paramConsignmentModel);
}
