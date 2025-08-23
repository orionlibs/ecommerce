package de.hybris.platform.warehousing.sourcing.strategy.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;
import de.hybris.platform.warehousing.sourcing.context.PosSelectionStrategy;
import de.hybris.platform.warehousing.sourcing.strategy.AbstractSourcingStrategy;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class PickupStrategy extends AbstractSourcingStrategy
{
    private static Logger LOGGER = LoggerFactory.getLogger(PickupStrategy.class);
    protected static final String CODE_PICKUP = "pickup";
    private PosSelectionStrategy posSelectionStrategy;


    public void source(SourcingContext sourcingContext)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("sourcingContext", sourcingContext);
        Optional<AbstractOrderEntryModel> entryOptional = sourcingContext.getOrderEntries().stream().filter(orderEntry -> (Objects.nonNull(orderEntry.getDeliveryPointOfService()) && ((OrderEntryModel)orderEntry).getQuantityUnallocated().longValue() > 0L)).findFirst();
        if(entryOptional.isPresent())
        {
            AbstractOrderEntryModel entry = entryOptional.get();
            Optional<SourcingLocation> pickupLocation = sourcingContext.getSourcingLocations().stream().filter(location -> canSourceOrderEntry(entry, location)).findFirst();
            pickupLocation.ifPresent(sourcingLocation -> createPickupSourcingResult(sourcingContext, sourcingLocation));
        }
        if(Objects.nonNull(sourcingContext.getResult()) && !sourcingContext.getResult().getResults().isEmpty())
        {
            LOGGER.debug("Total order entries sourceable using Pickup Strategy: {}",
                            Integer.valueOf(((SourcingResult)sourcingContext.getResult().getResults().iterator().next()).getAllocation().size()));
        }
    }


    protected boolean canSourceOrderEntry(AbstractOrderEntryModel entry, SourcingLocation location)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("entry", entry);
        ServicesUtil.validateParameterNotNullStandardMessage("location", location);
        ServicesUtil.validateParameterNotNullStandardMessage("warehouse", location.getWarehouse());
        Collection<DeliveryModeModel> warehouseSupportedDeliveryModes = location.getWarehouse().getDeliveryModes();
        boolean isPickupDeliveryMode = warehouseSupportedDeliveryModes.stream().anyMatch(deliveryMode ->
                        (deliveryMode instanceof de.hybris.platform.commerceservices.model.PickUpDeliveryModeModel || "pickup".equals(deliveryMode.getCode())));
        boolean isPosMatch = entry.getDeliveryPointOfService().equals(getPosSelectionStrategy().getPointOfService(entry.getOrder(), location.getWarehouse()));
        return (isPickupDeliveryMode && isPosMatch);
    }


    protected void createPickupSourcingResult(SourcingContext sourcingContext, SourcingLocation location)
    {
        boolean isComplete = true;
        Map<AbstractOrderEntryModel, Long> allocations = new HashMap<>();
        for(AbstractOrderEntryModel entry : sourcingContext.getOrderEntries())
        {
            Long orderQty;
            OrderEntryModel orderEntryModel = (OrderEntryModel)entry;
            Long stock = (Long)location.getAvailability().get(entry.getProduct());
            if(stock.longValue() >= orderEntryModel.getQuantityUnallocated().longValue())
            {
                orderQty = orderEntryModel.getQuantityUnallocated();
            }
            else
            {
                orderQty = stock;
                isComplete = false;
                LOGGER.debug("Incomplete sourcing - Insufficient stock for product [{}]: requested qty [{}], stock qty [{}]", new Object[] {entry
                                .getProduct().getCode(), orderEntryModel.getQuantityUnallocated(), stock});
            }
            if(stock.longValue() > 0L)
            {
                allocations.put(entry, orderQty);
                LOGGER.debug("Created sourcing result allocation for product [{}]: requested qty [{}] at location [{}] ", new Object[] {entry
                                .getProduct().getCode(), orderQty, location.getWarehouse().getCode()});
            }
        }
        if(!allocations.isEmpty())
        {
            SourcingResult result = getSourcingResultFactory().create(allocations, location);
            sourcingContext.getResult().getResults().add(result);
        }
        sourcingContext.getResult().setComplete(isComplete);
    }


    protected PosSelectionStrategy getPosSelectionStrategy()
    {
        return this.posSelectionStrategy;
    }


    @Required
    public void setPosSelectionStrategy(PosSelectionStrategy posSelectionStrategy)
    {
        this.posSelectionStrategy = posSelectionStrategy;
    }
}
