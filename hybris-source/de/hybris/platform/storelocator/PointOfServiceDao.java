package de.hybris.platform.storelocator;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface PointOfServiceDao extends Dao
{
    Collection<PointOfServiceModel> getAllPos();


    Collection<PointOfServiceModel> getAllGeocodedPOS(GPS paramGPS, double paramDouble, BaseStoreModel paramBaseStoreModel);


    Collection<PointOfServiceModel> getAllGeocodedPOS(GPS paramGPS, double paramDouble);


    PointOfServiceModel getPosByName(String paramString);


    Collection<PointOfServiceModel> getPosToGeocode(int paramInt);


    Collection<PointOfServiceModel> getPosToGeocode();


    Map<CountryModel, Integer> getPointOfServiceCountPerCountryForStore(BaseStoreModel paramBaseStoreModel);


    Map<RegionModel, Integer> getPointOfServiceRegionCountForACountryAndStore(CountryModel paramCountryModel, BaseStoreModel paramBaseStoreModel);


    List<PointOfServiceModel> getPosForCountry(String paramString, BaseStoreModel paramBaseStoreModel);


    List<PointOfServiceModel> getPosForRegion(String paramString1, String paramString2, BaseStoreModel paramBaseStoreModel);
}
