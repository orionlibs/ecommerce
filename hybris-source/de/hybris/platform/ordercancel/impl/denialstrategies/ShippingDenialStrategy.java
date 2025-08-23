package de.hybris.platform.ordercancel.impl.denialstrategies;

import de.hybris.platform.basecommerce.enums.OrderCancelState;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ordercancel.OrderCancelDenialReason;
import de.hybris.platform.ordercancel.OrderCancelDenialStrategy;
import de.hybris.platform.ordercancel.OrderCancelStateMappingStrategy;
import de.hybris.platform.ordercancel.model.OrderCancelConfigModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import org.apache.log4j.Logger;

public class ShippingDenialStrategy extends AbstractCancelDenialStrategy implements OrderCancelDenialStrategy, StateMappingStrategyDependent
{
    private static final Logger LOG = Logger.getLogger(ShippingDenialStrategy.class.getName());
    private OrderCancelStateMappingStrategy stateMappingStrategy;
    private List<OrderCancelState> strategyInvolvedStates;


    public OrderCancelDenialReason getCancelDenialReason(OrderCancelConfigModel configuration, OrderModel order, PrincipalModel requester, boolean partialCancel, boolean partialEntryCancel)
    {
        ServicesUtil.validateParameterNotNull(configuration, "Parameter configuration must not be null");
        if(this.stateMappingStrategy == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Missing OrderCancelStateMappingStrategy!");
            }
            return null;
        }
        OrderCancelState currentOrderState = this.stateMappingStrategy.getOrderCancelState(order);
        if(this.strategyInvolvedStates.contains(currentOrderState))
        {
            boolean decision;
            if(partialCancel)
            {
                decision = configuration.isCancelAfterWarehouseAllowed();
            }
            else
            {
                decision = (configuration.isCancelAfterWarehouseAllowed() && configuration.isCompleteCancelAfterShippingStartedAllowed());
            }
            if(decision)
            {
                return null;
            }
            return getReason();
        }
        return null;
    }


    public OrderCancelStateMappingStrategy getStateMappingStrategy()
    {
        return this.stateMappingStrategy;
    }


    public void setStateMappingStrategy(OrderCancelStateMappingStrategy stateMappingStrategy)
    {
        this.stateMappingStrategy = stateMappingStrategy;
    }


    public List<OrderCancelState> getStrategyInvolvedStates()
    {
        return this.strategyInvolvedStates;
    }


    public void setStrategyInvolvedStates(List<OrderCancelState> strategyInvolvedStates)
    {
        this.strategyInvolvedStates = strategyInvolvedStates;
    }
}
