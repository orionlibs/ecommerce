package de.hybris.platform.masterserver;

import com.hybris.statistics.StatisticsGateway;

public interface StatisticsGatewayFactory<T>
{
    StatisticsGateway<T> getOrCreateStatisticsGateway();
}
