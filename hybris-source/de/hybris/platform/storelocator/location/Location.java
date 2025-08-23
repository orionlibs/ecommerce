package de.hybris.platform.storelocator.location;

import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.data.AddressData;

public interface Location
{
    String getName();


    String getDescription();


    GPS getGPS();


    String getCountry();


    String getTextualAddress();


    AddressData getAddressData();


    String getMapIconUrl();


    String getType();
}
