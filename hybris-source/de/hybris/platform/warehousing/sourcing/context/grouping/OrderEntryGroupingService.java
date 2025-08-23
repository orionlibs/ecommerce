package de.hybris.platform.warehousing.sourcing.context.grouping;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import java.util.Collection;
import java.util.Set;

public interface OrderEntryGroupingService
{
    Set<OrderEntryGroup> splitOrderByMatchers(AbstractOrderModel paramAbstractOrderModel, Collection<OrderEntryMatcher> paramCollection);


    Set<OrderEntryGroup> splitGroupsByMatcher(Set<OrderEntryGroup> paramSet, OrderEntryMatcher paramOrderEntryMatcher);


    Set<OrderEntryGroup> splitGroupByMatcher(OrderEntryGroup paramOrderEntryGroup, OrderEntryMatcher paramOrderEntryMatcher);
}
