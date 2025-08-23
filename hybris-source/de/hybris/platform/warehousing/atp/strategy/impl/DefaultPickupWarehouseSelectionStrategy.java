package de.hybris.platform.warehousing.atp.strategy.impl;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.atp.strategy.PickupWarehouseSelectionStrategy;
import de.hybris.platform.warehousing.warehouse.filter.WarehousesFilterProcessor;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPickupWarehouseSelectionStrategy implements PickupWarehouseSelectionStrategy
{
    protected static final String CODE_PICKUP = "pickup";
    private WarehousesFilterProcessor warehousesFilterProcessor;


    public Collection<WarehouseModel> getWarehouses(PointOfServiceModel pos)
    {
        ServicesUtil.validateParameterNotNull(pos, "point of service cannot be null");
        Set<WarehouseModel> pickupWarehouses = null;
        if(Objects.nonNull(pos.getWarehouses()))
        {
            pickupWarehouses = (Set<WarehouseModel>)pos.getWarehouses().stream().filter(warehouse -> Objects.nonNull(warehouse.getDeliveryModes())).filter(warehouse -> warehouse.getDeliveryModes().stream().anyMatch(())).collect(Collectors.toSet());
            pickupWarehouses = getWarehousesFilterProcessor().filterLocations(pickupWarehouses);
        }
        return Objects.isNull(pickupWarehouses) ? Collections.<WarehouseModel>emptySet() : pickupWarehouses;
    }


    protected WarehousesFilterProcessor getWarehousesFilterProcessor()
    {
        return this.warehousesFilterProcessor;
    }


    @Required
    public void setWarehousesFilterProcessor(WarehousesFilterProcessor warehousesFilterProcessor)
    {
        this.warehousesFilterProcessor = warehousesFilterProcessor;
    }
}
