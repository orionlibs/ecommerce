package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.strategies.AsSearchConfigurationStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileCalculationStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileLoadStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileMapping;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsSearchProfileMapping implements AsSearchProfileMapping
{
    private String type;
    private AsSearchProfileLoadStrategy loadStrategy;
    private AsSearchProfileCalculationStrategy calculationStrategy;
    private AsSearchConfigurationStrategy searchConfigurationStrategy;


    public String getType()
    {
        return this.type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public AsSearchProfileLoadStrategy getLoadStrategy()
    {
        return this.loadStrategy;
    }


    @Required
    public void setLoadStrategy(AsSearchProfileLoadStrategy loadStrategy)
    {
        this.loadStrategy = loadStrategy;
    }


    public AsSearchProfileCalculationStrategy getCalculationStrategy()
    {
        return this.calculationStrategy;
    }


    @Required
    public void setCalculationStrategy(AsSearchProfileCalculationStrategy calculationStrategy)
    {
        this.calculationStrategy = calculationStrategy;
    }


    public AsSearchConfigurationStrategy getSearchConfigurationStrategy()
    {
        return this.searchConfigurationStrategy;
    }


    @Required
    public void setSearchConfigurationStrategy(AsSearchConfigurationStrategy searchConfigurationStrategy)
    {
        this.searchConfigurationStrategy = searchConfigurationStrategy;
    }
}
