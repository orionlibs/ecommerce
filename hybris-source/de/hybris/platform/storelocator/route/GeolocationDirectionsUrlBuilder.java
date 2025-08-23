package de.hybris.platform.storelocator.route;

import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.data.AddressData;
import java.util.Map;

public interface GeolocationDirectionsUrlBuilder
{
    String getWebServiceUrl(String paramString, GPS paramGPS1, GPS paramGPS2, Map paramMap);


    String getWebServiceUrl(String paramString, AddressData paramAddressData1, AddressData paramAddressData2, Map paramMap);
}
