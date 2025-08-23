package de.hybris.platform.warehousing.sourcing.strategy.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.sourcing.fitness.FitnessService;
import de.hybris.platform.warehousing.sourcing.strategy.AbstractSourcingStrategy;
import java.util.Collection;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class NoSplittingStrategy extends AbstractSourcingStrategy
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NoSplittingStrategy.class);
    private FitnessService fitnessService;


    public void source(SourcingContext sourcingContext)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("sourcingContext", sourcingContext);
        Collection<SourcingLocation> sourcingLocations = getFitnessCalculationService().sortByFitness(sourcingContext);
        Optional<SourcingLocation> bestMatch = sourcingLocations.stream().filter(sourcingLocation -> isSourcingNoSplittingPossible(sourcingContext.getOrderEntries(), sourcingLocation)).findFirst();
        bestMatch.ifPresent(bestLocation -> sourcingContext.getResult().getResults().add(getSourcingResultFactory().create(sourcingContext.getOrderEntries(), bestLocation)));
        boolean checkSourceCompleted = checkSourceCompleted(sourcingContext);
        sourcingContext.getResult().setComplete(checkSourceCompleted);
        LOGGER.debug("Total order entries sourceable using No Splitting Strategy: {}",
                        Integer.valueOf(sourcingContext.getResult().getResults().size()));
    }


    protected boolean isSourcingNoSplittingPossible(Collection<AbstractOrderEntryModel> entries, SourcingLocation sourcingLocation)
    {
        return entries.stream().allMatch(entry -> (((OrderEntryModel)entry).getQuantityUnallocated().longValue() <= getAvailabilityForProduct(entry.getProduct(), sourcingLocation).longValue()));
    }


    protected FitnessService getFitnessCalculationService()
    {
        return this.fitnessService;
    }


    @Required
    public void setFitnessCalculationService(FitnessService fitnessCalculationService)
    {
        this.fitnessService = fitnessCalculationService;
    }
}
