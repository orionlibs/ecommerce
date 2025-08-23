package de.hybris.platform.ticket.resolver;

import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultOrderResolver implements TicketAssociatedObjectResolver
{
    protected static final String ORDER_NOT_FOUND_FOR_USER_AND_BASE_STORE = "Order with guid %s not found for current user in current BaseStore";
    private UserService userService;
    private BaseStoreService baseStoreService;
    private CustomerAccountService customerAccountService;


    public AbstractOrderModel getObject(String code, String userUid, String siteUid)
    {
        BaseStoreModel baseStoreModel = StringUtils.isEmpty(siteUid) ? this.baseStoreService.getCurrentBaseStore() : this.baseStoreService.getBaseStoreForUid(siteUid);
        try
        {
            return StringUtils.isBlank(userUid) ? (AbstractOrderModel)this.customerAccountService.getOrderForCode((CustomerModel)this.userService
                            .getCurrentUser(), code, baseStoreModel) : (AbstractOrderModel)this.customerAccountService.getOrderForCode((CustomerModel)this.userService
                            .getUserForUID(userUid), code, baseStoreModel);
        }
        catch(ModelNotFoundException e)
        {
            throw new UnknownIdentifierException(String.format("Order with guid %s not found for current user in current BaseStore", new Object[] {code}));
        }
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    protected BaseStoreService getBaseStoreService()
    {
        return this.baseStoreService;
    }


    protected CustomerAccountService getCustomerAccountService()
    {
        return this.customerAccountService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setBaseStoreService(BaseStoreService baseStoreService)
    {
        this.baseStoreService = baseStoreService;
    }


    @Required
    public void setCustomerAccountService(CustomerAccountService customerAccountService)
    {
        this.customerAccountService = customerAccountService;
    }
}
