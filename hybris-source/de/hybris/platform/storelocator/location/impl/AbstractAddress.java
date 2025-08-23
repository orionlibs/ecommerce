package de.hybris.platform.storelocator.location.impl;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.io.Serializable;

public abstract class AbstractAddress implements Serializable
{
    private AddressData addressData;
    private GPS gps;
    private String textualAddress;
    private transient Object owner;
    private boolean gpsUpdate = false;


    protected AbstractAddress()
    {
    }


    protected AbstractAddress(String plzOrEquivalent)
    {
        this.addressData = new AddressData(plzOrEquivalent);
    }


    protected AbstractAddress(String name, String street, String buildingNo, String city, String plzOrEquivalent, String countryCode)
    {
        this();
        this.addressData = new AddressData(name, street, buildingNo, plzOrEquivalent, city, countryCode);
    }


    public AddressData getAddressData()
    {
        return this.addressData;
    }


    protected String getAllowedChars()
    {
        return "0-9a-zA-Z.-:/\\s\\t\\n";
    }


    public String getPlzOrEquivalent()
    {
        return this.addressData.getZip();
    }


    public GPS getGps()
    {
        return this.gps;
    }


    public void setGps(GPS gps)
    {
        this.gps = gps;
    }


    public String getTextualAddress()
    {
        return this.textualAddress;
    }


    public abstract void setTextualAddress(String... paramVarArgs);


    public Object getOwner()
    {
        return this.owner;
    }


    protected AddressData extractAddressData(PointOfServiceModel pos)
    {
        AddressData newAddressData = new AddressData();
        AddressModel addressModel = pos.getAddress();
        newAddressData.setStreet(addressModel.getStreetname());
        newAddressData.setBuilding(addressModel.getStreetnumber());
        newAddressData.setCity(addressModel.getTown());
        newAddressData.setCountryCode(addressModel.getCountry().getIsocode());
        newAddressData.setZip(addressModel.getPostalcode());
        return newAddressData;
    }


    public boolean isGpsUpdate()
    {
        return this.gpsUpdate;
    }


    public void setGpsUpdate(boolean gpsUpdate)
    {
        this.gpsUpdate = gpsUpdate;
    }
}
