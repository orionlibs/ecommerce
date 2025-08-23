package de.hybris.platform.warehousing.sourcing.strategy;

import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import java.util.Collection;
import java.util.List;

public interface SourcingStrategyService
{
    List<SourcingStrategy> getStrategies(SourcingContext paramSourcingContext, Collection<SourcingStrategyMapper> paramCollection);


    List<SourcingStrategy> getDefaultStrategies();
}
