package de.hybris.platform.adaptivesearch.strategies;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;

public interface AsSearchProfileLoadStrategy<T extends de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel, R extends de.hybris.platform.adaptivesearch.data.AbstractAsSearchProfile> extends AsCacheAwareStrategy<T>
{
    R load(AsSearchProfileContext paramAsSearchProfileContext, T paramT);


    default R map(AsSearchProfileContext context, R searchProfile)
    {
        return searchProfile;
    }
}
