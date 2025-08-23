package de.hybris.platform.warehousing.externalfulfillment.strategy;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public interface SendConsignmentToExternalFulfillmentSystemStrategy
{
    void sendConsignment(ConsignmentModel paramConsignmentModel);
}
