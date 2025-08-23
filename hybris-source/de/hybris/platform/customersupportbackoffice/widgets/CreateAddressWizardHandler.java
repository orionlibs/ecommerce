package de.hybris.platform.customersupportbackoffice.widgets;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowController;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.customersupportbackoffice.data.CsCreateAddressForm;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class CreateAddressWizardHandler implements FlowActionHandler
{
    private CustomerAccountService customerAccountService;
    private ModelService modelService;


    public void perform(CustomType customType, FlowActionHandlerAdapter flowActionHandlerAdapter, Map<String, String> map)
    {
        CsCreateAddressForm form = (CsCreateAddressForm)flowActionHandlerAdapter.getWidgetInstanceManager().getModel().getValue("customersupport_backoffice_addressForm", CsCreateAddressForm.class);
        this.customerAccountService.saveAddressEntry(form.getOwner(), populateAddress(form));
        flowActionHandlerAdapter.getWidgetInstanceManager().getModel().removeAllObservers();
        ConfigurableFlowController controller = (ConfigurableFlowController)flowActionHandlerAdapter.getWidgetInstanceManager().getWidgetslot().getAttribute("widgetController");
        controller.setValue("finished", Boolean.TRUE);
        controller.getBreadcrumbDiv().invalidate();
        flowActionHandlerAdapter.done();
    }


    protected AddressModel populateAddress(CsCreateAddressForm form)
    {
        AddressModel address = (AddressModel)getModelService().create(AddressModel.class);
        address.setTitle(form.getTitle());
        address.setFirstname(form.getFirstName());
        address.setLastname(form.getLastName());
        address.setStreetname(form.getAddressLine1());
        address.setStreetnumber(form.getAddressLine2());
        address.setTown(form.getTown());
        address.setCountry(form.getCountry());
        address.setRegion(form.getRegion());
        address.setPostalcode(form.getPostalcode());
        address.setPhone1(form.getPhone1());
        address.setShippingAddress(form.getShippingAddress());
        address.setBillingAddress(form.getBillingAddress());
        address.setVisibleInAddressBook(Boolean.TRUE);
        return address;
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


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
