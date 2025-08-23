package de.hybris.platform.storelocator.location;

import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.map.Map;

public interface LocationMapService
{
    Map getMapOfLocations(String paramString1, String paramString2, int paramInt, BaseStoreModel paramBaseStoreModel);


    Map getMapOfLocationsForPostcode(String paramString1, String paramString2, int paramInt, BaseStoreModel paramBaseStoreModel);


    Map getMapOfLocationsForTown(String paramString, int paramInt, BaseStoreModel paramBaseStoreModel);


    Map getMapOfLocations(GPS paramGPS, int paramInt, BaseStoreModel paramBaseStoreModel);
}
