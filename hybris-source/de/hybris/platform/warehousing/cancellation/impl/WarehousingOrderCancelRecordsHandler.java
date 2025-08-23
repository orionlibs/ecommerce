package de.hybris.platform.warehousing.cancellation.impl;

import de.hybris.platform.basecommerce.enums.OrderCancelEntryStatus;
import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.OrderCancelResponse;
import de.hybris.platform.ordercancel.exceptions.OrderCancelRecordsHandlerException;
import de.hybris.platform.ordercancel.impl.DefaultOrderCancelRecordsHandler;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.ordercancel.model.OrderEntryCancelRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordModel;

public class WarehousingOrderCancelRecordsHandler extends DefaultOrderCancelRecordsHandler
{
    public OrderCancelRecordEntryModel updateRecordEntry(OrderCancelResponse response) throws OrderCancelRecordsHandlerException
    {
        if(response == null)
        {
            throw new IllegalArgumentException("Cancel response cannot be null");
        }
        if(response.getOrder() == null)
        {
            throw new IllegalArgumentException("Cancel response contains no order reference");
        }
        OrderModel order = response.getOrder();
        OrderCancelRecordEntryModel currentEntry = getPendingCancelRecordEntry(order);
        switch(null.$SwitchMap$de$hybris$platform$ordercancel$OrderCancelResponse$ResponseStatus[response.getResponseStatus().ordinal()])
        {
            case 1:
                currentEntry.setCancelResult(OrderCancelEntryStatus.FULL);
                currentEntry.setStatus(OrderModificationEntryStatus.SUCCESSFULL);
                break;
            case 2:
                currentEntry.setCancelResult(OrderCancelEntryStatus.PARTIAL);
                currentEntry.setStatus(OrderModificationEntryStatus.SUCCESSFULL);
                break;
            case 3:
                currentEntry.setCancelResult(OrderCancelEntryStatus.DENIED);
                currentEntry.setRefusedMessage(response.getNotes());
                currentEntry.setStatus(OrderModificationEntryStatus.SUCCESSFULL);
                break;
            case 4:
                currentEntry.setStatus(OrderModificationEntryStatus.FAILED);
                currentEntry.setFailedMessage(response.getNotes());
                break;
        }
        response.getEntriesToCancel().forEach(responseEntry -> {
            OrderEntryCancelRecordEntryModel orderEntryRecord = getOrderCancelDao().getOrderEntryCancelRecord((OrderEntryModel)responseEntry.getOrderEntry(), currentEntry);
            Integer previousCancelledQty = (orderEntryRecord.getCancelledQuantity() != null) ? orderEntryRecord.getCancelledQuantity() : Integer.valueOf(0);
            Integer newCancelledQty = Integer.valueOf(previousCancelledQty.intValue() + (int)responseEntry.getCancelQuantity());
            orderEntryRecord.setCancelledQuantity(newCancelledQty);
            getModelService().save(orderEntryRecord);
        });
        getModelService().save(currentEntry);
        OrderModificationRecordModel modificationRecord = currentEntry.getModificationRecord();
        modificationRecord.setInProgress(false);
        getModelService().save(modificationRecord);
        return currentEntry;
    }
}
