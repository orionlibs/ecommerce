package de.hybris.platform.warehouse;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public interface Process2WarehouseAdapter
{
    void prepareConsignment(ConsignmentModel paramConsignmentModel);


    void shipConsignment(ConsignmentModel paramConsignmentModel);
}
