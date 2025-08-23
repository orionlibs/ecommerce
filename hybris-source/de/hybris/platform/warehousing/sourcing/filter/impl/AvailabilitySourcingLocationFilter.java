package de.hybris.platform.warehousing.sourcing.filter.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.stock.services.impl.DefaultWarehouseStockService;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class AvailabilitySourcingLocationFilter extends AbstractBaseSourcingLocationFilter
{
    private static Logger LOGGER = LoggerFactory.getLogger(AvailabilitySourcingLocationFilter.class);
    private DefaultWarehouseStockService warehouseStockService;


    public Collection<WarehouseModel> applyFilter(AbstractOrderModel order, Set<WarehouseModel> warehouses)
    {
        Collection<WarehouseModel> result = (Collection<WarehouseModel>)warehouses.stream().filter(warehouse -> isWarehouseHasAvailabilityForAnyProductInOrder(order, warehouse)).collect(Collectors.toList());
        LOGGER.debug("Filter '{}' found '{}' warehouses.", getClass().getSimpleName(), Integer.valueOf(result.size()));
        return result;
    }


    protected boolean isWarehouseHasAvailabilityForAnyProductInOrder(AbstractOrderModel order, WarehouseModel warehouse)
    {
        return order.getEntries().stream().anyMatch(entry -> {
            Long available = getWarehouseStockService().getStockLevelForProductCodeAndWarehouse(entry.getProduct().getCode(), warehouse);
            return (available == null || available.longValue() > 0L);
        });
    }


    protected DefaultWarehouseStockService getWarehouseStockService()
    {
        return this.warehouseStockService;
    }


    @Required
    public void setWarehouseStockService(DefaultWarehouseStockService warehouseStockService)
    {
        this.warehouseStockService = warehouseStockService;
    }
}
