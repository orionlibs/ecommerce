package de.hybris.platform.warehousing.cancellation.impl;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commerceservices.util.GuidKeyGenerator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelResponse;
import de.hybris.platform.ordermodify.model.OrderModificationRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehousing.cancellation.ConsignmentCancellationService;
import de.hybris.platform.warehousing.comment.WarehousingCommentService;
import de.hybris.platform.warehousing.data.comment.WarehousingCommentContext;
import de.hybris.platform.warehousing.data.comment.WarehousingCommentEventType;
import de.hybris.platform.warehousing.inventoryevent.service.InventoryEventService;
import de.hybris.platform.warehousing.model.CancellationEventModel;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;
import de.hybris.platform.warehousing.taskassignment.services.WarehousingConsignmentWorkflowService;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultConsignmentCancellationService implements ConsignmentCancellationService
{
    private static Logger LOGGER = LoggerFactory.getLogger(DefaultConsignmentCancellationService.class);
    protected static final String COMMENT_SUBJECT = "Cancel consignment";
    protected static final String CONSIGNMENT_ACTION_EVENT_NAME = "ConsignmentActionEvent";
    protected static final String CANCEL_CONSIGNMENT_CHOICE = "cancelConsignment";
    private WarehousingCommentService consignmentEntryCommentService;
    private InventoryEventService inventoryEventService;
    private ModelService modelService;
    private GuidKeyGenerator guidKeyGenerator;
    private List<ConsignmentStatus> nonCancellableConsignmentStatus;
    private WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService;
    private WarehousingConsignmentWorkflowService warehousingConsignmentWorkflowService;


    public void processConsignmentCancellation(OrderCancelResponse orderCancelResponse)
    {
        Map<AbstractOrderEntryModel, Long> orderCancelEntriesCompleted = new HashMap<>();
        for(OrderCancelEntry orderCancelEntry : orderCancelResponse.getEntriesToCancel())
        {
            saveSourceOrderEntryToCancelledConsignmentEntry(orderCancelEntry, orderCancelResponse);
            Long alreadyCancelledQty = getAlreadyCancelledQty(orderCancelEntriesCompleted, orderCancelEntry);
            if(orderCancelEntry.getCancelQuantity() > alreadyCancelledQty.longValue())
            {
                moveProcessAndTerminateWorkflow(orderCancelResponse, orderCancelEntriesCompleted, orderCancelEntry);
            }
        }
    }


    protected void saveSourceOrderEntryToCancelledConsignmentEntry(OrderCancelEntry orderCancelEntry, OrderCancelResponse orderCancelResponse)
    {
        for(Iterator<ConsignmentEntryModel> iterator = orderCancelEntry.getOrderEntry().getConsignmentEntries().iterator(); iterator.hasNext(); )
        {
            ConsignmentEntryModel consignmentEntry = iterator.next();
            AbstractOrderEntryModel oldOrderEntry = consignmentEntry.getOrderEntry();
            ModelService ms = getModelService();
            getCurrentCancelRecordEntry(orderCancelResponse).getOrderEntriesModificationEntries().forEach(orderEntryModificationRecordEntryModel -> {
                OrderEntryModel currentOrderEntry = orderEntryModificationRecordEntryModel.getOrderEntry();
                OrderEntryModel originalOrderEntry = orderEntryModificationRecordEntryModel.getOriginalOrderEntry();
                if(oldOrderEntry.equals(currentOrderEntry) && consignmentEntry.getSourceOrderEntry() == null)
                {
                    consignmentEntry.setSourceOrderEntry(originalOrderEntry);
                    ms.save(consignmentEntry);
                }
            });
        }
    }


    protected OrderModificationRecordEntryModel getCurrentCancelRecordEntry(OrderCancelResponse orderCancelResponse)
    {
        Optional<OrderModificationRecordModel> optionalHistoryCancelRecordEntries = orderCancelResponse.getOrder().getModificationRecords().stream().findFirst();
        if(optionalHistoryCancelRecordEntries.isPresent())
        {
            Collection<OrderModificationRecordEntryModel> historyCancelRecordEntries = ((OrderModificationRecordModel)optionalHistoryCancelRecordEntries.get()).getModificationRecordEntries();
            Optional<OrderModificationRecordEntryModel> currentCancelRecordEntry = historyCancelRecordEntries.stream().skip(historyCancelRecordEntries.size() - 1L).findFirst();
            if(currentCancelRecordEntry.isPresent())
            {
                return currentCancelRecordEntry.get();
            }
        }
        return new OrderModificationRecordEntryModel();
    }


    protected void moveProcessAndTerminateWorkflow(OrderCancelResponse orderCancelResponse, Map<AbstractOrderEntryModel, Long> orderCancelEntriesCompleted, OrderCancelEntry orderCancelEntry)
    {
        for(ConsignmentEntryModel consignmentEntry : orderCancelEntry.getOrderEntry().getConsignmentEntries())
        {
            if(!getNonCancellableConsignmentStatus().contains(consignmentEntry.getConsignment().getStatus()))
            {
                consignmentEntry.getConsignment().setStatus(ConsignmentStatus.CANCELLED);
                getModelService().save(consignmentEntry.getConsignment());
                orderCancelEntriesCompleted.putAll(cancelConsignment(consignmentEntry.getConsignment(), orderCancelResponse));
                getConsignmentBusinessProcessService()
                                .triggerChoiceEvent((ItemModel)consignmentEntry.getConsignment(), "ConsignmentActionEvent", "cancelConsignment");
                getWarehousingConsignmentWorkflowService().terminateConsignmentWorkflow(consignmentEntry.getConsignment());
            }
        }
    }


    protected Long getAlreadyCancelledQty(Map<AbstractOrderEntryModel, Long> orderCancelEntriesCompleted, OrderCancelEntry orderCancelEntry)
    {
        Long alreadyCancelledQty = Long.valueOf(0L);
        for(Map.Entry<AbstractOrderEntryModel, Long> entry : orderCancelEntriesCompleted.entrySet())
        {
            if(((AbstractOrderEntryModel)entry.getKey()).equals(orderCancelEntry.getOrderEntry()))
            {
                alreadyCancelledQty = Long.valueOf(alreadyCancelledQty.longValue() + ((Long)entry.getValue()).longValue());
            }
        }
        return alreadyCancelledQty;
    }


    public Map<AbstractOrderEntryModel, Long> cancelConsignment(ConsignmentModel consignment, OrderCancelResponse orderCancelResponse)
    {
        LOGGER.debug("Cancel consignment with code: [{}]", consignment.getCode());
        Map<AbstractOrderEntryModel, Long> result = new HashMap<>();
        for(ConsignmentEntryModel consignmentEntry : consignment.getConsignmentEntries())
        {
            Optional<OrderCancelEntry> myEntry = orderCancelResponse.getEntriesToCancel().stream().filter(entry -> entry.getOrderEntry().equals(consignmentEntry.getOrderEntry())).findFirst();
            CancelReason myReason = myEntry.isPresent() ? ((OrderCancelEntry)myEntry.get()).getCancelReason() : orderCancelResponse.getCancelReason();
            String myNote = myEntry.isPresent() ? ((OrderCancelEntry)myEntry.get()).getNotes() : orderCancelResponse.getNotes();
            createCancellationEventForInternalWarehouse(consignmentEntry, myReason);
            if(!Objects.isNull(myNote))
            {
                WarehousingCommentContext commentContext = new WarehousingCommentContext();
                commentContext.setCommentType(WarehousingCommentEventType.CANCEL_CONSIGNMENT_COMMENT);
                commentContext.setItem((ItemModel)consignment);
                commentContext.setSubject("Cancel consignment");
                commentContext.setText(myNote);
                String code = "cancellation_" + getGuidKeyGenerator().generate().toString();
                getConsignmentEntryCommentService().createAndSaveComment(commentContext, code);
            }
            if(myEntry.isPresent())
            {
                result.put(consignmentEntry.getOrderEntry(), consignmentEntry.getQuantity());
            }
        }
        return result;
    }


    protected void createCancellationEventForInternalWarehouse(ConsignmentEntryModel consignmentEntry, CancelReason myReason)
    {
        if(!consignmentEntry.getConsignment().getWarehouse().isExternal())
        {
            CancellationEventModel event = new CancellationEventModel();
            event.setConsignmentEntry(consignmentEntry);
            event.setOrderEntry((OrderEntryModel)getModelService().get(consignmentEntry.getOrderEntry().getPk()));
            event.setReason(myReason.getCode());
            event.setQuantity(consignmentEntry.getQuantity().longValue());
            getInventoryEventService().createCancellationEvents(event);
        }
    }


    protected WarehousingCommentService getConsignmentEntryCommentService()
    {
        return this.consignmentEntryCommentService;
    }


    @Required
    public void setConsignmentEntryCommentService(WarehousingCommentService consignmentEntryCommentService)
    {
        this.consignmentEntryCommentService = consignmentEntryCommentService;
    }


    protected GuidKeyGenerator getGuidKeyGenerator()
    {
        return this.guidKeyGenerator;
    }


    @Required
    public void setGuidKeyGenerator(GuidKeyGenerator guidKeyGenerator)
    {
        this.guidKeyGenerator = guidKeyGenerator;
    }


    protected InventoryEventService getInventoryEventService()
    {
        return this.inventoryEventService;
    }


    @Required
    public void setInventoryEventService(InventoryEventService inventoryEventService)
    {
        this.inventoryEventService = inventoryEventService;
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


    protected List<ConsignmentStatus> getNonCancellableConsignmentStatus()
    {
        return this.nonCancellableConsignmentStatus;
    }


    @Required
    public void setNonCancellableConsignmentStatus(List<ConsignmentStatus> nonCancellableConsignmentStatus)
    {
        this.nonCancellableConsignmentStatus = nonCancellableConsignmentStatus;
    }


    protected WarehousingBusinessProcessService<ConsignmentModel> getConsignmentBusinessProcessService()
    {
        return this.consignmentBusinessProcessService;
    }


    @Required
    public void setConsignmentBusinessProcessService(WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService)
    {
        this.consignmentBusinessProcessService = consignmentBusinessProcessService;
    }


    protected WarehousingConsignmentWorkflowService getWarehousingConsignmentWorkflowService()
    {
        return this.warehousingConsignmentWorkflowService;
    }


    @Required
    public void setWarehousingConsignmentWorkflowService(WarehousingConsignmentWorkflowService warehousingConsignmentWorkflowService)
    {
        this.warehousingConsignmentWorkflowService = warehousingConsignmentWorkflowService;
    }
}
