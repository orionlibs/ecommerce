package de.hybris.platform.servicelayer.i18n.impl;

import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.servicelayer.i18n.ConversionStrategy;

public class DefaultConversionStrategy implements ConversionStrategy
{
    public double convert(double value, double sourceFactor, double targetFactor)
    {
        return CoreAlgorithms.convert(sourceFactor, targetFactor, value);
    }


    public double round(double value, int digits)
    {
        return CoreAlgorithms.round(value, digits);
    }
}
