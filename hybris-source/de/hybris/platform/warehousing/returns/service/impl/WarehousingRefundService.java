package de.hybris.platform.warehousing.returns.service.impl;

import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.refund.impl.DefaultRefundService;
import de.hybris.platform.returns.OrderReturnRecordsHandlerException;
import de.hybris.platform.returns.model.OrderReturnRecordEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.apache.commons.collections4.CollectionUtils;

public class WarehousingRefundService extends DefaultRefundService
{
    public void apply(OrderModel previewOrder, ReturnRequestModel request) throws OrderReturnRecordsHandlerException
    {
        ServicesUtil.validateParameterNotNull(previewOrder, "Preview Order cannot be null");
        ServicesUtil.validateParameterNotNull(request, "Return Request cannot be null");
        ServicesUtil.validateParameterNotNull(request.getOrder(), "Order cannot be null inside Return request ");
        OrderModel finalOrder = request.getOrder();
        OrderReturnRecordEntryModel orderReturnRecordEntryModel = getModificationHandler().createRefundEntry(finalOrder, getRefunds(request), "Refund request for order: " + finalOrder.getCode());
        orderReturnRecordEntryModel.setReturnRequest(request);
        getModelService().save(orderReturnRecordEntryModel);
        if(CollectionUtils.isNotEmpty(previewOrder.getEntries()))
        {
            previewOrder.getEntries().stream().forEach(previewEntry -> {
                AbstractOrderEntryModel originalEntry = getEntry((AbstractOrderModel)finalOrder, previewEntry.getEntryNumber());
                long newQuantity = previewEntry.getQuantity().longValue();
                originalEntry.setQuantity(Long.valueOf(newQuantity));
                originalEntry.setCalculated(Boolean.FALSE);
                if(newQuantity <= 0L)
                {
                    originalEntry.setQuantityStatus(OrderEntryStatus.DEAD);
                }
                getModelService().save(originalEntry);
            });
        }
    }
}
