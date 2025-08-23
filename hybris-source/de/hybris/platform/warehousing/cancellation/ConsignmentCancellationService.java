package de.hybris.platform.warehousing.cancellation;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordercancel.OrderCancelResponse;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import java.util.Map;

public interface ConsignmentCancellationService
{
    void processConsignmentCancellation(OrderCancelResponse paramOrderCancelResponse);


    Map<AbstractOrderEntryModel, Long> cancelConsignment(ConsignmentModel paramConsignmentModel, OrderCancelResponse paramOrderCancelResponse);
}
