package de.hybris.platform.warehousing.sourcing.result;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;
import java.util.Collection;
import java.util.Map;

public interface SourcingResultFactory
{
    SourcingResult create(AbstractOrderEntryModel paramAbstractOrderEntryModel, SourcingLocation paramSourcingLocation, Long paramLong);


    SourcingResult create(Collection<AbstractOrderEntryModel> paramCollection, SourcingLocation paramSourcingLocation);


    SourcingResult create(Map<AbstractOrderEntryModel, Long> paramMap, SourcingLocation paramSourcingLocation);


    SourcingResults create(Collection<SourcingResults> paramCollection);
}
