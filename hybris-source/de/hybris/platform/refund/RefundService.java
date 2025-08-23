package de.hybris.platform.refund;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.returns.OrderReturnRecordsHandlerException;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import java.util.List;

public interface RefundService
{
    OrderModel createRefundOrderPreview(OrderModel paramOrderModel);


    void apply(List<RefundEntryModel> paramList, OrderModel paramOrderModel);


    void apply(OrderModel paramOrderModel, ReturnRequestModel paramReturnRequestModel) throws OrderReturnRecordsHandlerException;


    List<RefundEntryModel> getRefunds(ReturnRequestModel paramReturnRequestModel);
}
