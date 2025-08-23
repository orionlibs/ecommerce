package de.hybris.platform.warehousing.cancellation.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ordercancel.OrderCancelCancelableEntriesStrategy;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class CapturePaymentOnConsignmentOrderCancelableEntriesStrategy implements OrderCancelCancelableEntriesStrategy
{
    private Collection<ConsignmentStatus> notCancelableConsignmentStatusList;


    public Map<AbstractOrderEntryModel, Long> getAllCancelableEntries(OrderModel order, PrincipalModel requestor)
    {
        return (Map<AbstractOrderEntryModel, Long>)order.getEntries().stream().filter(entry -> (calculateCancelableQtyForOrderEntry((OrderEntryModel)entry) > 0L))
                        .collect(Collectors.toMap(entry -> entry, entry -> Long.valueOf(calculateCancelableQtyForOrderEntry((OrderEntryModel)entry))));
    }


    protected long calculateCancelableQtyForOrderEntry(OrderEntryModel orderEntryModel)
    {
        long cancelableConsignmentQty = orderEntryModel.getOrder().getConsignments().stream().filter(consignmentModel -> !getNotCancelableConsignmentStatusList().contains(consignmentModel.getStatus())).flatMap(consignmentModel -> consignmentModel.getConsignmentEntries().stream())
                        .filter(consignmentEntryModel -> consignmentEntryModel.getOrderEntry().getEntryNumber().equals(orderEntryModel.getEntryNumber())).mapToLong(ConsignmentEntryModel::getQuantity).sum();
        return orderEntryModel.getQuantityUnallocated().longValue() + cancelableConsignmentQty;
    }


    protected Collection<ConsignmentStatus> getNotCancelableConsignmentStatusList()
    {
        return this.notCancelableConsignmentStatusList;
    }


    @Required
    public void setNotCancelableConsignmentStatusList(Collection<ConsignmentStatus> notCancelableConsignmentStatusList)
    {
        this.notCancelableConsignmentStatusList = notCancelableConsignmentStatusList;
    }
}
