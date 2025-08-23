package de.hybris.platform.warehousing.sourcing.strategy;

import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractSourcingStrategyMapper implements SourcingStrategyMapper
{
    private SourcingStrategy strategy;


    public SourcingStrategy getStrategy()
    {
        return this.strategy;
    }


    @Required
    public void setStrategy(SourcingStrategy strategy)
    {
        this.strategy = strategy;
    }
}
