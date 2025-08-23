package de.hybris.platform.warehousing.sourcing.fitness.evaluation.impl;

import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.sourcing.fitness.evaluation.FitnessEvaluator;

public class PriorityEvaluator implements FitnessEvaluator
{
    public Double evaluate(SourcingLocation sourcingLocation)
    {
        if(sourcingLocation.getPriority() == null)
        {
            return Double.valueOf(Double.NaN);
        }
        return Double.valueOf(sourcingLocation.getPriority().intValue());
    }
}
