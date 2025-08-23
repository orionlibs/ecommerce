package de.hybris.platform.adaptivesearch.strategies;

import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import java.util.List;
import java.util.Map;

public interface AsSearchProfileRegistry
{
    AsSearchProfileMapping getSearchProfileMapping(AbstractAsSearchProfileModel paramAbstractAsSearchProfileModel);


    Map<String, AsSearchProfileMapping> getSearchProfileMappings();


    List<AsSearchProfileActivationMapping> getSearchProfileActivationMappings();
}
