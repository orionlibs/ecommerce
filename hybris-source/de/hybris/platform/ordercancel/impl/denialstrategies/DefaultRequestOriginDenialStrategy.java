package de.hybris.platform.ordercancel.impl.denialstrategies;

import de.hybris.platform.core.Constants;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.ordercancel.OrderCancelDenialReason;
import de.hybris.platform.ordercancel.OrderCancelDenialStrategy;
import de.hybris.platform.ordercancel.model.OrderCancelConfigModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

public class DefaultRequestOriginDenialStrategy extends AbstractCancelDenialStrategy implements OrderCancelDenialStrategy
{
    private TypeService typeService;


    public OrderCancelDenialReason getCancelDenialReason(OrderCancelConfigModel configuration, OrderModel order, PrincipalModel requestor, boolean partialCancel, boolean partialEntryCancel)
    {
        ServicesUtil.validateParameterNotNull(configuration, "Parameter configuration must not be null");
        ComposedTypeModel ctm = this.typeService.getComposedTypeForClass(requestor.getClass());
        boolean isCalledByCustomer = Constants.TYPES.Customer.equals(ctm.getCode());
        if(!configuration.isOrderCancelAllowed() && isCalledByCustomer)
        {
            return getReason();
        }
        return null;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
