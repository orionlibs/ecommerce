package de.hybris.platform.returns.impl;

import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReplacementReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.refund.RefundService;
import de.hybris.platform.returns.OrderReturnException;
import de.hybris.platform.returns.OrderReturnRecordHandler;
import de.hybris.platform.returns.RMAGenerator;
import de.hybris.platform.returns.ReturnActionRequest;
import de.hybris.platform.returns.ReturnActionRequestExecutor;
import de.hybris.platform.returns.ReturnActionResponse;
import de.hybris.platform.returns.ReturnCallbackService;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.returns.dao.ReplacementOrderDao;
import de.hybris.platform.returns.dao.ReturnRequestDao;
import de.hybris.platform.returns.model.OrderReturnRecordModel;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReplacementEntryModel;
import de.hybris.platform.returns.model.ReplacementOrderEntryModel;
import de.hybris.platform.returns.model.ReplacementOrderModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.returns.processor.RefundOrderProcessor;
import de.hybris.platform.returns.processor.ReplacementOrderProcessor;
import de.hybris.platform.returns.processor.ReturnEntryProcessor;
import de.hybris.platform.returns.strategy.ReturnableCheck;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

public class DefaultReturnService implements ReturnService, ReturnCallbackService
{
    private ReplacementOrderDao replacementOrderDao;
    private ReturnRequestDao returnRequestDao;
    private OrderReturnRecordHandler modificationHandler;
    private static final Logger LOG = Logger.getLogger(DefaultReturnService.class.getName());
    private List<ReturnableCheck> returnableChecks = new LinkedList<>();
    private RefundService refundService = null;
    private RMAGenerator generator = null;
    private ReturnEntryProcessor returnEntryProcessor = null;
    private ReplacementOrderProcessor replacementOrderProcessor = null;
    private RefundOrderProcessor refundOrderProcessor = null;
    private FlexibleSearchService flexibleSearchService;
    private ReturnActionRequestExecutor returnActionRequestExecutor;
    private ModelService modelService;


    protected RefundService getRefundService()
    {
        return this.refundService;
    }


    public void setRefundService(RefundService refundService)
    {
        this.refundService = refundService;
    }


    public void setReturnsEntryProcessor(ReturnEntryProcessor returnEntryProcessor)
    {
        this.returnEntryProcessor = returnEntryProcessor;
    }


    protected ReturnEntryProcessor getReturnsEntryProcessor()
    {
        return this.returnEntryProcessor;
    }


    public void setReplacementOrderProcessor(ReplacementOrderProcessor replacementOrderProcessor)
    {
        this.replacementOrderProcessor = replacementOrderProcessor;
    }


    protected ReplacementOrderProcessor getReplacementOrderProcessor()
    {
        return this.replacementOrderProcessor;
    }


    public void setRefundOrderProcessor(RefundOrderProcessor refundOrderProcessor)
    {
        this.refundOrderProcessor = refundOrderProcessor;
    }


    protected RefundOrderProcessor getRefundOrderProcessor()
    {
        return this.refundOrderProcessor;
    }


    protected List<ReturnableCheck> getReturnableChecks()
    {
        return this.returnableChecks;
    }


    public void setReturnableChecks(List<ReturnableCheck> returnableChecks)
    {
        this.returnableChecks = returnableChecks;
    }


    public void setGenerator(RMAGenerator generator)
    {
        this.generator = generator;
    }


    protected RMAGenerator getGenerator()
    {
        return this.generator;
    }


    public String getRMA(ReturnRequestModel request)
    {
        return request.getRMA();
    }


    public String createRMA(ReturnRequestModel request)
    {
        request.setRMA(getGenerator().generateRMA(request));
        getModelService().save(request);
        return request.getRMA();
    }


    public ReturnRequestModel createReturnRequest(OrderModel order)
    {
        return getReturnRequestDao().createReturnRequest(order);
    }


    public List<ReturnRequestModel> getReturnRequests(String code)
    {
        return getReturnRequestDao().getReturnRequests(code);
    }


    public OrderModel getOrderByCode(String code)
    {
        Map<String, Object> values = new HashMap<>();
        values.put("value", code);
        String query = "SELECT {" + Item.PK + "} FROM {Order} WHERE {code} = ?value ORDER BY {" + Item.PK + "} ASC";
        List<OrderModel> result = this.flexibleSearchService.search(query, values).getResult();
        return (result == null) ? null : result.iterator().next();
    }


    public ReplacementOrderModel getReplacementOrder(String rma)
    {
        return getReplacementOrderDao().getReplacementOrder(rma);
    }


