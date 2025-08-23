package de.hybris.platform.ordercancel.impl.denialstrategies;

import de.hybris.platform.ordercancel.OrderCancelStateMappingStrategy;

public interface StateMappingStrategyDependent
{
    void setStateMappingStrategy(OrderCancelStateMappingStrategy paramOrderCancelStateMappingStrategy);
}
