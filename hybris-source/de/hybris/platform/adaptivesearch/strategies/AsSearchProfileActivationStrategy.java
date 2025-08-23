package de.hybris.platform.adaptivesearch.strategies;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileActivationGroup;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import java.util.Collections;
import java.util.List;

public interface AsSearchProfileActivationStrategy
{
    default List<AbstractAsSearchProfileModel> getActiveSearchProfiles(AsSearchProfileContext context)
    {
        return Collections.emptyList();
    }


    default AsSearchProfileActivationGroup getSearchProfileActivationGroup(AsSearchProfileContext context)
    {
        List<AbstractAsSearchProfileModel> activeSearchProfiles = getActiveSearchProfiles(context);
        AsSearchProfileActivationGroup group = new AsSearchProfileActivationGroup();
        group.setSearchProfiles(activeSearchProfiles);
        return group;
    }
}
