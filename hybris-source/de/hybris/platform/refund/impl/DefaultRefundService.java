package de.hybris.platform.refund.impl;

import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.orderhistory.OrderHistoryService;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.refund.RefundService;
import de.hybris.platform.refund.dao.RefundDao;
import de.hybris.platform.returns.OrderReturnRecordHandler;
import de.hybris.platform.returns.OrderReturnRecordsHandlerException;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRefundService implements RefundService
{
    private static final Logger LOG = Logger.getLogger(DefaultRefundService.class.getName());
    private OrderReturnRecordHandler modificationHandler;
    private RefundDao refundDao;
    private OrderService orderService;
    private CalculationService calculationService;
    private FlexibleSearchService flexibleSearchService;
    private OrderHistoryService orderHistoryService;
    private ModelService modelService;


    public void setRefundDao(RefundDao refundDao)
    {
        this.refundDao = refundDao;
    }


    public void setModificationHandler(OrderReturnRecordHandler modificationHandler)
    {
        this.modificationHandler = modificationHandler;
    }


    public OrderReturnRecordHandler getModificationHandler()
    {
        return this.modificationHandler;
    }


    protected AbstractOrderEntryModel getOrderEntry(RefundEntryModel refund, AbstractOrderModel order)
    {
        AbstractOrderEntryModel refundOrderEntry = refund.getOrderEntry();
        AbstractOrderEntryModel ret = null;
        for(AbstractOrderEntryModel original : order.getEntries())
        {
            if(original.equals(refundOrderEntry))
            {
                ret = original;
                break;
            }
        }
        return ret;
    }


    public OrderModel createRefundOrderPreview(OrderModel original)
    {
        OrderModel refundOrder = getOrderHistoryService().createHistorySnapshot(original);
        refundOrder.setReturnRequests(null);
        getModelService().saveAll(Arrays.asList(new OrderModel[] {refundOrder}));
        return refundOrder;
    }


    public void apply(List<RefundEntryModel> refunds, OrderModel order)
    {
        for(Iterator<RefundEntryModel> it = refunds.iterator(); it.hasNext(); )
        {
            RefundEntryModel refund = it.next();
            AbstractOrderEntryModel orderEntry = getOrderEntry(refund, (AbstractOrderModel)order);
            if(orderEntry != null)
            {
                long newQuantity = orderEntry.getQuantity().longValue() - refund.getExpectedQuantity().longValue();
                orderEntry.setQuantity(Long.valueOf(newQuantity));
                orderEntry.setCalculated(Boolean.FALSE);
                if(newQuantity <= 0L)
                {
                    orderEntry.setQuantityStatus(OrderEntryStatus.DEAD);
                }
                getModelService().save(orderEntry);
            }
        }
        order.setCalculated(Boolean.FALSE);
        getModelService().save(order);
        try
        {
            getCalculationService().calculate((AbstractOrderModel)order);
        }
        catch(CalculationException e)
        {
            throw new SystemException("Could not calculate order [" + order.getCode() + "] due to : " + e.getMessage(), e);
        }
    }


    public void apply(OrderModel previewOrder, ReturnRequestModel request) throws OrderReturnRecordsHandlerException
    {
        OrderModel finalOrder = request.getOrder();
        getModificationHandler().createRefundEntry(finalOrder, getRefunds(request), "Refund request for order: " + finalOrder
                        .getCode());
        for(AbstractOrderEntryModel previewEntry : previewOrder.getEntries())
        {
            AbstractOrderEntryModel originalEntry = getEntry((AbstractOrderModel)finalOrder, previewEntry.getEntryNumber());
            long newQuantity = previewEntry.getQuantity().longValue();
            originalEntry.setQuantity(Long.valueOf(newQuantity));
            originalEntry.setCalculated(Boolean.FALSE);
            if(newQuantity <= 0L)
            {
                originalEntry.setQuantityStatus(OrderEntryStatus.DEAD);
            }
            getModelService().save(originalEntry);
        }
        finalOrder.setCalculated(Boolean.FALSE);
        getModelService().save(finalOrder);
        try
        {
            getCalculationService().calculate((AbstractOrderModel)finalOrder);
        }
        catch(CalculationException e)
        {
            throw new SystemException("Could not calculate order [" + finalOrder.getCode() + "] due to : " + e.getMessage(), e);
        }
    }


    protected String createOrderHistoryEntryDescription(List<RefundEntryModel> refunds)
    {
        StringBuilder description = new StringBuilder("Refunds:");
        for(Iterator<RefundEntryModel> it = refunds.iterator(); it.hasNext(); )
        {
            RefundEntryModel refund = it.next();
            description.append("- ").append(refund.getNotes()).append('\n');
        }
        return description.toString();
    }


    protected OrderHistoryEntryModel createOrderHistoryEntry(OrderModel processedOrder, OrderModel snapshot)
    {
        OrderHistoryEntryModel historyModel = (OrderHistoryEntryModel)getModelService().create(OrderHistoryEntryModel.class);
        historyModel.setTimestamp(new Date());
        historyModel.setOrder(processedOrder);
        historyModel.setPreviousOrderVersion(snapshot);
        getModelService().save(historyModel);
        return historyModel;
    }


    protected AbstractOrderEntryModel getEntry(AbstractOrderModel order, Integer postion)
    {
        Map<Object, Object> values = new HashMap<>();
        values.put("o", order);
        values.put("nr", postion);
        SearchResult<AbstractOrderEntryModel> res = getFlexibleSearchService().search("GET {AbstractOrderEntry} WHERE {order} = ?o AND {entryNumber} = ?nr", values);
        if(res.getResult().isEmpty())
        {
            LOG.warn("can't find entry number " + postion + " within order " + order);
            return null;
        }
        if(res.getResult().size() > 1)
        {
            LOG.warn("there are more than one entries [" + res.getResult() + "] with the same number " + postion + " within one order");
            return null;
        }
        return res.getResult().iterator().next();
    }


    public List<RefundEntryModel> getRefunds(ReturnRequestModel request)
    {
        return this.refundDao.getRefunds(request);
    }


    protected RefundDao getRefundDao()
    {
        return this.refundDao;
    }


    protected OrderService getOrderService()
    {
        return this.orderService;
    }


    @Required
    public void setOrderService(OrderService orderService)
    {
        this.orderService = orderService;
    }


    protected CalculationService getCalculationService()
    {
        return this.calculationService;
    }


    @Required
    public void setCalculationService(CalculationService calculationService)
    {
        this.calculationService = calculationService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
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
