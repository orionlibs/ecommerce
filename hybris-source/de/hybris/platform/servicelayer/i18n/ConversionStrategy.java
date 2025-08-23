package de.hybris.platform.servicelayer.i18n;

public interface ConversionStrategy
{
    double convert(double paramDouble1, double paramDouble2, double paramDouble3);


    double round(double paramDouble, int paramInt);
}
