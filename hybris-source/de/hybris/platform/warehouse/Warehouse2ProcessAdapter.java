package de.hybris.platform.warehouse;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public interface Warehouse2ProcessAdapter
{
    void receiveConsignmentStatus(ConsignmentModel paramConsignmentModel, WarehouseConsignmentStatus paramWarehouseConsignmentStatus);
}
