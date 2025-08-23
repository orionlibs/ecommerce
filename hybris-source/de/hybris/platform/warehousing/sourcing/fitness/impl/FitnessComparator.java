package de.hybris.platform.warehousing.sourcing.fitness.impl;

import de.hybris.platform.warehousing.data.sourcing.FitSourcingLocation;
import java.math.BigDecimal;
import java.util.Comparator;

public class FitnessComparator implements Comparator<FitSourcingLocation>
{
    public int compare(FitSourcingLocation sourcingLocation, FitSourcingLocation nextSourcingLocation)
    {
        if(sourcingLocation.getFitness().doubleValue() < nextSourcingLocation.getFitness().doubleValue())
        {
            return 1;
        }
        if(BigDecimal.valueOf(sourcingLocation.getFitness().doubleValue())
                        .equals(BigDecimal.valueOf(nextSourcingLocation.getFitness().doubleValue())))
        {
            return 0;
        }
        return -1;
    }
}
