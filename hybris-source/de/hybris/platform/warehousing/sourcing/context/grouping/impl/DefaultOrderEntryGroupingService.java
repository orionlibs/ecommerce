package de.hybris.platform.warehousing.sourcing.context.grouping.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.warehousing.sourcing.context.grouping.OrderEntryGroup;
import de.hybris.platform.warehousing.sourcing.context.grouping.OrderEntryGroupingService;
import de.hybris.platform.warehousing.sourcing.context.grouping.OrderEntryMatcher;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultOrderEntryGroupingService implements OrderEntryGroupingService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderEntryGroupingService.class);


    public Set<OrderEntryGroup> splitOrderByMatchers(AbstractOrderModel order, Collection<OrderEntryMatcher> matchers)
    {
        Preconditions.checkArgument((matchers != null), "Matchers collection cannot be null.");
        LOGGER.debug("Start creating order entry groups");
        Set<OrderEntryGroup> finalSet = new HashSet<>();
        OrderEntryGroup initialGroup = new OrderEntryGroup(excludeCompletedEntries(order));
        finalSet.add(initialGroup);
        for(OrderEntryMatcher matcher : matchers)
        {
            LOGGER.debug("Splitting order entry groups using matcher '{}'", matcher.getClass().getSimpleName());
            finalSet = splitGroupsByMatcher(finalSet, matcher);
        }
        return finalSet;
    }


    public Set<OrderEntryGroup> splitGroupsByMatcher(Set<OrderEntryGroup> groups, OrderEntryMatcher matcher)
    {
        Set<OrderEntryGroup> result = (Set<OrderEntryGroup>)groups.stream().map(group -> splitGroupByMatcher(group, matcher)).flatMap(Collection::stream).collect(Collectors.toSet());
        return (result == null) ? new HashSet<>() : result;
    }


    public Set<OrderEntryGroup> splitGroupByMatcher(OrderEntryGroup group, OrderEntryMatcher matcher)
    {
        Map<Object, OrderEntryGroup> mapper = new HashMap<>();
        for(AbstractOrderEntryModel orderEntry : group.getEntries())
        {
            OrderEntryGroup mapEntry = mapper.get(matcher.getMatchingObject(orderEntry));
            if(mapEntry == null)
            {
                mapper.put(matcher.getMatchingObject(orderEntry), new OrderEntryGroup(Sets.newHashSet((Object[])new AbstractOrderEntryModel[] {orderEntry})));
                continue;
            }
            mapEntry.add(orderEntry);
        }
        return Sets.newHashSet(mapper.values());
    }


    protected List<AbstractOrderEntryModel> excludeCompletedEntries(AbstractOrderModel order)
    {
        return (List<AbstractOrderEntryModel>)order.getEntries().stream().filter(orderEntry -> (((OrderEntryModel)orderEntry).getQuantityUnallocated().longValue() > 0L))
                        .collect(Collectors.toList());
    }
}
