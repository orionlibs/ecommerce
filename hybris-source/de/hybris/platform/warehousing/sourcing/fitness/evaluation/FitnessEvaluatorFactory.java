package de.hybris.platform.warehousing.sourcing.fitness.evaluation;

import de.hybris.platform.warehousing.data.sourcing.SourcingFactorIdentifiersEnum;

public interface FitnessEvaluatorFactory
{
    FitnessEvaluator getEvaluator(SourcingFactorIdentifiersEnum paramSourcingFactorIdentifiersEnum);
}
