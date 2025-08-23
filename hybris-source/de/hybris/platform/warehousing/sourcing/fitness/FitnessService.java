package de.hybris.platform.warehousing.sourcing.fitness;

import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import java.util.List;

public interface FitnessService
{
    List<SourcingLocation> sortByFitness(SourcingContext paramSourcingContext);
}
