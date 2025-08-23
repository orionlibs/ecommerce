package de.hybris.platform.warehousing.sourcing.strategy;

import de.hybris.platform.warehousing.data.sourcing.SourcingContext;

public interface SourcingStrategy
{
    void source(SourcingContext paramSourcingContext);


    Boolean isTerminal();
}
