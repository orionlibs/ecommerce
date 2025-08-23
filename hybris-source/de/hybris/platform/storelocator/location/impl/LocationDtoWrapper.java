package de.hybris.platform.storelocator.location.impl;

import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.impl.DefaultGPS;
import de.hybris.platform.storelocator.location.Location;
import org.apache.log4j.Logger;

public class LocationDtoWrapper implements Location
{
    private static final Logger LOGGER = Logger.getLogger(LocationDtoWrapper.class.getName());
    private LocationDTO locationDto;
    private AddressData addressData;
    private GPS gps;


    public LocationDtoWrapper(AddressData addressData, GPS gps)
    {
        this.addressData = addressData;
        this.gps = gps;
    }


    public LocationDtoWrapper(LocationDTO locationDto)
    {
        this.locationDto = locationDto;
    }


    public AddressData getAddressData()
    {
        if(this.addressData != null)
        {
            return this.addressData;
        }
        if(this.locationDto != null)
        {
            String street = this.locationDto.getStreet();
            String city = this.locationDto.getCity();
            String buildingNo = this.locationDto.getBuildingNo();
            String postCode = this.locationDto.getPostalCode();
            this.addressData = new AddressData(getName(), street, buildingNo, postCode, city, getCountry());
            return this.addressData;
        }
        return null;
    }


    public String getCountry()
    {
        if(this.locationDto != null)
        {
            return this.locationDto.getCountryIsoCode();
        }
        return null;
    }


    public String getDescription()
    {
        if(this.locationDto != null)
        {
            return this.locationDto.getDescription();
        }
        return null;
    }


    public GPS getGPS()
    {
        if(this.gps != null)
        {
            return this.gps;
        }
        this.gps = extractGPS(this.locationDto);
        return this.gps;
    }


    public String getMapIconUrl()
    {
        if(this.locationDto != null)
        {
            return this.locationDto.getMapIconUrl();
        }
        return null;
    }


    public String getName()
    {
        if(this.locationDto != null)
        {
            return this.locationDto.getName();
        }
        return null;
    }


    public String getTextualAddress()
    {
        AddressData address = getAddressData();
        if(address != null)
        {
            return address.toString();
        }
        return null;
    }


    private GPS extractGPS(LocationDTO locationDTO)
    {
        if(locationDTO != null && locationDTO.getLatitude() != null && locationDTO.getLongitude() != null)
        {
            try
            {
                return (new DefaultGPS()).create(Double.parseDouble(locationDTO.getLatitude()),
                                Double.parseDouble(locationDTO.getLongitude()));
            }
            catch(NumberFormatException e)
            {
                LOGGER.error("Latitude and longitude in the LocationDTO must be numbers");
            }
            catch(GeoLocatorException e)
            {
                LOGGER.error("Latitude and longitude in the LocationDTO must be valid geographical coordinates", (Throwable)e);
            }
        }
        return null;
    }


    public String getType()
    {
        if(this.locationDto != null)
        {
            return this.locationDto.getType();
        }
        return null;
    }
}
