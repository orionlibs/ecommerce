package com.hybris.cis.api.subscription.mock.util;

import com.hybris.cis.api.model.CisAddress;
import com.hybris.cis.api.subscription.mock.data.AddressMock;

public final class AddressMockConverter
{
    private AddressMockConverter() throws IllegalAccessException
    {
        throw new IllegalAccessException(String.format("Utility class %s may not be instantiated.", new Object[] {AddressMockConverter.class
                        .getSimpleName()}));
    }


    public static CisAddress convert(AddressMock source)
    {
        CisAddress target = new CisAddress();
        if(source != null)
        {
            target.setTitle(source.getTitle());
            target.setFirstName(source.getFirstName());
            target.setLastName(source.getLastName());
            target.setAddressLine1(source.getAddr1());
            target.setAddressLine2(source.getAddr2());
            target.setCity(source.getCity());
            target.setZipCode(source.getPostalCode());
            target.setCountry(source.getCountry());
            target.setEmail(source.getEmailAddress());
        }
        return target;
    }


    public static AddressMock reverseConvert(CisAddress source)
    {
        AddressMock target = new AddressMock();
        if(source != null)
        {
            target.setTitle(source.getTitle());
            target.setFirstName(source.getFirstName());
            target.setLastName(source.getLastName());
            target.setAddr1(source.getAddressLine1());
            target.setAddr2(source.getAddressLine2());
            target.setCity(source.getCity());
            target.setPostalCode(source.getZipCode());
            target.setCountry(source.getCountry());
            target.setEmailAddress(source.getEmail());
        }
        return target;
    }
}
