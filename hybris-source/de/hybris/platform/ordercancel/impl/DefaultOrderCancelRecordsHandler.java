package de.hybris.platform.ordercancel.impl;

import de.hybris.platform.basecommerce.enums.OrderCancelEntryStatus;
import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelRecordsHandler;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.OrderCancelResponse;
import de.hybris.platform.ordercancel.dao.OrderCancelDao;
import de.hybris.platform.ordercancel.exceptions.OrderCancelDaoException;
import de.hybris.platform.ordercancel.exceptions.OrderCancelRecordsHandlerException;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordModel;
import de.hybris.platform.ordercancel.model.OrderEntryCancelRecordEntryModel;
import de.hybris.platform.orderhistory.OrderHistoryService;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultOrderCancelRecordsHandler implements OrderCancelRecordsHandler
{
    private OrderCancelDao orderCancelDao;
    private OrderHistoryService orderHistoryService;
    private UserService userService;
    private ModelService modelService;


    public OrderCancelRecordEntryModel createRecordEntry(OrderCancelRequest request) throws OrderCancelRecordsHandlerException
    {
        return createRecordEntry(request, (PrincipalModel)getUserService().getCurrentUser());
    }


    public OrderCancelRecordEntryModel createRecordEntry(OrderCancelRequest request, PrincipalModel requestor) throws OrderCancelRecordsHandlerException
    {
        if(request == null)
        {
            throw new OrderCancelRecordsHandlerException(null, "Cancel request cannot be null");
        }
        if(request.getOrder() == null)
        {
            throw new OrderCancelRecordsHandlerException(null, "Cancel request contains no order reference");
        }
        OrderModel order = request.getOrder();
        OrderModel version = this.orderHistoryService.createHistorySnapshot(order);
        Map<Integer, AbstractOrderEntryModel> originalOrderEntriesMapping = storeOriginalOrderEntriesMapping(version);
        String description = (!request.isPartialCancel() ? "Full c" : "Partial c") + "ancel request for order: " + (!request.isPartialCancel() ? "Full c" : "Partial c");
        OrderHistoryEntryModel snapshot = createSnaphot(order, version, description, requestor);
        try
        {
            OrderCancelRecordModel cancelRecord = getOrCreateCancelRecord(order);
            if(cancelRecord.isInProgress())
            {
                throw new IllegalStateException("Cannot create new Orde cancel request - the order cancel record indicates: Cancel already in progress");
            }
            cancelRecord.setInProgress(true);
            getModelService().save(cancelRecord);
            return createCancelRecordEntry(request, order, cancelRecord, snapshot, originalOrderEntriesMapping);
        }
        catch(OrderCancelDaoException e)
        {
            throw new OrderCancelRecordsHandlerException(order.getCode(), e);
        }
    }


    protected OrderCancelRecordEntryModel createCancelRecordEntry(OrderCancelRequest request, OrderModel order, OrderCancelRecordModel cancelRecord, OrderHistoryEntryModel snapshot, Map<Integer, AbstractOrderEntryModel> originalOrderEntriesMapping) throws OrderCancelRecordsHandlerException
    {
        OrderCancelRecordEntryModel cancelRecordEntry = (OrderCancelRecordEntryModel)getModelService().create(OrderCancelRecordEntryModel.class);
        cancelRecordEntry.setTimestamp(new Date());
        cancelRecordEntry.setCode(generateEntryCode(snapshot));
        cancelRecordEntry.setOriginalVersion(snapshot);
        cancelRecordEntry.setModificationRecord((OrderModificationRecordModel)cancelRecord);
        cancelRecordEntry.setPrincipal((PrincipalModel)getUserService().getCurrentUser());
        cancelRecordEntry.setOwner((ItemModel)cancelRecord);
        cancelRecordEntry.setStatus(OrderModificationEntryStatus.INPROGRESS);
        cancelRecordEntry.setCancelResult(
                        request.isPartialCancel() ? OrderCancelEntryStatus.PARTIAL : OrderCancelEntryStatus.FULL);
        cancelRecordEntry.setCancelReason(request.getCancelReason());
        cancelRecordEntry.setNotes(request.getNotes());
        getModelService().save(cancelRecordEntry);
        List<OrderEntryModificationRecordEntryModel> orderEntriesRecords = new ArrayList<>();
        for(OrderCancelEntry cancelRequestEntry : request.getEntriesToCancel())
        {
            OrderEntryCancelRecordEntryModel orderEntryRecordEntry = (OrderEntryCancelRecordEntryModel)getModelService().create(OrderEntryCancelRecordEntryModel.class);
            orderEntryRecordEntry.setCode(cancelRequestEntry.getOrderEntry().getPk().toString());
            orderEntryRecordEntry.setCancelRequestQuantity(Integer.valueOf((int)cancelRequestEntry.getCancelQuantity()));
            orderEntryRecordEntry.setModificationRecordEntry((OrderModificationRecordEntryModel)cancelRecordEntry);
            orderEntryRecordEntry.setOrderEntry((OrderEntryModel)cancelRequestEntry.getOrderEntry());
            orderEntryRecordEntry.setOriginalOrderEntry(getOriginalOrderEntry(originalOrderEntriesMapping, cancelRequestEntry));
            orderEntryRecordEntry.setNotes(cancelRequestEntry.getNotes());
            orderEntryRecordEntry.setCancelReason(cancelRequestEntry.getCancelReason());
            getModelService().save(orderEntryRecordEntry);
            orderEntriesRecords.add(orderEntryRecordEntry);
        }
        cancelRecordEntry.setOrderEntriesModificationEntries(orderEntriesRecords);
        getModelService().save(cancelRecordEntry);
        return cancelRecordEntry;
    }


    protected OrderEntryModel getOriginalOrderEntry(Map<Integer, AbstractOrderEntryModel> originalOrderEntriesMapping, OrderCancelEntry cancelRequestEntry) throws OrderCancelRecordsHandlerException
    {
        try
        {
            int entryPos = cancelRequestEntry.getOrderEntry().getEntryNumber().intValue();
            return (OrderEntryModel)originalOrderEntriesMapping.get(Integer.valueOf(entryPos));
        }
        catch(IndexOutOfBoundsException e)
        {
            throw new IllegalStateException("Cloned and original order have different number of entries", e);
        }
        catch(Exception e)
        {
            throw new OrderCancelRecordsHandlerException(cancelRequestEntry.getOrderEntry().getOrder().getCode(), "Error during getting historical orderEntry", e);
        }
    }


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
        for(OrderCancelEntry responseEntry : response.getEntriesToCancel())
        {
            OrderEntryCancelRecordEntryModel orderEntryRecord = getOrderCancelDao().getOrderEntryCancelRecord((OrderEntryModel)responseEntry.getOrderEntry(), currentEntry);
            orderEntryRecord.setCancelledQuantity(Integer.valueOf((int)responseEntry.getCancelQuantity()));
            getModelService().save(orderEntryRecord);
        }
        getModelService().save(currentEntry);
        OrderModificationRecordModel orderModificationRecord = currentEntry.getModificationRecord();
        orderModificationRecord.setInProgress(false);
        getModelService().save(orderModificationRecord);
        return currentEntry;
    }


    public OrderCancelRecordEntryModel getPendingCancelRecordEntry(OrderModel order) throws OrderCancelRecordsHandlerException
    {
        OrderCancelRecordModel orderCancelRecord = getOrderCancelDao().getOrderCancelRecord(order);
        if(orderCancelRecord == null || !orderCancelRecord.isInProgress())
        {
            throw new IllegalStateException("Order[" + order.getCode() + "]: cancel is not currently in progress");
        }
        Collection<OrderModificationRecordEntryModel> entries = orderCancelRecord.getModificationRecordEntries();
        if(entries == null || entries.isEmpty())
        {
            throw new IllegalStateException("Order[" + order.getCode() + "]: has no cancel records");
        }
        OrderCancelRecordEntryModel currentCancelEntry = null;
        for(Iterator<OrderModificationRecordEntryModel> iter = entries.iterator(); iter.hasNext(); )
        {
            OrderCancelRecordEntryModel entry = (OrderCancelRecordEntryModel)iter.next();
            if(entry.getStatus().equals(OrderModificationEntryStatus.INPROGRESS))
            {
                if(currentCancelEntry == null)
                {
                    currentCancelEntry = entry;
                    continue;
                }
                throw new IllegalStateException("Order[" + order
                                .getCode() + "]: cancel record has more than one entries with status 'IN PROGRESS'");
            }
        }
        return currentCancelEntry;
    }


    public OrderCancelRecordModel getCancelRecord(OrderModel order)
    {
        return getOrderCancelDao().getOrderCancelRecord(order);
    }


    protected OrderCancelRecordModel getOrCreateCancelRecord(OrderModel order)
    {
        OrderCancelRecordModel cancelRecord = getOrderCancelDao().getOrderCancelRecord(order);
        if(cancelRecord == null)
        {
            cancelRecord = createCancelRecord(order);
        }
        return cancelRecord;
    }


    protected Map<Integer, AbstractOrderEntryModel> storeOriginalOrderEntriesMapping(OrderModel order)
    {
        Map<Integer, AbstractOrderEntryModel> mapping = new HashMap<>(order.getEntries().size());
        for(AbstractOrderEntryModel currentEntry : order.getEntries())
        {
            mapping.put(currentEntry.getEntryNumber(), currentEntry);
        }
        return mapping;
    }


    protected OrderCancelRecordModel createCancelRecord(OrderModel order)
    {
        OrderCancelRecordModel cancelRecord = (OrderCancelRecordModel)getModelService().create(OrderCancelRecordModel.class);
        cancelRecord.setOrder(order);
        cancelRecord.setOwner((ItemModel)order);
        cancelRecord.setInProgress(false);
        getModelService().save(cancelRecord);
        return cancelRecord;
    }


    protected OrderHistoryEntryModel createSnaphot(OrderModel order, OrderModel version, String description)
    {
        return createSnaphot(order, version, description, null);
    }


    protected OrderHistoryEntryModel createSnaphot(OrderModel order, OrderModel version, String description, PrincipalModel requestor)
    {
        getOrderHistoryService().saveHistorySnapshot(version);
        OrderHistoryEntryModel historyEntry = (OrderHistoryEntryModel)getModelService().create(OrderHistoryEntryModel.class);
        historyEntry.setOrder(order);
        historyEntry.setPreviousOrderVersion(version);
        historyEntry.setDescription(description);
        historyEntry.setTimestamp(new Date());
        if(requestor instanceof EmployeeModel)
        {
            historyEntry.setEmployee((EmployeeModel)requestor);
        }
        getModelService().save(historyEntry);
        return historyEntry;
    }


    protected String generateEntryCode(OrderHistoryEntryModel snapshot)
    {
        return snapshot.getOrder().getCode() + "_v" + snapshot.getOrder().getCode() + "_c";
    }


    protected OrderCancelDao getOrderCancelDao()
    {
        return this.orderCancelDao;
    }


    @Required
    public void setOrderCancelDao(OrderCancelDao orderCancelDao)
    {
        this.orderCancelDao = orderCancelDao;
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


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
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
}
