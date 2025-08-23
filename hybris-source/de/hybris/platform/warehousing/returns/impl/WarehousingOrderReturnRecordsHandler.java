package de.hybris.platform.warehousing.returns.impl;

import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.returns.OrderReturnRecordsHandlerException;
import de.hybris.platform.returns.impl.DefaultOrderReturnRecordsHandler;
import de.hybris.platform.returns.model.OrderReturnRecordEntryModel;
import de.hybris.platform.returns.model.OrderReturnRecordModel;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

public class WarehousingOrderReturnRecordsHandler extends DefaultOrderReturnRecordsHandler
{
    public OrderReturnRecordEntryModel createRefundEntry(OrderModel order, List<RefundEntryModel> refunds, String description) throws OrderReturnRecordsHandlerException
    {
        OrderHistoryEntryModel snapshot = createSnaphot(order, description);
        OrderReturnRecordModel returnRecord = getOrCreateReturnRecord(order);
        returnRecord.setInProgress(true);
        getModelService().save(returnRecord);
        return createRefundRecordEntry(order, returnRecord, snapshot, refunds, null);
    }


    protected void finalizeOrderReturnRecord(OrderReturnRecordModel orderReturnRecord)
    {
        ServicesUtil.validateParameterNotNull(orderReturnRecord, "Order Return Record cannot be null");
        if(CollectionUtils.isNotEmpty(orderReturnRecord.getModificationRecordEntries()))
        {
            boolean isAnyRecordEntryInProgress = orderReturnRecord.getModificationRecordEntries().stream().anyMatch(modificationRecordEntry -> OrderModificationEntryStatus.INPROGRESS.equals(modificationRecordEntry.getStatus()));
            orderReturnRecord.setInProgress(isAnyRecordEntryInProgress);
            getModelService().save(orderReturnRecord);
        }
    }


    protected boolean isReturnRecordEntryForReturnRequest(ReturnRequestModel returnRequest, OrderReturnRecordEntryModel orderReturnRecordEntry)
    {
        ServicesUtil.validateParameterNotNull(orderReturnRecordEntry, "Order return record entry cannot be null");
        ServicesUtil.validateParameterNotNull(returnRequest, "Return request cannot be null");
        ServicesUtil.validateParameterNotNull(orderReturnRecordEntry.getReturnRequest(), "Return request cannot be null for OrderReturnRecordEntry");
        return (OrderModificationEntryStatus.INPROGRESS.equals(orderReturnRecordEntry.getStatus()) && returnRequest.getCode()
                        .equals(orderReturnRecordEntry.getReturnRequest().getCode()));
    }
}
