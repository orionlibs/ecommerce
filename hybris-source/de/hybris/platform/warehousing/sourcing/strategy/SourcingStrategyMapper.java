package de.hybris.platform.warehousing.sourcing.strategy;

import de.hybris.platform.warehousing.data.sourcing.SourcingContext;

public interface SourcingStrategyMapper
{
    SourcingStrategy getStrategy();


    Boolean isMatch(SourcingContext paramSourcingContext);
}
