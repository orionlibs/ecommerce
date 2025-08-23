package de.hybris.platform.ordercancel.impl;

import de.hybris.platform.basecommerce.enums.OrderCancelState;
import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ordercancel.CancelDecision;
import de.hybris.platform.ordercancel.OrderCancelCallbackService;
import de.hybris.platform.ordercancel.OrderCancelCancelableEntriesStrategy;
import de.hybris.platform.ordercancel.OrderCancelDenialReason;
import de.hybris.platform.ordercancel.OrderCancelDenialStrategy;
import de.hybris.platform.ordercancel.OrderCancelDeniedException;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelRecordsHandler;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.OrderCancelRequestExecutor;
import de.hybris.platform.ordercancel.OrderCancelResponse;
import de.hybris.platform.ordercancel.OrderCancelResponseExecutor;
import de.hybris.platform.ordercancel.OrderCancelService;
import de.hybris.platform.ordercancel.OrderCancelStateMappingStrategy;
import de.hybris.platform.ordercancel.dao.OrderCancelDao;
import de.hybris.platform.ordercancel.model.OrderCancelConfigModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultOrderCancelService implements OrderCancelService, OrderCancelCallbackService
{
    private ModelService modelService;
    private OrderCancelRecordsHandler orderCancelRecordsHandler;
    private OrderCancelDao orderCancelDao;
    private OrderCancelStateMappingStrategy stateMappingStrategy;
    private List<OrderCancelDenialStrategy> cancelDenialStrategies;
    private Map<OrderCancelState, OrderCancelRequestExecutor> requestExecutorsMap;
    private Map<OrderCancelState, OrderCancelResponseExecutor> responseExecutorsMap;
    private OrderCancelCancelableEntriesStrategy cancelableEntriesStrategy;


    public OrderCancelRecordModel getCancelRecordForOrder(OrderModel order)
    {
        return this.orderCancelRecordsHandler.getCancelRecord(order);
    }


    public OrderCancelRecordEntryModel getPendingCancelRecordEntry(OrderModel order) throws OrderCancelException
    {
        return this.orderCancelRecordsHandler.getPendingCancelRecordEntry(order);
    }


    public CancelDecision isCancelPossible(OrderModel order, PrincipalModel requestor, boolean partialCancel, boolean partialEntryCancel)
    {
        boolean cancelAllowed;
        OrderCancelConfigModel configuration = getConfiguration();
        List<OrderCancelDenialReason> reasons = new ArrayList<>();
        for(OrderCancelDenialStrategy ocas : this.cancelDenialStrategies)
        {
            OrderCancelDenialReason result = ocas.getCancelDenialReason(configuration, order, requestor, partialCancel, partialEntryCancel);
            if(result != null)
            {
                reasons.add(result);
            }
        }
        if(reasons.isEmpty())
        {
            cancelAllowed = true;
        }
        else
        {
            cancelAllowed = false;
        }
        return new CancelDecision(cancelAllowed, reasons);
    }


    public Map<AbstractOrderEntryModel, Long> getAllCancelableEntries(OrderModel order, PrincipalModel requestor)
    {
        return this.cancelableEntriesStrategy.getAllCancelableEntries(order, requestor);
    }


    public OrderCancelConfigModel getConfiguration()
    {
        return this.orderCancelDao.getOrderCancelConfiguration();
    }


    public OrderCancelRecordEntryModel requestOrderCancel(OrderCancelRequest orderCancelRequest, PrincipalModel requestor) throws OrderCancelException
    {
        OrderCancelRecordEntryModel result;
        CancelDecision cancelDecision = isCancelPossible(orderCancelRequest.getOrder(), requestor, orderCancelRequest
                        .isPartialCancel(), orderCancelRequest.isPartialEntryCancel());
        if(cancelDecision.isAllowed())
        {
            result = this.orderCancelRecordsHandler.createRecordEntry(orderCancelRequest, requestor);
            OrderCancelState currentCancelState = this.stateMappingStrategy.getOrderCancelState(orderCancelRequest.getOrder());
            OrderCancelRequestExecutor ocre = this.requestExecutorsMap.get(currentCancelState);
            if(ocre == null)
            {
                throw new IllegalStateException("Cannot find request executor for cancel state: " + currentCancelState.name());
            }
            ocre.processCancelRequest(orderCancelRequest, result);
        }
        else
        {
            throw new OrderCancelDeniedException(orderCancelRequest.getOrder().getCode(), cancelDecision);
        }
        return result;
    }


    public void onOrderCancelResponse(OrderCancelResponse cancelResponse) throws OrderCancelException
    {
        OrderCancelRecordModel ocrm = getCancelRecordForOrder(cancelResponse.getOrder());
        if(ocrm.isInProgress())
        {
            OrderModificationRecordEntryModel pendingRecord = findPendingCancelRequest(ocrm);
            OrderCancelState currentCancelState = this.stateMappingStrategy.getOrderCancelState(pendingRecord
                            .getModificationRecord().getOrder());
            OrderCancelResponseExecutor ocre = this.responseExecutorsMap.get(currentCancelState);
            if(ocre == null)
            {
                throw new IllegalStateException("Cannot find response executor for cancel state: " + currentCancelState.name());
            }
            ocre.processCancelResponse(cancelResponse, (OrderCancelRecordEntryModel)pendingRecord);
        }
        else
        {
            throw new IllegalArgumentException("No pending cancel requests for given order found");
        }
    }


    protected OrderModificationRecordEntryModel findPendingCancelRequest(OrderCancelRecordModel ocrm)
    {
        OrderModificationRecordEntryModel pendingRecord = null;
        for(OrderModificationRecordEntryModel omrem : ocrm.getModificationRecordEntries())
        {
            if(OrderModificationEntryStatus.INPROGRESS == omrem.getStatus())
            {
                if(pendingRecord != null)
                {
                    throw new IllegalStateException("more than one pending cancel requests for given order found");
                }
                pendingRecord = omrem;
            }
        }
        if(pendingRecord == null)
        {
            throw new IllegalArgumentException("No pending cancel requests for given order found");
        }
        return pendingRecord;
    }


    public OrderCancelRecordsHandler getOrderCancelRecordsHandler()
    {
        return this.orderCancelRecordsHandler;
    }


    public void setOrderCancelRecordsHandler(OrderCancelRecordsHandler orderCancelRecordsHandler)
    {
        this.orderCancelRecordsHandler = orderCancelRecordsHandler;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public OrderCancelStateMappingStrategy getStateMappingStrategy()
    {
        return this.stateMappingStrategy;
    }


    public void setStateMappingStrategy(OrderCancelStateMappingStrategy stateMappingStrategy)
    {
        this.stateMappingStrategy = stateMappingStrategy;
    }


    public List<OrderCancelDenialStrategy> getCancelDenialStrategies()
    {
        return this.cancelDenialStrategies;
    }


    public void setCancelDenialStrategies(List<OrderCancelDenialStrategy> cancelDenialStrategies)
    {
        this.cancelDenialStrategies = cancelDenialStrategies;
    }


    public Map<OrderCancelState, OrderCancelRequestExecutor> getRequestExecutorsMap()
    {
        return this.requestExecutorsMap;
    }


    public void setRequestExecutorsMap(Map<OrderCancelState, OrderCancelRequestExecutor> requestExecutorsMap)
    {
        this.requestExecutorsMap = requestExecutorsMap;
    }


    public Map<OrderCancelState, OrderCancelResponseExecutor> getResponseExecutorsMap()
    {
        return this.responseExecutorsMap;
    }


    public void setResponseExecutorsMap(Map<OrderCancelState, OrderCancelResponseExecutor> responseExecutorsMap)
    {
        this.responseExecutorsMap = responseExecutorsMap;
    }


    public OrderCancelDao getOrderCancelDao()
    {
        return this.orderCancelDao;
    }


    public void setOrderCancelDao(OrderCancelDao orderCancelDao)
    {
        this.orderCancelDao = orderCancelDao;
    }


    public OrderCancelCancelableEntriesStrategy getCancelableEntriesStrategy()
    {
        return this.cancelableEntriesStrategy;
    }


    public void setCancelableEntriesStrategy(OrderCancelCancelableEntriesStrategy cancelableEntriesStrategy)
    {
        this.cancelableEntriesStrategy = cancelableEntriesStrategy;
    }
}
