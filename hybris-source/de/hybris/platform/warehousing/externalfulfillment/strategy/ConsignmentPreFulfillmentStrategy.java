package de.hybris.platform.warehousing.externalfulfillment.strategy;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public interface ConsignmentPreFulfillmentStrategy
{
    void perform(ConsignmentModel paramConsignmentModel);


    boolean canProceedAfterPerform(ConsignmentModel paramConsignmentModel);
}
