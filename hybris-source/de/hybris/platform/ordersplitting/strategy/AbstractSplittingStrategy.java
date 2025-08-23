package de.hybris.platform.ordersplitting.strategy;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.strategy.impl.OrderEntryGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public abstract class AbstractSplittingStrategy implements SplittingStrategy
{
    private static final Logger LOG = Logger.getLogger(AbstractSplittingStrategy.class);


    public abstract Object getGroupingObject(AbstractOrderEntryModel paramAbstractOrderEntryModel);


    public abstract void afterSplitting(Object paramObject, ConsignmentModel paramConsignmentModel);


    public void afterSplitting(OrderEntryGroup group, ConsignmentModel createdOne)
    {
        afterSplitting(group.getParameter(toString()), createdOne);
    }


    public List<OrderEntryGroup> perform(List<OrderEntryGroup> orderEntryListList)
    {
        List<OrderEntryGroup> newListOrderEntryGroup = new ArrayList<>();
        for(OrderEntryGroup orderEntryList : orderEntryListList)
        {
            Map<Object, OrderEntryGroup> groupingMap = new HashMap<>();
            for(AbstractOrderEntryModel orderEntry : orderEntryList)
            {
                Object groupingObject = getGroupingObject(orderEntry);
                OrderEntryGroup tmpList = groupingMap.get(groupingObject);
                if(tmpList == null)
                {
                    tmpList = orderEntryList.getEmpty();
                    newListOrderEntryGroup.add(tmpList);
                    tmpList.setParameter(toString(), groupingObject);
                    groupingMap.put(groupingObject, tmpList);
                }
                tmpList.add(orderEntry);
            }
            if(LOG.isDebugEnabled())
            {
                logSplittingMap(groupingMap);
            }
        }
        return newListOrderEntryGroup;
    }


    private void logSplittingMap(Map<Object, OrderEntryGroup> groupingMap)
    {
        if(!groupingMap.isEmpty())
        {
            LOG.debug("Resulting grouping map: ");
        }
        for(Map.Entry<Object, OrderEntryGroup> entry : groupingMap.entrySet())
        {
            if(entry != null)
            {
                LOG.debug(" > GroupingObject [" + entry.getKey().getClass().getName() + "] : " + entry.getKey());
                OrderEntryGroup oeGroup = entry.getValue();
                if(oeGroup != null)
                {
                    oeGroup.forEach(oe -> LOG.debug(" --- " + oe));
                }
            }
        }
    }
}
