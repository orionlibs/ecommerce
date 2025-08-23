package de.hybris.platform.returns;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.returns.model.OrderReturnRecordEntryModel;
import de.hybris.platform.returns.model.OrderReturnRecordModel;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import java.util.List;

public interface OrderReturnRecordHandler
{
    OrderReturnRecordModel getReturnRecord(OrderModel paramOrderModel);


    OrderReturnRecordEntryModel createRefundEntry(OrderModel paramOrderModel, List<RefundEntryModel> paramList, String paramString) throws OrderReturnRecordsHandlerException;


    OrderReturnRecordModel finalizeOrderReturnRecordForReturnRequest(ReturnRequestModel paramReturnRequestModel);


    OrderReturnRecordEntryModel getPendingReturnRecordEntryForReturnRequest(ReturnRequestModel paramReturnRequestModel);
}
