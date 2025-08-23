package de.hybris.platform.warehousing.asn.strategy.impl;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.asn.strategy.WarehouseSelectionForAsnStrategy;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeModel;
import org.apache.commons.collections.CollectionUtils;

public class DefaultWarehouseSelectionForAsnStrategy implements WarehouseSelectionForAsnStrategy
{
    public WarehouseModel getDefaultWarehouse(AdvancedShippingNoticeModel advancedShippingNotice)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("advancedShippingNotice", advancedShippingNotice);
        PointOfServiceModel pos = advancedShippingNotice.getPointOfService();
        ServicesUtil.validateParameterNotNull(pos, "No Point of Service assigned to ASN:" + advancedShippingNotice.getExternalId() + (
                        (advancedShippingNotice.getInternalId() != null) ? (":" +
                                        advancedShippingNotice.getInternalId()) : ""));
        return CollectionUtils.isNotEmpty(pos.getWarehouses()) ? pos.getWarehouses().get(0) : null;
    }
}
