package de.hybris.platform.masterserver;

public interface StatisticsConfig
{
    static StatisticsConfig defaultConfig()
    {
        return (StatisticsConfig)DefaultStatisticsConfig.INSTANCE;
    }


    boolean isStatisticsPublisherEnabled();
}
