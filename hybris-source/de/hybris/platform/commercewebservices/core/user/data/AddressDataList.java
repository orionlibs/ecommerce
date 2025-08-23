package de.hybris.platform.commercewebservices.core.user.data;

import de.hybris.platform.commercefacades.user.data.AddressData;
import java.io.Serializable;
import java.util.List;

public class AddressDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<AddressData> addresses;


    public void setAddresses(List<AddressData> addresses)
    {
        this.addresses = addresses;
    }


    public List<AddressData> getAddresses()
    {
        return this.addresses;
    }
}
