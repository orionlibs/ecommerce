package de.hybris.platform.warehousing.sourcing.filter.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.atp.strategy.PickupWarehouseSelectionStrategy;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class PickupSourcingLocationFilter extends AbstractBaseSourcingLocationFilter
{
    private static Logger LOGGER = LoggerFactory.getLogger(PickupSourcingLocationFilter.class);
    private PickupWarehouseSelectionStrategy pickupWarehouseSelectionStrategy;


    public Collection<WarehouseModel> applyFilter(AbstractOrderModel order, Set<WarehouseModel> locations)
    {
        Collection<WarehouseModel> result = (Collection<WarehouseModel>)order.getEntries().stream().filter(entry -> (entry.getDeliveryPointOfService() != null)).flatMap(entry -> getPickupWarehouseSelectionStrategy().getWarehouses(entry.getDeliveryPointOfService()).stream())
                        .collect(Collectors.toSet());
        LOGGER.debug("Filter '{}' found '{}' warehouses.", getClass().getSimpleName(), Integer.valueOf(result.size()));
        return result;
    }


    protected PickupWarehouseSelectionStrategy getPickupWarehouseSelectionStrategy()
    {
        return this.pickupWarehouseSelectionStrategy;
    }


    @Required
    public void setPickupWarehouseSelectionStrategy(PickupWarehouseSelectionStrategy pickupWarehouseSelectionStrategy)
    {
        this.pickupWarehouseSelectionStrategy = pickupWarehouseSelectionStrategy;
    }
}
