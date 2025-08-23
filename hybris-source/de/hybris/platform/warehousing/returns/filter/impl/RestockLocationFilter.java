package de.hybris.platform.warehousing.returns.filter.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.sourcing.filter.impl.AbstractBaseSourcingLocationFilter;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestockLocationFilter extends AbstractBaseSourcingLocationFilter
{
    private static Logger LOGGER = LoggerFactory.getLogger(RestockLocationFilter.class);


    public Collection<WarehouseModel> applyFilter(AbstractOrderModel order, Set<WarehouseModel> locations)
    {
        ServicesUtil.validateParameterNotNull(locations, "Parameter locations cannot be null.");
        LOGGER.debug("Filter '{}' found '{}' warehouses.", getClass().getSimpleName(), Integer.valueOf(locations.size()));
        return (Collection<WarehouseModel>)locations.stream().filter(warehouse -> Boolean.TRUE.equals(warehouse.getIsAllowRestock())).collect(Collectors.toList());
    }
}
