package de.hybris.platform.basecommerce.model;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;

public class AddressLine2Attribute extends AbstractDynamicAttributeHandler<String, AddressModel>
{
    public String get(AddressModel addressModel)
    {
        if(addressModel == null)
        {
            throw new IllegalArgumentException("address model is required");
        }
        return addressModel.getStreetnumber();
    }


    public void set(AddressModel addressModel, String value)
    {
        if(addressModel != null)
        {
            addressModel.setStreetnumber(value);
        }
    }
}
