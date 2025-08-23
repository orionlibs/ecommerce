package de.hybris.platform.warehousing.externalfulfillment.dao;

import de.hybris.platform.ordersplitting.model.WarehouseModel;

public interface WarehousingFulfillmentConfigDao
{
    Object getConfiguration(WarehouseModel paramWarehouseModel);
}
