package de.hybris.platform.ordersplitting.strategy;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.strategy.impl.OrderEntryGroup;
import java.util.List;

public interface SplittingStrategy
{
    List<OrderEntryGroup> perform(List<OrderEntryGroup> paramList);


    void afterSplitting(OrderEntryGroup paramOrderEntryGroup, ConsignmentModel paramConsignmentModel);
}
