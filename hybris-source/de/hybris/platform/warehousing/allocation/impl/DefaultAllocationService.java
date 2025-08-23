package de.hybris.platform.warehousing.allocation.impl;

import com.google.common.base.Strings;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commerceservices.util.GuidKeyGenerator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.DeliveryModeService;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.stock.impl.StockLevelDao;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.allocation.AllocationException;
import de.hybris.platform.warehousing.allocation.AllocationService;
import de.hybris.platform.warehousing.allocation.strategy.ShippingDateStrategy;
import de.hybris.platform.warehousing.comment.WarehousingCommentService;
import de.hybris.platform.warehousing.data.allocation.DeclineEntries;
import de.hybris.platform.warehousing.data.allocation.DeclineEntry;
import de.hybris.platform.warehousing.data.comment.WarehousingCommentContext;
import de.hybris.platform.warehousing.data.comment.WarehousingCommentEventType;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;
import de.hybris.platform.warehousing.externalfulfillment.dao.WarehousingFulfillmentConfigDao;
import de.hybris.platform.warehousing.inventoryevent.service.InventoryEventService;
import de.hybris.platform.warehousing.model.DeclineConsignmentEntryEventModel;
import de.hybris.platform.warehousing.taskassignment.services.WarehousingConsignmentWorkflowService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class DefaultAllocationService implements AllocationService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAllocationService.class);
    protected static final String PICKUP_CODE = "pickup";
    protected static final String REALLOCATE_COMMENT_SUBJECT = "Decline consignment entry";
    private ModelService modelService;
    private StockLevelDao stockLevelDao;
    private DeliveryModeService deliveryModeService;
    private InventoryEventService inventoryEventService;
    private WarehousingCommentService consignmentEntryCommentService;
    private GuidKeyGenerator guidKeyGenerator;
    private ShippingDateStrategy shippingDateStrategy;
    private WarehousingConsignmentWorkflowService warehousingConsignmentWorkflowService;
    private WarehousingFulfillmentConfigDao warehousingFulfillmentConfigDao;


    public Collection<ConsignmentModel> createConsignments(AbstractOrderModel order, String code, SourcingResults results)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("results", results);
        Assert.isTrue(!Strings.isNullOrEmpty(code), "Parameter code cannot be null or empty");
        AtomicLong index = new AtomicLong();
        order.getConsignments().forEach(value -> index.getAndIncrement());
        Collection<ConsignmentModel> consignments = (Collection<ConsignmentModel>)results.getResults().stream().map(result -> createConsignment(order, code + "_" + code, result)).collect(Collectors.toList());
        getModelService().save(order);
        return consignments;
    }


    public ConsignmentModel createConsignment(AbstractOrderModel order, String code, SourcingResult result)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("result", result);
        ServicesUtil.validateParameterNotNullStandardMessage("order", order);
        Assert.isTrue(!Strings.isNullOrEmpty(code), "Parameter code cannot be null or empty");
        LOGGER.debug("Creating consignment for Location: '{}'", result.getWarehouse().getCode());
        ConsignmentModel consignment = (ConsignmentModel)getModelService().create(ConsignmentModel.class);
        consignment.setCode(code);
        consignment.setOrder(order);
        try
        {
            consignment.setFulfillmentSystemConfig(getWarehousingFulfillmentConfigDao().getConfiguration(result.getWarehouse()));
            Set<Map.Entry<AbstractOrderEntryModel, Long>> resultEntries = result.getAllocation().entrySet();
            Optional<PointOfServiceModel> pickupPos = resultEntries.stream().map(entry -> ((AbstractOrderEntryModel)entry.getKey()).getDeliveryPointOfService()).filter(Objects::nonNull).findFirst();
            if(pickupPos.isPresent())
            {
                consignment.setStatus(ConsignmentStatus.READY);
                consignment.setDeliveryMode(getDeliveryModeService().getDeliveryModeForCode("pickup"));
                consignment.setShippingAddress(((PointOfServiceModel)pickupPos.get()).getAddress());
                consignment.setDeliveryPointOfService(pickupPos.get());
            }
            else
            {
                consignment.setStatus(ConsignmentStatus.READY);
                consignment.setDeliveryMode(order.getDeliveryMode());
                consignment.setShippingAddress(order.getDeliveryAddress());
                consignment.setShippingDate(getShippingDateStrategy().getExpectedShippingDate(consignment));
            }
            Set<ConsignmentEntryModel> entries = (Set<ConsignmentEntryModel>)resultEntries.stream().map(mapEntry -> createConsignmentEntry((AbstractOrderEntryModel)mapEntry.getKey(), (Long)mapEntry.getValue(), consignment)).collect(Collectors.toSet());
            consignment.setConsignmentEntries(entries);
            consignment.setWarehouse(result.getWarehouse());
            if(consignment.getFulfillmentSystemConfig() == null)
            {
                getWarehousingConsignmentWorkflowService().startConsignmentWorkflow(consignment);
            }
            if(!consignment.getWarehouse().isExternal())
            {
                getInventoryEventService().createAllocationEvents(consignment);
            }
        }
        catch(AmbiguousIdentifierException e)
        {
            consignment.setStatus(ConsignmentStatus.CANCELLED);
            LOGGER.warn("Cancelling consignment with code {} since only one fulfillment system configuration is allowed per consignment.", consignment
                            .getCode());
        }
        getModelService().save(consignment);
        return consignment;
    }


    public Collection<ConsignmentModel> manualReallocate(DeclineEntries declinedEntries) throws AllocationException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("declinedEntries", declinedEntries);
        Assert.isTrue(CollectionUtils.isNotEmpty(declinedEntries.getEntries()), "Entries cannot be null or empty.");
        boolean isEntryWithNoLocation = declinedEntries.getEntries().stream().anyMatch(entry -> (entry.getReallocationWarehouse() == null));
        if(isEntryWithNoLocation)
        {
            throw new AllocationException("Invalid or no warehouse selected for manual reallocation");
        }
        boolean isWarehouseWithNoStockEntry = declinedEntries.getEntries().stream().anyMatch(entry -> {
            Collection<StockLevelModel> stockLevels = getStockLevelDao().findStockLevels(entry.getConsignmentEntry().getOrderEntry().getProduct().getCode(), Collections.singletonList(entry.getReallocationWarehouse()));
            return CollectionUtils.isEmpty(stockLevels);
        });
        if(isWarehouseWithNoStockEntry)
        {
            throw new AllocationException("No stock level entry found for manual reallocation");
        }
        List<ConsignmentModel> newConsignments = new ArrayList<>();
        ConsignmentModel consignment = ((DeclineEntry)declinedEntries.getEntries().iterator().next()).getConsignmentEntry().getConsignment();
        AbstractOrderModel order = consignment.getOrder();
        LOGGER.debug("Declining consignment with code :{}", consignment.getCode());
        declinedEntries.getEntries().stream().map(this::declineConsignmentEntry).filter(Optional::isPresent).map(Optional::get)
                        .forEach(event -> consolidateConsignmentEntries(newConsignments, consignment, order, event));
        return newConsignments;
    }


    protected void consolidateConsignmentEntries(List<ConsignmentModel> newConsignments, ConsignmentModel consignment, AbstractOrderModel order, DeclineConsignmentEntryEventModel event)
    {
        Optional<ConsignmentModel> optional = newConsignments.stream().filter(newConsignment -> newConsignment.getWarehouse().equals(event.getReallocatedWarehouse())).findAny();
        if(optional.isPresent())
        {
            ConsignmentModel newConsolidatedConsignment = optional.get();
            ConsignmentEntryModel consignmentEntryModel = createConsignmentEntry(event.getConsignmentEntry().getOrderEntry(), event
                            .getQuantity(), newConsolidatedConsignment);
            if(!consignmentEntryModel.getConsignment().getWarehouse().isExternal())
            {
                getInventoryEventService().createAllocationEventsForConsignmentEntry(consignmentEntryModel);
            }
            Set<ConsignmentEntryModel> consEntries = new HashSet<>();
            Objects.requireNonNull(consEntries);
            newConsolidatedConsignment.getConsignmentEntries().forEach(consEntries::add);
            consEntries.add(consignmentEntryModel);
            newConsolidatedConsignment.setConsignmentEntries(consEntries);
        }
        else
        {
            Map<AbstractOrderEntryModel, Long> allocation = new HashMap<>();
            allocation.put(event.getConsignmentEntry().getOrderEntry(), event.getQuantity());
            if(allocation.isEmpty())
            {
                throw new AllocationException("Unable to process reallocation since there is nothing to reallocate.");
            }
            SourcingResult sourcingResult = new SourcingResult();
            sourcingResult.setAllocation(allocation);
            sourcingResult.setWarehouse(event.getReallocatedWarehouse());
            ConsignmentModel newConsignment = createConsignment(order, consignment.getCode() + "_" + consignment.getCode(), sourcingResult);
            newConsignments.add(newConsignment);
        }
    }


    public void autoReallocate(DeclineEntries declinedEntries)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("declinedEntries", declinedEntries);
        Assert.isTrue(CollectionUtils.isNotEmpty(declinedEntries.getEntries()), "Entries cannot be null or empty.");
        ConsignmentModel consignment = ((DeclineEntry)declinedEntries.getEntries().iterator().next()).getConsignmentEntry().getConsignment();
        LOGGER.debug("Declining consignment with code : {}", consignment.getCode());
        declinedEntries.getEntries().forEach(this::declineConsignmentEntry);
    }


    protected int getNumDeclines(ConsignmentModel consignment)
    {
        return consignment.getConsignmentEntries().stream()
                        .filter(entry -> CollectionUtils.isNotEmpty(entry.getDeclineEntryEvents()))
                        .mapToInt(entry -> entry.getDeclineEntryEvents().size()).sum();
    }


    protected Optional<DeclineConsignmentEntryEventModel> declineConsignmentEntry(DeclineEntry declineEntry)
    {
        Long quantityToDecline = getQuantityToDecline(declineEntry);
        if(quantityToDecline.longValue() > 0L)
        {
            ConsignmentEntryModel consignmentEntry = declineEntry.getConsignmentEntry();
            DeclineConsignmentEntryEventModel event = (DeclineConsignmentEntryEventModel)getModelService().create(DeclineConsignmentEntryEventModel.class);
            event.setConsignmentEntry(consignmentEntry);
            event.setReason(declineEntry.getReason());
            event.setQuantity(quantityToDecline);
            event.setReallocatedWarehouse(declineEntry.getReallocationWarehouse());
            getModelService().save(event);
            if(!declineEntry.getConsignmentEntry().getConsignment().getWarehouse().isExternal())
            {
                reallocateAllocationEvent(declineEntry, quantityToDecline);
            }
            if(!Objects.isNull(declineEntry.getNotes()))
            {
                WarehousingCommentContext commentContext = new WarehousingCommentContext();
                commentContext.setCommentType(WarehousingCommentEventType.REALLOCATE_CONSIGNMENT_COMMENT);
                commentContext.setItem((ItemModel)declineEntry.getConsignmentEntry());
                commentContext.setSubject("Decline consignment entry");
                commentContext.setText(declineEntry.getNotes());
                String code = "reallocation_" + getGuidKeyGenerator().generate().toString();
                getConsignmentEntryCommentService().createAndSaveComment(commentContext, code);
            }
            consignmentEntry.setQuantity(Long.valueOf(consignmentEntry.getQuantity().longValue() - quantityToDecline.longValue()));
            getModelService().save(consignmentEntry);
            return Optional.of(event);
        }
        return Optional.empty();
    }


    protected Long getQuantityToDecline(DeclineEntry declineEntry)
    {
        getModelService().refresh(declineEntry.getConsignmentEntry());
        ConsignmentEntryModel consignmentEntry = declineEntry.getConsignmentEntry();
        return (declineEntry.getQuantity().longValue() <= consignmentEntry.getQuantity().longValue()) ?
                        declineEntry.getQuantity() :
                        useQuantityPending(consignmentEntry, declineEntry);
    }


    protected Long useQuantityPending(ConsignmentEntryModel consignmentEntry, DeclineEntry declineEntry)
    {
        LOGGER.warn("You are trying to decline a quantity of: {} which is more than you are allowed, We will only decline the pending quantity of: {}", declineEntry
                        .getQuantity(), consignmentEntry.getQuantity());
        return consignmentEntry.getQuantity();
    }


    protected ConsignmentEntryModel createConsignmentEntry(AbstractOrderEntryModel orderEntry, Long quantity, ConsignmentModel consignment)
    {
        LOGGER.debug("ConsignmentEntry :: Product [{}]: \tQuantity: '{}'", orderEntry.getProduct().getCode(), quantity);
        ConsignmentEntryModel entry = (ConsignmentEntryModel)getModelService().create(ConsignmentEntryModel.class);
        entry.setOrderEntry(orderEntry);
        entry.setQuantity(quantity);
        entry.setConsignment(consignment);
        Set<ConsignmentEntryModel> consignmentEntries = new HashSet<>();
        if(orderEntry.getConsignmentEntries() != null)
        {
            Objects.requireNonNull(consignmentEntries);
            orderEntry.getConsignmentEntries().forEach(consignmentEntries::add);
        }
        consignmentEntries.add(entry);
        orderEntry.setConsignmentEntries(consignmentEntries);
        return entry;
    }


    protected void reallocateAllocationEvent(DeclineEntry declineEntry, Long quantityToDecline)
    {
        LOGGER.debug("Update or delete allocation event for a ConsignmentEntry to be declined :: Product [{}], at Warehouse [{}]: \tQuantity: '{}'", new Object[] {declineEntry
                        .getConsignmentEntry().getOrderEntry().getProduct().getCode(), declineEntry
                        .getConsignmentEntry().getConsignment().getWarehouse().getCode(), quantityToDecline});
        getInventoryEventService().reallocateAllocationEvent(declineEntry, quantityToDecline);
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


    protected DeliveryModeService getDeliveryModeService()
    {
        return this.deliveryModeService;
    }


    @Required
    public void setDeliveryModeService(DeliveryModeService deliveryModeService)
    {
        this.deliveryModeService = deliveryModeService;
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


    protected InventoryEventService getInventoryEventService()
    {
        return this.inventoryEventService;
    }


    @Required
    public void setInventoryEventService(InventoryEventService inventoryEventService)
    {
        this.inventoryEventService = inventoryEventService;
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


    protected ShippingDateStrategy getShippingDateStrategy()
    {
        return this.shippingDateStrategy;
    }


    public void setShippingDateStrategy(ShippingDateStrategy shippingDateStrategy)
    {
        this.shippingDateStrategy = shippingDateStrategy;
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


    protected WarehousingFulfillmentConfigDao getWarehousingFulfillmentConfigDao()
    {
        return this.warehousingFulfillmentConfigDao;
    }


    @Required
    public void setWarehousingFulfillmentConfigDao(WarehousingFulfillmentConfigDao warehousingFulfillmentConfigDao)
    {
        this.warehousingFulfillmentConfigDao = warehousingFulfillmentConfigDao;
    }


    protected StockLevelDao getStockLevelDao()
    {
        return this.stockLevelDao;
    }


    @Required
    public void setStockLevelDao(StockLevelDao stockLevelDao)
    {
        this.stockLevelDao = stockLevelDao;
    }
}
