package de.hybris.platform.servicelayer.stats;

public interface BasicStatisticsCollector extends StatisticsCollector
{
    float collect();


    boolean evaluateValue(float paramFloat);
}
