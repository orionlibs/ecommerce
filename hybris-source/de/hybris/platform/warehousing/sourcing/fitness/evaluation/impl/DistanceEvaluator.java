package de.hybris.platform.warehousing.sourcing.fitness.evaluation.impl;

import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.sourcing.fitness.evaluation.FitnessEvaluator;

public class DistanceEvaluator implements FitnessEvaluator
{
    public Double evaluate(SourcingLocation sourcingLocation)
    {
        if(sourcingLocation.getDistance() == null)
        {
            return Double.valueOf(Double.NaN);
        }
        return sourcingLocation.getDistance();
    }
}
