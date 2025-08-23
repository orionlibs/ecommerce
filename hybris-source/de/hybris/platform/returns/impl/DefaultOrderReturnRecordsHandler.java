package de.hybris.platform.returns.impl;

import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordercancel.exceptions.OrderCancelDaoException;
import de.hybris.platform.orderhistory.OrderHistoryService;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordModel;
import de.hybris.platform.refund.model.OrderRefundRecordEntryModel;
import de.hybris.platform.returns.OrderReturnRecordHandler;
import de.hybris.platform.returns.OrderReturnRecordsHandlerException;
import de.hybris.platform.returns.dao.OrderReturnDao;
import de.hybris.platform.returns.model.OrderEntryReturnRecordEntryModel;
import de.hybris.platform.returns.model.OrderReturnRecordEntryModel;
import de.hybris.platform.returns.model.OrderReturnRecordModel;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultOrderReturnRecordsHandler implements OrderReturnRecordHandler
{
    private OrderReturnDao orderReturnDao;
    private OrderHistoryService orderHistoryService;
    private ModelService modelService;


    public OrderReturnRecordEntryModel createRefundEntry(OrderModel order, List<RefundEntryModel> refunds, String description) throws OrderReturnRecordsHandlerException
    {
        OrderHistoryEntryModel snapshot = createSnaphot(order, description);
        try
        {
            OrderReturnRecordModel returnRecord = getOrCreateReturnRecord(order);
            if(returnRecord.isInProgress())
            {
                throw new IllegalStateException("Cannot create new Order return request - the order return record indicates: Return already in progress");
            }
            returnRecord.setInProgress(true);
            getModelService().save(returnRecord);
            return createRefundRecordEntry(order, returnRecord, snapshot, refunds, null);
        }
        catch(OrderCancelDaoException e)
        {
            throw new OrderReturnRecordsHandlerException(order.getCode(), e);
        }
    }


    public OrderReturnRecordModel finalizeOrderReturnRecordForReturnRequest(ReturnRequestModel returnRequest)
    {
        OrderReturnRecordEntryModel orderReturnRecordEntry = getPendingReturnRecordEntryForReturnRequest(returnRequest);
        if(orderReturnRecordEntry == null)
        {
            throw new IllegalStateException("Order[" + returnRequest
                            .getOrder().getCode() + "]: return record has no entry with status 'IN PROGRESS'");
        }
        finalizeOrderReturnRecordEntry(orderReturnRecordEntry);
        OrderReturnRecordModel orderReturnRecord = (OrderReturnRecordModel)orderReturnRecordEntry.getModificationRecord();
        finalizeOrderReturnRecord(orderReturnRecord);
        return orderReturnRecord;
    }


    public OrderReturnRecordEntryModel getPendingReturnRecordEntryForReturnRequest(ReturnRequestModel returnRequest)
    {
        OrderReturnRecordModel orderReturnRecord = getPendingReturnRecordForReturnRequest(returnRequest);
        Collection<OrderModificationRecordEntryModel> entries = orderReturnRecord.getModificationRecordEntries();
        if(CollectionUtils.isEmpty(entries))
        {
            throw new IllegalStateException("Order[" + returnRequest.getOrder().getCode() + "]: has no return records");
        }
        OrderReturnRecordEntryModel currentReturnEntry = null;
        for(OrderModificationRecordEntryModel orderModificationRecordEntry : entries)
        {
            OrderReturnRecordEntryModel orderReturnRecordEntry = (OrderReturnRecordEntryModel)orderModificationRecordEntry;
            if(isReturnRecordEntryForReturnRequest(returnRequest, orderReturnRecordEntry))
            {
                if(currentReturnEntry == null)
                {
                    currentReturnEntry = orderReturnRecordEntry;
                    continue;
                }
                throw new IllegalStateException("Return request[" + returnRequest.getCode() + "]: has more than one return record entries with status 'IN PROGRESS'");
            }
        }
        return currentReturnEntry;
    }


    protected OrderReturnRecordModel getPendingReturnRecordForReturnRequest(ReturnRequestModel returnRequest)
    {
        ServicesUtil.validateParameterNotNull(returnRequest, "Return Request cannot be null");
        ServicesUtil.validateParameterNotNull(returnRequest.getOrder(), "Order cannot be null ");
        OrderModel orderModel = returnRequest.getOrder();
        OrderReturnRecordModel orderReturnRecord = getReturnRecord(orderModel);
        if(orderReturnRecord == null || !orderReturnRecord.isInProgress())
        {
            throw new IllegalStateException("Order[" + orderModel.getCode() + "]: return is not currently in progress");
        }
        return orderReturnRecord;
    }


    protected boolean isReturnRecordEntryForReturnRequest(ReturnRequestModel returnRequest, OrderReturnRecordEntryModel orderReturnRecordEntry)
    {
        ServicesUtil.validateParameterNotNull(orderReturnRecordEntry, "Order return record entry cannot be null");
        return OrderModificationEntryStatus.INPROGRESS.equals(orderReturnRecordEntry.getStatus());
    }


    protected void finalizeOrderReturnRecordEntry(OrderReturnRecordEntryModel orderReturnRecordEntry)
    {
        ServicesUtil.validateParameterNotNull(orderReturnRecordEntry, "Order return record entry cannot be null");
        for(OrderEntryModificationRecordEntryModel orderEntryModificationRecordEntry : orderReturnRecordEntry
                        .getOrderEntriesModificationEntries())
        {
            OrderEntryReturnRecordEntryModel orderEntryReturnEntry = (OrderEntryReturnRecordEntryModel)orderEntryModificationRecordEntry;
            orderEntryReturnEntry.setReturnedQuantity(orderEntryReturnEntry.getExpectedQuantity());
            getModelService().save(orderEntryReturnEntry);
        }
        orderReturnRecordEntry.setStatus(OrderModificationEntryStatus.SUCCESSFULL);
        getModelService().save(orderReturnRecordEntry);
    }


    protected void finalizeOrderReturnRecord(OrderReturnRecordModel orderReturnRecord)
    {
        ServicesUtil.validateParameterNotNull(orderReturnRecord, "Order return record cannot be null");
        orderReturnRecord.setInProgress(false);
        getModelService().save(orderReturnRecord);
    }


    protected OrderReturnRecordEntryModel createRefundRecordEntry(OrderModel order, OrderReturnRecordModel returnRecord, OrderHistoryEntryModel snapshot, List<RefundEntryModel> refunds, UserModel principal) throws OrderReturnRecordsHandlerException
    {
        OrderRefundRecordEntryModel refundRecordEntry = (OrderRefundRecordEntryModel)getModelService().create(OrderRefundRecordEntryModel.class);
        refundRecordEntry.setTimestamp(new Date());
        refundRecordEntry.setCode(generateEntryCode(snapshot));
        refundRecordEntry.setOriginalVersion(snapshot);
        refundRecordEntry.setModificationRecord((OrderModificationRecordModel)returnRecord);
        refundRecordEntry.setPrincipal((PrincipalModel)principal);
        refundRecordEntry.setOwner((ItemModel)returnRecord);
        refundRecordEntry.setStatus(OrderModificationEntryStatus.INPROGRESS);
        getModelService().save(refundRecordEntry);
        List<OrderEntryModificationRecordEntryModel> orderEntriesRecords = new ArrayList<>();
        for(RefundEntryModel refundEntry : refunds)
        {
            OrderEntryReturnRecordEntryModel orderEntryRefundEntry = (OrderEntryReturnRecordEntryModel)getModelService().create(OrderEntryReturnRecordEntryModel.class);
            orderEntryRefundEntry.setCode(refundEntry.getOrderEntry().getPk().toString());
            orderEntryRefundEntry.setExpectedQuantity(refundEntry.getExpectedQuantity());
            orderEntryRefundEntry.setModificationRecordEntry((OrderModificationRecordEntryModel)refundRecordEntry);
            orderEntryRefundEntry.setOriginalOrderEntry(getOriginalOrderEntry(snapshot, refundEntry));
            getModelService().save(orderEntryRefundEntry);
            orderEntriesRecords.add(orderEntryRefundEntry);
        }
        refundRecordEntry.setOrderEntriesModificationEntries(orderEntriesRecords);
        getModelService().saveAll();
        return (OrderReturnRecordEntryModel)refundRecordEntry;
    }


    protected OrderEntryModel getOriginalOrderEntry(OrderHistoryEntryModel snapshot, RefundEntryModel refundEntry) throws OrderReturnRecordsHandlerException
    {
        try
        {
            int entryPos = refundEntry.getOrderEntry().getEntryNumber().intValue();
            return snapshot.getPreviousOrderVersion().getEntries().get(entryPos);
        }
        catch(IndexOutOfBoundsException e)
        {
            throw new IllegalStateException("Cloned and original order have different number of entries", e);
        }
        catch(Exception e)
        {
            throw new OrderReturnRecordsHandlerException(refundEntry.getOrderEntry().getOrder().getCode(), "Error during getting historical orderEntry", e);
        }
    }


    protected String generateEntryCode(OrderHistoryEntryModel snapshot)
    {
        return snapshot.getOrder().getCode() + "_v" + snapshot.getOrder().getCode() + "_c";
    }


    protected OrderReturnRecordModel getOrCreateReturnRecord(OrderModel order)
    {
        OrderReturnRecordModel returnRecord = this.orderReturnDao.getOrderReturnRecord(order);
        if(returnRecord == null)
        {
            returnRecord = createReturnRecord(order);
        }
        return returnRecord;
    }


    protected OrderReturnRecordModel createReturnRecord(OrderModel order)
    {
        OrderReturnRecordModel returnRecord = (OrderReturnRecordModel)getModelService().create(OrderReturnRecordModel.class);
        returnRecord.setOrder(order);
        returnRecord.setOwner((ItemModel)order);
        returnRecord.setInProgress(false);
        getModelService().save(returnRecord);
        return returnRecord;
    }


    protected OrderHistoryEntryModel createSnaphot(OrderModel order, String description)
    {
        OrderModel version = getOrderHistoryService().createHistorySnapshot(order);
        getOrderHistoryService().saveHistorySnapshot(version);
        OrderHistoryEntryModel historyEntry = (OrderHistoryEntryModel)getModelService().create(OrderHistoryEntryModel.class);
        historyEntry.setOrder(order);
        historyEntry.setPreviousOrderVersion(version);
        historyEntry.setDescription(description);
        historyEntry.setTimestamp(new Date());
        getModelService().save(historyEntry);
        return historyEntry;
    }


    public OrderReturnRecordModel getReturnRecord(OrderModel order)
    {
        return this.orderReturnDao.getOrderReturnRecord(order);
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected OrderReturnDao getOrderReturnDao()
    {
        return this.orderReturnDao;
    }


    @Required
    public void setOrderReturnDao(OrderReturnDao orderReturnDao)
    {
        this.orderReturnDao = orderReturnDao;
    }


    protected OrderHistoryService getOrderHistoryService()
    {
        return this.orderHistoryService;
    }


    @Required
    public void setOrderHistoryService(OrderHistoryService orderHistoryService)
    {
        this.orderHistoryService = orderHistoryService;
    }
}
