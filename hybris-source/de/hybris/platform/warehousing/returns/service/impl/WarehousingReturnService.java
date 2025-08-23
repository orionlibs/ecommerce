package de.hybris.platform.warehousing.returns.service.impl;

import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.returns.impl.DefaultReturnService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

public class WarehousingReturnService extends DefaultReturnService
{
    public ReturnRequestModel createReturnRequest(OrderModel order)
    {
        validateReturnRequest(order);
        ReturnRequestModel returnRequest = super.createReturnRequest(order);
        finalizeReturnRequest(returnRequest);
        return returnRequest;
    }


    public RefundEntryModel createRefund(ReturnRequestModel request, AbstractOrderEntryModel entry, String notes, Long expectedQuantity, ReturnAction action, RefundReason reason)
    {
        validateRefund(request, (OrderEntryModel)entry, expectedQuantity, action, reason);
        RefundEntryModel refundEntry = (RefundEntryModel)getModelService().create(RefundEntryModel.class);
        refundEntry.setOrderEntry(entry);
        refundEntry.setAction(action);
        refundEntry.setNotes(notes);
        refundEntry.setReason(reason);
        refundEntry.setReturnRequest(request);
        refundEntry.setExpectedQuantity(expectedQuantity);
        refundEntry.setStatus(ReturnStatus.WAIT);
        getModelService().save(refundEntry);
        getModelService().save(request);
        boolean isInStore = refundEntry.getReturnRequest().getReturnEntries().stream().allMatch(entryModel -> entryModel.getAction().equals(ReturnAction.IMMEDIATE));
        finalizeRefund(refundEntry, isInStore);
        return refundEntry;
    }


    protected void validateReturnRequest(OrderModel order)
    {
        ServicesUtil.validateParameterNotNull(order, "Parameter order cannot be null");
    }


    protected void finalizeReturnRequest(ReturnRequestModel returnRequest)
    {
        returnRequest.setStatus(ReturnStatus.APPROVAL_PENDING);
        createRMA(returnRequest);
    }


    protected void validateRefund(ReturnRequestModel request, OrderEntryModel entry, Long expectedQuantity, ReturnAction action, RefundReason reason)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("request", request);
        ServicesUtil.validateParameterNotNullStandardMessage("entry", entry);
        ServicesUtil.validateParameterNotNullStandardMessage("expectedQuantity", expectedQuantity);
        ServicesUtil.validateParameterNotNullStandardMessage("action", action);
        ServicesUtil.validateParameterNotNullStandardMessage("reason", reason);
        if(expectedQuantity.longValue() <= 0L)
        {
            throw new IllegalArgumentException("Expected quantity must be above 0");
        }
        OrderModel order = request.getOrder();
        if(!order.equals(entry.getOrder()))
        {
            throw new IllegalArgumentException("Order entry is not part of the order");
        }
        if(!isReturnable(order, (AbstractOrderEntryModel)entry, expectedQuantity.longValue()))
        {
            throw new IllegalArgumentException("Item is not returnable for this quantity");
        }
    }


    protected void finalizeRefund(RefundEntryModel refundEntry, boolean isInStore)
    {
        if(isInStore)
        {
            refundEntry.getReturnRequest().setStatus(ReturnStatus.RECEIVED);
            refundEntry.setStatus(ReturnStatus.RECEIVED);
            refundEntry.setReceivedQuantity(refundEntry.getExpectedQuantity());
        }
        else
        {
            refundEntry.getReturnRequest().setStatus(ReturnStatus.APPROVAL_PENDING);
            refundEntry.setStatus(ReturnStatus.APPROVAL_PENDING);
            refundEntry.setReceivedQuantity(Long.valueOf(0L));
        }
        getModelService().save(refundEntry);
        getModelService().save(refundEntry.getReturnRequest());
    }
}
