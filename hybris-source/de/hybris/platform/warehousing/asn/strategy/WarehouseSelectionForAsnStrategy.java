package de.hybris.platform.warehousing.asn.strategy;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeModel;

public interface WarehouseSelectionForAsnStrategy
{
    WarehouseModel getDefaultWarehouse(AdvancedShippingNoticeModel paramAdvancedShippingNoticeModel);
}
