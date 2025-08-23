package de.hybris.platform.warehousing.sourcing.fitness.evaluation.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.sourcing.fitness.evaluation.FitnessEvaluator;
import java.util.ArrayList;
import java.util.List;

public class AllocationFitnessEvaluator implements FitnessEvaluator
{
    private static final long ZERO_LONG = 0L;


    public Double evaluate(SourcingLocation sourcingLocation)
    {
        List<AbstractOrderEntryModel> entries = new ArrayList<>(sourcingLocation.getContext().getOrderEntries());
        long totalAvailableQuantity = 0L;
        for(AbstractOrderEntryModel entry : entries)
        {
            long availableQuantity = (sourcingLocation.getAvailability().get(entry.getProduct()) == null) ? 0L : ((Long)sourcingLocation.getAvailability().get(entry.getProduct())).longValue();
            totalAvailableQuantity += availableQuantity;
        }
        return Double.valueOf(totalAvailableQuantity);
    }
}
