package de.hybris.platform.returns;

import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReplacementReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.returns.model.OrderReturnRecordModel;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReplacementEntryModel;
import de.hybris.platform.returns.model.ReplacementOrderModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import java.util.List;
import java.util.Map;

public interface ReturnService
{
    ReturnRequestModel createReturnRequest(OrderModel paramOrderModel);


    List<ReturnRequestModel> getReturnRequests(String paramString);


    String getRMA(ReturnRequestModel paramReturnRequestModel);


    String createRMA(ReturnRequestModel paramReturnRequestModel);


    ReplacementOrderModel getReplacementOrder(String paramString);


    ReplacementOrderModel createReplacementOrder(ReturnRequestModel paramReturnRequestModel);


    void addReplacementOrderEntries(ReplacementOrderModel paramReplacementOrderModel, List<ReplacementEntryModel> paramList);


    ReplacementEntryModel createReplacement(ReturnRequestModel paramReturnRequestModel, AbstractOrderEntryModel paramAbstractOrderEntryModel, String paramString, Long paramLong, ReturnAction paramReturnAction, ReplacementReason paramReplacementReason);


    RefundEntryModel createRefund(ReturnRequestModel paramReturnRequestModel, AbstractOrderEntryModel paramAbstractOrderEntryModel, String paramString, Long paramLong, ReturnAction paramReturnAction, RefundReason paramRefundReason);


    List<ReturnEntryModel> getReturnEntries(ProductModel paramProductModel);


    List<ReturnEntryModel> getReturnEntry(AbstractOrderEntryModel paramAbstractOrderEntryModel);


    List<ReplacementEntryModel> getReplacements(ReturnRequestModel paramReturnRequestModel);


    boolean isReturnable(OrderModel paramOrderModel, AbstractOrderEntryModel paramAbstractOrderEntryModel, long paramLong);


    default long maxReturnQuantity(OrderModel order, AbstractOrderEntryModel entry)
    {
        return entry.getQuantity().longValue();
    }


    void processReturnEntries(List<ReturnEntryModel> paramList);


    void processReplacementOrder(ReplacementOrderModel paramReplacementOrderModel);


    void processRefundOrder(OrderModel paramOrderModel);


    Map<AbstractOrderEntryModel, Long> getAllReturnableEntries(OrderModel paramOrderModel);


    OrderReturnRecordModel getOrderReturnRecordForOrder(OrderModel paramOrderModel) throws OrderReturnException;


    void requestManualPaymentReversalForReturnRequest(ReturnRequestModel paramReturnRequestModel) throws OrderReturnException;


    void requestManualTaxReversalForReturnRequest(ReturnRequestModel paramReturnRequestModel) throws OrderReturnException;
}
