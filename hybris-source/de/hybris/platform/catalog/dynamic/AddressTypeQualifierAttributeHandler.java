package de.hybris.platform.catalog.dynamic;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;

public class AddressTypeQualifierAttributeHandler implements DynamicAttributeHandler<String, AddressModel>
{
    private static final String DELIMITER = "/";


    public String get(AddressModel model)
    {
        StringBuilder typeQualifer = new StringBuilder();
        if(Boolean.TRUE.equals(model.getUnloadingAddress()))
        {
            typeQualifer.append(localize("address.is.unloading"));
        }
        if(Boolean.TRUE.equals(model.getShippingAddress()))
        {
            if(typeQualifer.length() > 0)
            {
                typeQualifer.append("/");
            }
            typeQualifer.append(localize("address.is.shipping"));
        }
        if(Boolean.TRUE.equals(model.getBillingAddress()))
        {
            if(typeQualifer.length() > 0)
            {
                typeQualifer.append("/");
            }
            typeQualifer.append(localize("address.is.billing"));
        }
        if(Boolean.TRUE.equals(model.getContactAddress()))
        {
            if(typeQualifer.length() > 0)
            {
                typeQualifer.append("/");
            }
            typeQualifer.append(localize("address.is.contact"));
        }
        if(typeQualifer.length() == 0)
        {
            typeQualifer.append("/");
        }
        return typeQualifer.toString();
    }


    private String localize(String key)
    {
        return Localization.getLocalizedString(key);
    }


    public void set(AddressModel model, String value)
    {
        throw new UnsupportedOperationException();
    }
}
