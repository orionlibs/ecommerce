package de.hybris.platform.ordercancel.impl.denialstrategies;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ordercancel.OrderCancelDenialReason;
import de.hybris.platform.ordercancel.OrderCancelDenialStrategy;
import de.hybris.platform.ordercancel.model.OrderCancelConfigModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

public class PartialCancelRulesViolationStrategy extends AbstractCancelDenialStrategy implements OrderCancelDenialStrategy
{
    public OrderCancelDenialReason getCancelDenialReason(OrderCancelConfigModel configuration, OrderModel order, PrincipalModel requester, boolean partialCancel, boolean partialEntryCancel)
    {
        boolean partialCancelViolation;
        ServicesUtil.validateParameterNotNull(configuration, "Parameter configuration must not be null");
        if(partialCancel)
        {
            if(configuration.isPartialCancelAllowed())
            {
                if(partialEntryCancel)
                {
                    partialCancelViolation = !configuration.isPartialOrderEntryCancelAllowed();
                }
                else
                {
                    partialCancelViolation = false;
                }
            }
            else
            {
                partialCancelViolation = true;
            }
        }
        else
        {
            partialCancelViolation = false;
        }
        if(partialCancelViolation)
        {
            return getReason();
        }
        return null;
    }
}
