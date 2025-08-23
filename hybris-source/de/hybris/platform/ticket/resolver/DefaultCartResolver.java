package de.hybris.platform.ticket.resolver;

import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCartResolver implements TicketAssociatedObjectResolver
{
    private static final String ORDER_NOT_FOUND_FOR_USER_AND_BASE_STORE = "Order with guid %s not found for current user in current BaseStore";
    private UserService userService;
    private CommerceCartService commerceCartService;


    public AbstractOrderModel getObject(String code, String userUid, String siteUid)
    {
        try
        {
            return StringUtils.isBlank(userUid) ? (AbstractOrderModel)this.commerceCartService.getCartForCodeAndUser(code, this.userService.getCurrentUser()) :
                            (AbstractOrderModel)this.commerceCartService.getCartForCodeAndUser(code, this.userService.getUserForUID(userUid));
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


    protected CommerceCartService getCommerceCartService()
    {
        return this.commerceCartService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setCommerceCartService(CommerceCartService commerceCartService)
    {
        this.commerceCartService = commerceCartService;
    }
}
