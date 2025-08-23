package de.hybris.platform.adaptivesearch.strategies;

import de.hybris.platform.adaptivesearch.data.AsConfigurableSearchConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;

public interface AsSearchProfileResultFactory
{
    AsSearchProfileResult createResult();


    AsSearchProfileResult createResultFromSearchConfiguration(AsConfigurableSearchConfiguration paramAsConfigurableSearchConfiguration);


    <T, R> AsConfigurationHolder<T, R> createConfigurationHolder(T paramT);


    <T, R> AsConfigurationHolder<T, R> createConfigurationHolder(T paramT, Object paramObject);


    <T, R> AsConfigurationHolder<T, R> cloneConfigurationHolder(AsConfigurationHolder<T, R> paramAsConfigurationHolder);
}
