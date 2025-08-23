package de.hybris.platform.customersupportbackoffice.strategies;

import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customersupportbackoffice.data.CsCreateCustomerForm;
import de.hybris.platform.customersupportbackoffice.widgets.DefaultCsFormInitialsFactory;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCsCreateCustomerStrategy implements CsCreateCustomerStrategy
{
    private CustomerAccountService customerAccountService;
    private BaseSiteService baseSiteService;
    private ModelService modelService;
    private CommonI18NService commonI18NService;
    private DefaultCsFormInitialsFactory csFormInitialsFactory;


    public void createCustomer(CsCreateCustomerForm createCustomerForm) throws DuplicateUidException
    {
        CustomerModel customerModel = (CustomerModel)getModelService().create(CustomerModel.class);
        customerModel.setName(createCustomerForm.getName());
        customerModel.setTitle(createCustomerForm.getTitle());
        customerModel.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
        customerModel.setSessionCurrency(getCommonI18NService().getCurrentCurrency());
        customerModel.setUid(createCustomerForm.getEmail().toLowerCase());
        customerModel.setOriginalUid(createCustomerForm.getEmail());
        getBaseSiteService().setCurrentBaseSite(createCustomerForm.getSite(), false);
        getCustomerAccountService().register(customerModel, null);
        if(createCustomerForm.getAddress().getCountry() != null)
        {
            getCustomerAccountService().saveAddressEntry(customerModel, createCustomerForm.getAddress());
            getCsFormInitialsFactory().setLastSavedAddress(createCustomerForm.getAddress());
        }
    }


    protected CustomerAccountService getCustomerAccountService()
    {
        return this.customerAccountService;
    }


    @Required
    public void setCustomerAccountService(CustomerAccountService customerAccountService)
    {
        this.customerAccountService = customerAccountService;
    }


    protected BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }


    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    protected DefaultCsFormInitialsFactory getCsFormInitialsFactory()
    {
        return this.csFormInitialsFactory;
    }


    @Required
    public void setCsFormInitialsFactory(DefaultCsFormInitialsFactory csFormInitialsFactory)
    {
        this.csFormInitialsFactory = csFormInitialsFactory;
    }
}
