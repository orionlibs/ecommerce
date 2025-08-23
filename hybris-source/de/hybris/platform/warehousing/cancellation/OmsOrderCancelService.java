package de.hybris.platform.warehousing.cancellation;

import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import java.util.List;

public interface OmsOrderCancelService
{
    List<OrderCancelEntry> processOrderCancel(OrderCancelRecordEntryModel paramOrderCancelRecordEntryModel) throws OrderCancelException;
}
