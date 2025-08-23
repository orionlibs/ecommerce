package de.hybris.platform.ordercancel.impl.denialstrategies;

import de.hybris.platform.basecommerce.enums.OrderCancelState;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ordercancel.OrderCancelDenialReason;
import de.hybris.platform.ordercancel.OrderCancelDenialStrategy;
import de.hybris.platform.ordercancel.OrderCancelStateMappingStrategy;
import de.hybris.platform.ordercancel.model.OrderCancelConfigModel;
import java.util.List;
import org.apache.log4j.Logger;

public class OrderStateDenialStrategy extends AbstractCancelDenialStrategy implements OrderCancelDenialStrategy, StateMappingStrategyDependent
{
    private static final Logger LOG = Logger.getLogger(OrderStateDenialStrategy.class.getName());
    private OrderCancelStateMappingStrategy stateMappingStrategy;
    private List<OrderCancelState> partialCancelDeniedStates;
    private List<OrderCancelState> fullCancelDeniedStates;


    public OrderCancelDenialReason getCancelDenialReason(OrderCancelConfigModel configuration, OrderModel order, PrincipalModel requester, boolean partialCancel, boolean partialEntryCancel)
    {
        if(this.stateMappingStrategy == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Missing OrderCancelStateMappingStrategy!");
            }
            return null;
        }
        OrderCancelState currentOrderState = this.stateMappingStrategy.getOrderCancelState(order);
        if(partialCancel)
        {
            if(this.partialCancelDeniedStates.contains(currentOrderState))
            {
                return getReason();
            }
        }
        else if(this.fullCancelDeniedStates.contains(currentOrderState))
        {
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


    public List<OrderCancelState> getPartialCancelDeniedStates()
    {
        return this.partialCancelDeniedStates;
    }


    public void setPartialCancelDeniedStates(List<OrderCancelState> partialCancelDeniedStates)
    {
        this.partialCancelDeniedStates = partialCancelDeniedStates;
    }


    public List<OrderCancelState> getFullCancelDeniedStates()
    {
        return this.fullCancelDeniedStates;
    }


    public void setFullCancelDeniedStates(List<OrderCancelState> fullCancelDeniedStates)
    {
        this.fullCancelDeniedStates = fullCancelDeniedStates;
    }
}
