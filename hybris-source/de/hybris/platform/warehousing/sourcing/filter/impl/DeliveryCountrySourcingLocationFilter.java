package de.hybris.platform.warehousing.sourcing.filter.impl;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.warehouse.service.WarehousingWarehouseService;
import java.util.Collection;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DeliveryCountrySourcingLocationFilter extends AbstractBaseSourcingLocationFilter
{
    private static Logger LOGGER = LoggerFactory.getLogger(DeliveryCountrySourcingLocationFilter.class);
    private WarehousingWarehouseService warehousingWarehouseService;


    public Collection<WarehouseModel> applyFilter(AbstractOrderModel order, Set<WarehouseModel> locations)
    {
        if(order.getDeliveryAddress() != null && order.getDeliveryAddress().getCountry() != null && order.getStore() != null)
        {
            CountryModel country = order.getDeliveryAddress().getCountry();
            Collection<WarehouseModel> result = getWarehousingWarehouseService().getWarehousesByBaseStoreDeliveryCountry(order
                            .getStore(), country);
            LOGGER.debug("Filter '{}' found '{}' warehouses.", getClass().getSimpleName(), Integer.valueOf(result.size()));
            return result;
        }
        return locations;
    }


    protected WarehousingWarehouseService getWarehousingWarehouseService()
    {
        return this.warehousingWarehouseService;
    }


    @Required
    public void setWarehousingWarehouseService(WarehousingWarehouseService warehousingWarehouseService)
    {
        this.warehousingWarehouseService = warehousingWarehouseService;
    }
}
