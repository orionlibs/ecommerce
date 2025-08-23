package de.hybris.platform.warehousing.warehouse.service;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.util.Collection;

public interface WarehousingWarehouseService extends WarehouseService
{
    Collection<WarehouseModel> getWarehousesByBaseStoreDeliveryCountry(BaseStoreModel paramBaseStoreModel, CountryModel paramCountryModel);


    boolean isWarehouseInPoS(WarehouseModel paramWarehouseModel, PointOfServiceModel paramPointOfServiceModel);
}
