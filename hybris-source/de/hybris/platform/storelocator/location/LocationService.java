package de.hybris.platform.storelocator.location;

import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.GPS;
import java.util.List;

public interface LocationService<T extends Location>
{
    List<T> getLocationsNearby(GPS paramGPS, double paramDouble);


    List<T> getLocationsNearby(GPS paramGPS, double paramDouble, BaseStoreModel paramBaseStoreModel);


    boolean saveOrUpdateLocation(T paramT);


    boolean deleteLocation(T paramT);


    T getLocationByName(String paramString);


    T getLocation(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, boolean paramBoolean);


    List<T> getLocationsForPoint(GPS paramGPS, int paramInt, BaseStoreModel paramBaseStoreModel);


    List<T> getLocationsForSearch(String paramString1, String paramString2, int paramInt, BaseStoreModel paramBaseStoreModel);


    List<T> getLocationsForPostcode(String paramString1, String paramString2, int paramInt, BaseStoreModel paramBaseStoreModel);


    List<T> getLocationsForTown(String paramString, int paramInt, BaseStoreModel paramBaseStoreModel);


    List<T> getSortedLocationsNearby(GPS paramGPS, double paramDouble, BaseStoreModel paramBaseStoreModel);
}
