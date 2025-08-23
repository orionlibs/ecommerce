package de.hybris.platform.warehousing.warehouse.service.impl;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.ordersplitting.impl.DefaultWarehouseService;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.atp.strategy.impl.WarehousingWarehouseSelectionStrategy;
import de.hybris.platform.warehousing.warehouse.service.WarehousingWarehouseService;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class DefaultWarehousingWarehouseService extends DefaultWarehouseService implements WarehousingWarehouseService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWarehousingWarehouseService.class);
    private WarehousingWarehouseSelectionStrategy warehousingWarehouseSelectionStrategy;


    public Collection<WarehouseModel> getWarehousesByBaseStoreDeliveryCountry(BaseStoreModel baseStore, CountryModel country) throws IllegalArgumentException
    {
        ServicesUtil.validateParameterNotNull(baseStore, "BaseStore cannot be null.");
        ServicesUtil.validateParameterNotNull(country, "Country cannot be null.");
        Assert.isTrue(CollectionUtils.isNotEmpty(baseStore.getDeliveryCountries()),
                        String.format("Basestore:[%s] does not support delivery to any Country", new Object[] {baseStore.getUid()}));
        Set<WarehouseModel> filteredWarehouses = new HashSet<>();
        if(baseStore.getDeliveryCountries().contains(country))
        {
            filteredWarehouses.addAll(getWarehousingWarehouseSelectionStrategy().getWarehousesForBaseStore(baseStore));
        }
        else
        {
            LOGGER.info("Basestore:[{}] does not support delivery to Country:[{}]", baseStore.getUid(), country.getName());
        }
        return filteredWarehouses;
    }


    public boolean isWarehouseInPoS(WarehouseModel warehouse, PointOfServiceModel pointOfService)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("warehouse", warehouse);
        ServicesUtil.validateParameterNotNullStandardMessage("pointOfService", pointOfService);
        return pointOfService.getWarehouses().stream().anyMatch(w -> w.equals(warehouse));
    }


    protected WarehousingWarehouseSelectionStrategy getWarehousingWarehouseSelectionStrategy()
    {
        return this.warehousingWarehouseSelectionStrategy;
    }


    @Required
    public void setWarehousingWarehouseSelectionStrategy(WarehousingWarehouseSelectionStrategy warehousingWarehouseSelectionStrategy)
    {
        this.warehousingWarehouseSelectionStrategy = warehousingWarehouseSelectionStrategy;
    }
}
