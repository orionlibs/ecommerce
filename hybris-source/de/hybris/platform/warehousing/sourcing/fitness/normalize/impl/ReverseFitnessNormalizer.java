package de.hybris.platform.warehousing.sourcing.fitness.normalize.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.warehousing.sourcing.fitness.normalize.FitnessNormalizer;

public class ReverseFitnessNormalizer implements FitnessNormalizer
{
    private static final Double ZERO = Double.valueOf(0.0D);
    private static final Integer ONE_HUNDRED = Integer.valueOf(100);


    public Double normalize(Double fitness, Double total) throws IllegalArgumentException
    {
        Preconditions.checkArgument((total != null), "Total cannot be null.");
        Preconditions.checkArgument((fitness != null), "Fitness cannot be null.");
        if(fitness.isNaN() || total.isNaN())
        {
            return ZERO;
        }
        Preconditions.checkArgument((fitness.doubleValue() >= 0.0D), "Fitness cannot be negative.");
        Preconditions.checkArgument((total.doubleValue() > 0.0D), "Total cannot be zero or negative.");
        return Double.valueOf(ONE_HUNDRED.intValue() - fitness.doubleValue() / total.doubleValue() * ONE_HUNDRED.intValue());
    }
}