    public ReplacementOrderModel createReplacementOrder(ReturnRequestModel request)
    {
        ReplacementOrderModel replacement = getReplacementOrderDao().createReplacementOrder(request);
        getModelService().save(request);
        return replacement;
    }


    public ReplacementEntryModel createReplacement(ReturnRequestModel request, AbstractOrderEntryModel entry, String notes, Long expectedQuantity, ReturnAction action, ReplacementReason reason)
    {
        ReplacementEntryModel returnsEntry = (ReplacementEntryModel)getModelService().create(ReplacementEntryModel.class);
        returnsEntry.setOrderEntry(entry);
        returnsEntry.setAction(action);
        returnsEntry.setNotes(notes);
        returnsEntry.setReason(reason);
        returnsEntry.setReturnRequest(request);
        returnsEntry.setStatus(ReturnStatus.WAIT);
        returnsEntry.setExpectedQuantity(expectedQuantity);
        getModelService().save(returnsEntry);
        return returnsEntry;
    }


    public RefundEntryModel createRefund(ReturnRequestModel request, AbstractOrderEntryModel entry, String notes, Long expectedQuantity, ReturnAction action, RefundReason reason)
    {
        RefundEntryModel returnsEntry = (RefundEntryModel)getModelService().create(RefundEntryModel.class);
        returnsEntry.setOrderEntry(entry);
        returnsEntry.setAction(action);
        returnsEntry.setNotes(notes);
        returnsEntry.setReason(reason);
        returnsEntry.setReturnRequest(request);
        returnsEntry.setExpectedQuantity(expectedQuantity);
        returnsEntry.setStatus(ReturnStatus.WAIT);
        getModelService().save(returnsEntry);
        request.setSubtotal(request
                        .getSubtotal().add(BigDecimal.valueOf(entry.getBasePrice().doubleValue() * expectedQuantity.longValue())));
        getModelService().save(request);
        return returnsEntry;
    }


    public void addReplacementOrderEntries(ReplacementOrderModel order, List<ReplacementEntryModel> replacements)
    {
        for(ReplacementEntryModel replacement : replacements)
        {
            if(replacement.getAction().equals(ReturnAction.HOLD))
            {
                LOG.warn("Skipping 'replacement order entry' creation, because assigned 'replacement' instance is still on 'HOLD'");
                break;
            }
            ReplacementOrderEntryModel entry = (ReplacementOrderEntryModel)getModelService().create(ReplacementOrderEntryModel.class);
            entry.setProduct(replacement.getOrderEntry().getProduct());
            entry.setOrder((AbstractOrderModel)order);
            entry.setEntryNumber(replacement.getOrderEntry().getEntryNumber());
            entry.setQuantity(replacement.getExpectedQuantity());
            entry.setUnit(replacement.getOrderEntry().getUnit());
            getModelService().save(entry);
        }
        getModelService().refresh(order);
    }


