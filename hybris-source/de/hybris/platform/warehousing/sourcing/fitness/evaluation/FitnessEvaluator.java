package de.hybris.platform.warehousing.sourcing.fitness.evaluation;

import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;

public interface FitnessEvaluator
{
    Double evaluate(SourcingLocation paramSourcingLocation);
}
