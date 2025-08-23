package de.hybris.platform.customersupport.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customersupport.CommerceCustomerSupportService;

public class DefaultCommerceCustomerSupportService implements CommerceCustomerSupportService
{
    public boolean isCustomerSupportAgentActive()
    {
        return false;
    }


    public UserModel getEmulatedCustomer()
    {
        return null;
    }


    public UserModel getAgent()
    {
        return null;
    }
}
