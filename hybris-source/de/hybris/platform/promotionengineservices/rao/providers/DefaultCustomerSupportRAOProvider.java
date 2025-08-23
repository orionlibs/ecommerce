package de.hybris.platform.promotionengineservices.rao.providers;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customersupport.CommerceCustomerSupportService;
import de.hybris.platform.ruleengineservices.rao.CustomerSupportRAO;
import de.hybris.platform.ruleengineservices.rao.providers.RAOProvider;
import java.util.Collections;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCustomerSupportRAOProvider implements RAOProvider
{
    private CommerceCustomerSupportService commerceCustomerSupportService;


    public Set<?> expandFactModel(Object modelFact)
    {
        CustomerSupportRAO customerSupportRAO = new CustomerSupportRAO();
        customerSupportRAO.setCustomerSupportAgentActive(Boolean.valueOf(getCommerceCustomerSupportService()
                        .isCustomerSupportAgentActive()));
        if(getCommerceCustomerSupportService().isCustomerSupportAgentActive())
        {
            UserModel emulatedCustomer = getCommerceCustomerSupportService().getEmulatedCustomer();
            customerSupportRAO.setCustomerEmulationActive(Boolean.valueOf((emulatedCustomer != null)));
        }
        else
        {
            customerSupportRAO.setCustomerEmulationActive(Boolean.FALSE);
        }
        return Collections.singleton(customerSupportRAO);
    }


    protected CommerceCustomerSupportService getCommerceCustomerSupportService()
    {
        return this.commerceCustomerSupportService;
    }


    @Required
    public void setCommerceCustomerSupportService(CommerceCustomerSupportService commerceCustomerSupportService)
    {
        this.commerceCustomerSupportService = commerceCustomerSupportService;
    }
}
