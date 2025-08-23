package de.hybris.platform.warehousing.warehouse.filter.impl;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.model.SourcingBanModel;
import de.hybris.platform.warehousing.sourcing.ban.service.SourcingBanService;
import de.hybris.platform.warehousing.warehouse.filter.WarehousesFilter;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DeclinedWarehousesFilter implements WarehousesFilter
{
    private static Logger LOGGER = LoggerFactory.getLogger(DeclinedWarehousesFilter.class);
    private SourcingBanService sourcingBanService;


    public Set<WarehouseModel> applyFilter(Set<WarehouseModel> warehouses)
    {
        if(CollectionUtils.isNotEmpty(warehouses))
        {
            Collection<SourcingBanModel> existingBans = getSourcingBanService().getSourcingBan(warehouses);
            Collection<WarehouseModel> warehousesToExclude = (Collection<WarehouseModel>)existingBans.stream().map(existingBan -> existingBan.getWarehouse()).collect(Collectors.toList());
            if(!warehousesToExclude.isEmpty())
            {
                LOGGER.info("Filter '{}' excluded '{}' warehouses.", getClass().getSimpleName(), Integer.valueOf(warehousesToExclude.size()));
                warehouses.removeAll(warehousesToExclude);
            }
        }
        return warehouses;
    }


    @Required
    public void setSourcingBanService(SourcingBanService sourcingBanService)
    {
        this.sourcingBanService = sourcingBanService;
    }


    protected SourcingBanService getSourcingBanService()
    {
        return this.sourcingBanService;
    }
}
