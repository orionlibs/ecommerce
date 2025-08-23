package de.hybris.platform.customersupport;

import de.hybris.platform.core.model.user.UserModel;

public interface CommerceCustomerSupportService
{
    boolean isCustomerSupportAgentActive();


    UserModel getEmulatedCustomer();


    UserModel getAgent();
}
