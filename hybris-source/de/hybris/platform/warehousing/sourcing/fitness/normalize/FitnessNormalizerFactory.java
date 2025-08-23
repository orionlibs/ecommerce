package de.hybris.platform.warehousing.sourcing.fitness.normalize;

import de.hybris.platform.warehousing.data.sourcing.SourcingFactorIdentifiersEnum;

public interface FitnessNormalizerFactory
{
    FitnessNormalizer getNormalizer(SourcingFactorIdentifiersEnum paramSourcingFactorIdentifiersEnum);
}
