package de.hybris.platform.customersupportbackoffice.strategies;

import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.customersupportbackoffice.data.CsCreateCustomerForm;

public interface CsCreateCustomerStrategy
{
    void createCustomer(CsCreateCustomerForm paramCsCreateCustomerForm) throws DuplicateUidException;
}
