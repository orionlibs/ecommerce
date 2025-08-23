package de.hybris.platform.storelocator.location.impl;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.exception.LocationInstantiationException;
import de.hybris.platform.storelocator.impl.DefaultGPS;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.io.Serializable;

public class DistanceUnawareLocation implements Location, Serializable
{
    private final PointOfServiceModel posModel;
    private GPS gps;


    public DistanceUnawareLocation(PointOfServiceModel posModel)
    {
        this.posModel = posModel;
        if(posModel.getLatitude() != null && posModel.getLongitude() != null)
        {
            try
            {
                this.gps = (new DefaultGPS()).create(posModel.getLatitude().doubleValue(), posModel.getLongitude().doubleValue());
            }
            catch(GeoLocatorException e)
            {
                throw new LocationInstantiationException("GPS data contained in the POS entry is invalid", e);
            }
        }
    }


    public AddressData getAddressData()
    {
        AddressModel address = this.posModel.getAddress();
        if(address != null)
        {
            String street = address.getStreetname();
            String city = address.getTown();
            String buildingNo = address.getStreetnumber();
            String postCode = address.getPostalcode();
            return new AddressData(getName(), street, buildingNo, postCode, city, getCountry());
        }
        return null;
    }


    public String getCountry()
    {
        AddressModel address = this.posModel.getAddress();
        if(address != null && address.getCountry() != null)
        {
            return address.getCountry().getIsocode();
        }
        return null;
    }


    public GPS getGPS()
    {
        return this.gps;
    }


    public String getName()
    {
        if(this.posModel != null)
        {
            return this.posModel.getName();
        }
        return null;
    }


    public String getTextualAddress()
    {
        if(getAddressData() != null)
        {
            return getAddressData().toString();
        }
        return null;
    }


    public String getDescription()
    {
        if(this.posModel != null)
        {
            return this.posModel.getDescription();
        }
        return null;
    }


    public String getMapIconUrl()
    {
        if(this.posModel != null && this.posModel.getMapIcon() != null)
        {
            return this.posModel.getMapIcon().getURL();
        }
        return null;
    }


    public String getType()
    {
        if(this.posModel != null)
        {
            return this.posModel.getType().toString();
        }
        return null;
    }
}
