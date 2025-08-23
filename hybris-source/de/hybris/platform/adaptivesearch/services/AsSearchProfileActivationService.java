package de.hybris.platform.adaptivesearch.services;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileActivationGroup;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import java.util.List;
import java.util.Optional;

public interface AsSearchProfileActivationService
{
    void setCurrentSearchProfiles(List<AbstractAsSearchProfileModel> paramList);


    Optional<List<AbstractAsSearchProfileModel>> getCurrentSearchProfiles();


    void clearCurrentSearchProfiles();


    List<AsSearchProfileActivationGroup> getSearchProfileActivationGroupsForContext(AsSearchProfileContext paramAsSearchProfileContext);
}
