package de.hybris.platform.adaptivesearch.strategies;

public interface AsSearchProfileMapping
{
    AsSearchProfileLoadStrategy getLoadStrategy();


    AsSearchProfileCalculationStrategy getCalculationStrategy();


    AsSearchConfigurationStrategy getSearchConfigurationStrategy();
}
