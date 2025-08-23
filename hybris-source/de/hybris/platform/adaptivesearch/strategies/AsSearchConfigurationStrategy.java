package de.hybris.platform.adaptivesearch.strategies;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsSearchConfigurationInfoData;
import java.util.Optional;
import java.util.Set;

public interface AsSearchConfigurationStrategy<P extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel, C extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel>
{
    Optional<C> getForContext(AsSearchProfileContext paramAsSearchProfileContext, P paramP);


    C getOrCreateForContext(AsSearchProfileContext paramAsSearchProfileContext, P paramP);


    AsSearchConfigurationInfoData getInfoForContext(AsSearchProfileContext paramAsSearchProfileContext, P paramP);


    Set<String> getQualifiers(P paramP);
}
