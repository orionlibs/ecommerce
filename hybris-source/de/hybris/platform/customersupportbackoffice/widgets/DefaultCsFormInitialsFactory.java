package de.hybris.platform.customersupportbackoffice.widgets;

import com.hybris.cockpitng.util.CockpitSessionService;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.customersupportbackoffice.data.CsCloseTicketForm;
import de.hybris.platform.customersupportbackoffice.data.CsCreateAddressForm;
import de.hybris.platform.customersupportbackoffice.data.CsCreateCustomerForm;
import de.hybris.platform.customersupportbackoffice.data.CsCreateTicketForm;
import de.hybris.platform.customersupportbackoffice.data.CsReopenTicketForm;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.ticket.enums.CsEventReason;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.enums.CsResolutionType;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import de.hybris.platform.ticket.service.TicketService;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCsFormInitialsFactory
{
    private static final Logger LOG = Logger.getLogger(DefaultCsFormInitialsFactory.class);
    private TicketService ticketService;
    private UserService userService;
    private AddressModel lastSavedAddress;
    private EnumerationService enumerationService;
    private CockpitSessionService cockpitSessionService;
    private String defaultPriority;
    private String defaultCategory;
    private String defaultIntervention;
    private String defaultReason;
    private String defaultAgentGroup;
    private String defaultRootGroup;


    public CsCreateTicketForm getTicketForm()
    {
        CsCreateTicketForm ticketForm = new CsCreateTicketForm();
        try
        {
            ticketForm.setCategory((CsTicketCategory)this.enumerationService.getEnumerationValue("CsTicketCategory", getDefaultCategory()));
            ticketForm.setPriority((CsTicketPriority)this.enumerationService.getEnumerationValue("CsTicketPriority", getDefaultPriority()));
            ticketForm
                            .setIntervention((CsInterventionType)this.enumerationService.getEnumerationValue("CsInterventionType", getDefaultIntervention()));
            ticketForm.setReason((CsEventReason)this.enumerationService.getEnumerationValue("CsEventReason", getDefaultReason()));
            ticketForm.setAssignedAgent((EmployeeModel)this.userService.getCurrentUser());
            ticketForm.setRootGroup(this.userService.getUserGroupForUID(this.defaultRootGroup));
            Object currentSessionCustomerId = this.cockpitSessionService.getAttribute("sessionContextUID");
            if(null != currentSessionCustomerId)
            {
                ticketForm.setCustomer((CustomerModel)this.userService.getUserForUID(currentSessionCustomerId.toString()));
            }
            List<CsAgentGroupModel> group = (List<CsAgentGroupModel>)this.ticketService.getAgentGroups().stream().filter(m -> m.getUid().equalsIgnoreCase(getDefaultAgentGroup())).collect(Collectors.toList());
            ticketForm.setAssignedGroup(CollectionUtils.isNotEmpty(group) ? group.get(0) : null);
        }
        catch(Exception exp)
        {
            LOG.error("Can't load ticket defaults, please check your conifuration.", exp);
        }
        return ticketForm;
    }


    public CsCreateCustomerForm getCustomerForm()
    {
        CsCreateCustomerForm customerForm = new CsCreateCustomerForm();
        AddressModel addressModel = new AddressModel();
        if(getLastSavedAddress() != null)
        {
            addressModel.setCountry(getLastSavedAddress().getCountry());
        }
        customerForm.setAddress(addressModel);
        return customerForm;
    }


    public CsCreateAddressForm getAddressForm()
    {
        CsCreateAddressForm addressForm = new CsCreateAddressForm();
        Object currentSessionCustomerId = this.cockpitSessionService.getAttribute("sessionContextUID");
        if(null != currentSessionCustomerId)
        {
            CustomerModel customer = (CustomerModel)this.userService.getUserForUID(currentSessionCustomerId.toString());
            String[] cName = customer.getName().split(" ");
            addressForm.setFirstName(cName[0]);
            if(cName.length > 1 && StringUtils.isNotEmpty(cName[1]))
            {
                addressForm.setLastName(cName[1]);
            }
        }
        addressForm.setShippingAddress(Boolean.TRUE);
        addressForm.setBillingAddress(Boolean.FALSE);
        return addressForm;
    }


    public CsCloseTicketForm getCloseTicketForm()
    {
        CsCloseTicketForm closeTicketForm = new CsCloseTicketForm();
        closeTicketForm.setResolution(CsResolutionType.CLOSED);
        return closeTicketForm;
    }


    public CsReopenTicketForm getReopenTicketForm()
    {
        return new CsReopenTicketForm();
    }


    protected TicketService getTicketService()
    {
        return this.ticketService;
    }


    @Required
    public void setTicketService(TicketService ticketService)
    {
        this.ticketService = ticketService;
    }


    public void setLastSavedAddress(AddressModel addressModel)
    {
        if(addressModel != null && addressModel.getCountry() != null)
        {
            synchronized(this)
            {
                this.lastSavedAddress = addressModel;
            }
        }
    }


    protected AddressModel getLastSavedAddress()
    {
        return this.lastSavedAddress;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected EnumerationService getEnumerationService()
    {
        return this.enumerationService;
    }


    @Required
    public void setEnumerationService(EnumerationService enumerationService)
    {
        this.enumerationService = enumerationService;
    }


    public String getDefaultPriority()
    {
        return this.defaultPriority;
    }


    @Required
    public void setDefaultPriority(String defaultPriority)
    {
        this.defaultPriority = defaultPriority;
    }


    public String getDefaultCategory()
    {
        return this.defaultCategory;
    }


    @Required
    public void setDefaultCategory(String defaultCategory)
    {
        this.defaultCategory = defaultCategory;
    }


    public String getDefaultIntervention()
    {
        return this.defaultIntervention;
    }


    @Required
    public void setDefaultIntervention(String defaultIntervention)
    {
        this.defaultIntervention = defaultIntervention;
    }


    public String getDefaultReason()
    {
        return this.defaultReason;
    }


    @Required
    public void setDefaultReason(String defaultReason)
    {
        this.defaultReason = defaultReason;
    }


    protected CockpitSessionService getCockpitSessionService()
    {
        return this.cockpitSessionService;
    }


    public String getDefaultRootGroup()
    {
        return this.defaultRootGroup;
    }


    @Required
    public void setDefaultRootGroup(String defaultRootGroup)
    {
        this.defaultRootGroup = defaultRootGroup;
    }


    @Required
    public void setCockpitSessionService(CockpitSessionService cockpitSessionService)
    {
        this.cockpitSessionService = cockpitSessionService;
    }


    public String getDefaultAgentGroup()
    {
        return this.defaultAgentGroup;
    }


    @Required
    public void setDefaultAgentGroup(String defaultAgentGroup)
    {
        this.defaultAgentGroup = defaultAgentGroup;
    }
}