    public List<ReturnEntryModel> getReturnEntries(ProductModel product)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("product", product);
        String query = "SELECT {ret." + Item.PK + "} FROM {ReturnEntry AS ret JOIN OrderEntry AS ord ON { orderEntry} = { ord." + Item.PK + "}} WHERE {ord.product} = ?product ORDER BY {ret." + Item.PK + "} ASC";
        return this.flexibleSearchService.search(query, params).getResult();
    }


    public List<ReturnEntryModel> getReturnEntry(AbstractOrderEntryModel entry)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("entry", entry);
        String query = "SELECT {ret." + Item.PK + "} FROM { ReturnEntry AS ret} WHERE {orderEntry} = ?entry ORDER BY {ret." + Item.PK + "} ASC";
        return this.flexibleSearchService.search(query, params).getResult();
    }


    public List<ReplacementEntryModel> getReplacements(ReturnRequestModel request)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("request", request);
        String query = "SELECT {" + Item.PK + "} FROM {ReplacementEntry} WHERE {ReturnRequest} = ?request ORDER BY {" + Item.PK + "} ASC";
        List<ReplacementEntryModel> result = this.flexibleSearchService.search(query, params).getResult();
        return (result == null) ? Collections.<ReplacementEntryModel>emptyList() : result;
    }


    public boolean isReturnable(OrderModel order, AbstractOrderEntryModel entry, long returnQuantity)
    {
        boolean isReturnable = CollectionUtils.isEmpty(getReturnableChecks());
        for(ReturnableCheck strategy : getReturnableChecks())
        {
            isReturnable = strategy.perform(order, entry, returnQuantity);
            if(!isReturnable)
            {
                return false;
            }
        }
        return isReturnable;
    }


    public long maxReturnQuantity(OrderModel order, AbstractOrderEntryModel entry)
    {
        if(CollectionUtils.isEmpty(getReturnableChecks()))
        {
            return entry.getQuantity().longValue();
        }
        long maxQuantity = entry.getQuantity().longValue();
        for(ReturnableCheck strategy : getReturnableChecks())
        {
            long localMaxQuantity = strategy.maxReturnQuantity(order, entry);
            if(localMaxQuantity < 1L)
            {
                return localMaxQuantity;
            }
            if(localMaxQuantity < maxQuantity)
            {
                maxQuantity = localMaxQuantity;
            }
        }
        return maxQuantity;
    }


    public Map<AbstractOrderEntryModel, Long> getAllReturnableEntries(OrderModel order)
    {
        Map<AbstractOrderEntryModel, Long> returnable = new HashMap<>();
        for(AbstractOrderEntryModel entry : order.getEntries())
        {
            long returnableQuantity = maxReturnQuantity(order, entry);
            if(returnableQuantity > 0L)
            {
                returnable.put(entry, Long.valueOf(returnableQuantity));
            }
        }
        return returnable;
    }


    public OrderReturnRecordModel getOrderReturnRecordForOrder(OrderModel order) throws OrderReturnException
    {
        return getModificationHandler().getReturnRecord(order);
    }


    public void requestManualPaymentReversalForReturnRequest(ReturnRequestModel returnRequest) throws OrderReturnException
    {
        Assert.notNull(returnRequest, "ReturnRequest cannot be null");
        ReturnActionRequest returnActionRequest = new ReturnActionRequest(returnRequest);
        getReturnActionRequestExecutor().processManualPaymentReversalForReturnRequest(returnActionRequest);
    }


    public void requestManualTaxReversalForReturnRequest(ReturnRequestModel returnRequest) throws OrderReturnException
    {
        Assert.notNull(returnRequest, "ReturnRequest cannot be null");
        ReturnActionRequest returnActionRequest = new ReturnActionRequest(returnRequest);
        getReturnActionRequestExecutor().processManualTaxReversalForReturnRequest(returnActionRequest);
    }


    public void processReturnEntries(List<ReturnEntryModel> entries)
    {
        if(getReturnsEntryProcessor() != null)
        {
            getReturnsEntryProcessor().process(entries);
        }
    }


    public void processReplacementOrder(ReplacementOrderModel order)
    {
        if(getReplacementOrderProcessor() != null)
        {
            getReplacementOrderProcessor().process(order);
        }
    }


    public void processRefundOrder(OrderModel order)
    {
        if(getRefundOrderProcessor() != null)
        {
            getRefundOrderProcessor().process(order);
        }
    }


    public void onReturnApprovalResponse(ReturnActionResponse approvalResponse) throws OrderReturnException
    {
        getReturnActionRequestExecutor().processApprovingRequest(approvalResponse.getReturnRequest());
    }


    public void onReturnCancelResponse(ReturnActionResponse cancelResponse) throws OrderReturnException
    {
        getReturnActionRequestExecutor().processCancellingRequest(cancelResponse.getReturnRequest());
    }


    public void onReturnReceptionResponse(ReturnActionResponse receptionResponse) throws OrderReturnException
    {
        getReturnActionRequestExecutor().processReceivingRequest(receptionResponse.getReturnRequest());
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected ReturnActionRequestExecutor getReturnActionRequestExecutor()
    {
        return this.returnActionRequestExecutor;
    }


    public void setReturnActionRequestExecutor(ReturnActionRequestExecutor returnActionRequestExecutor)
    {
        this.returnActionRequestExecutor = returnActionRequestExecutor;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ReplacementOrderDao getReplacementOrderDao()
    {
        return this.replacementOrderDao;
    }


    public void setReplacementOrderDao(ReplacementOrderDao replacementOrderDao)
    {
        this.replacementOrderDao = replacementOrderDao;
    }


    protected ReturnRequestDao getReturnRequestDao()
    {
        return this.returnRequestDao;
    }


    public void setReturnRequestDao(ReturnRequestDao returnRequestDao)
    {
        this.returnRequestDao = returnRequestDao;
    }


    protected OrderReturnRecordHandler getModificationHandler()
    {
        return this.modificationHandler;
    }


    public void setModificationHandler(OrderReturnRecordHandler modificationHandler)
    {
        this.modificationHandler = modificationHandler;
    }
}
