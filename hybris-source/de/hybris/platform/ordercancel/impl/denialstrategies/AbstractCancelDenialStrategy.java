package de.hybris.platform.ordercancel.impl.denialstrategies;

import de.hybris.platform.ordercancel.OrderCancelDenialReason;

public class AbstractCancelDenialStrategy
{
    private OrderCancelDenialReason reason;


    public OrderCancelDenialReason getReason()
    {
        return this.reason;
    }


    public void setReason(OrderCancelDenialReason reason)
    {
        this.reason = reason;
    }
}
