package de.hybris.platform.warehousing.sourcing.strategy.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;
import de.hybris.platform.warehousing.sourcing.fitness.FitnessService;
import de.hybris.platform.warehousing.sourcing.strategy.AbstractSourcingStrategy;
import java.util.Collection;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class NoRestrictionsStrategy extends AbstractSourcingStrategy
{
    private static Logger LOGGER = LoggerFactory.getLogger(NoRestrictionsStrategy.class);
    private FitnessService fitnessService;


    public void source(SourcingContext sourcingContext)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("sourcingContext", sourcingContext);
        Collection<SourcingLocation> fitnessResult = getFitnessService().sortByFitness(sourcingContext);
        fitnessResult.forEach(fitnessSourcingLocation -> sourcingContext.getOrderEntries().forEach(()));
        boolean checkSourceCompleted = checkSourceCompleted(sourcingContext);
        sourcingContext.getResult().setComplete(checkSourceCompleted);
        LOGGER.debug("Total order entries sourceable using No Restrictions Strategy: {}",
                        Integer.valueOf(sourcingContext.getResult().getResults().size()));
    }


    protected void createSourcingResult(SourcingContext sourcingContext, AbstractOrderEntryModel orderEntry, SourcingLocation fitnessSourcingLocation)
    {
        Long totalQtySourced = Long.valueOf(getQuantitySourced(sourcingContext.getResult().getResults(), orderEntry));
        OrderEntryModel orderEntryModel = (OrderEntryModel)orderEntry;
        if(totalQtySourced.longValue() < orderEntryModel.getQuantityUnallocated().longValue())
        {
            Long stockLevel = getAvailabilityForProduct(orderEntry.getProduct(), fitnessSourcingLocation);
            Long remainingQty = Long.valueOf(orderEntryModel.getQuantityUnallocated().longValue() - totalQtySourced.longValue());
            if((stockLevel == null || stockLevel.longValue() > 0L) && remainingQty.longValue() > 0L)
            {
                Long orderQty;
                if(stockLevel == null || stockLevel.longValue() >= remainingQty.longValue())
                {
                    orderQty = remainingQty;
                }
                else
                {
                    orderQty = stockLevel;
                }
                Optional<SourcingResult> result = sourcingContext.getResult().getResults().stream().filter(predicate -> predicate.getWarehouse().equals(fitnessSourcingLocation.getWarehouse())).findFirst();
                if(result.isPresent())
                {
                    ((SourcingResult)result.get()).getAllocation().put(orderEntry, orderQty);
                    LOGGER.debug("Updated sourcing result for product [{}]: requested qty [{}] at location [{}]", new Object[] {orderEntry
                                    .getProduct().getCode(), orderQty, fitnessSourcingLocation.getWarehouse().getCode()});
                }
                else
                {
                    sourcingContext.getResult().getResults()
                                    .add(getSourcingResultFactory().create(orderEntry, fitnessSourcingLocation, orderQty));
                    LOGGER.debug("Created sourcing result for product [{}]: requested qty [{}] at location [{}] ", new Object[] {orderEntry
                                    .getProduct().getCode(), orderQty, fitnessSourcingLocation.getWarehouse().getCode()});
                }
            }
        }
    }


    protected FitnessService getFitnessService()
    {
        return this.fitnessService;
    }


    @Required
    public void setFitnessService(FitnessService fitnessService)
    {
        this.fitnessService = fitnessService;
    }
}
