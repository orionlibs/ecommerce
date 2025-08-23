package de.hybris.platform.warehousing.atp.strategy.impl;

import com.google.common.collect.Sets;
import de.hybris.platform.commerceservices.stock.strategies.impl.DefaultWarehouseSelectionStrategy;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.warehousing.warehouse.filter.WarehousesFilterProcessor;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Required;

public class WarehousingWarehouseSelectionStrategy extends DefaultWarehouseSelectionStrategy
{
    private WarehousesFilterProcessor warehousesFilterProcessor;


    public List<WarehouseModel> getWarehousesForBaseStore(BaseStoreModel baseStore)
    {
        ServicesUtil.validateParameterNotNull(baseStore, "baseStore must not be null");
        Set<WarehouseModel> warehouses = getWarehousesFilterProcessor().filterLocations(Sets.newHashSet(baseStore.getWarehouses()));
        return (List<WarehouseModel>)Stream.concat(warehouses.stream().filter(warehouse -> warehouse.getDeliveryModes().stream().anyMatch(())), warehouses
                                        .stream().filter(warehouse -> warehouse.getDeliveryModes().isEmpty())).distinct()
                        .collect(Collectors.toList());
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
