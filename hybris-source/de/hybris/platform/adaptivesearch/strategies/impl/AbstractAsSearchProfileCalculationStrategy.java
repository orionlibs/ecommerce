package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsSearchProfile;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileCalculationStrategy;
import java.io.Serializable;

public abstract class AbstractAsSearchProfileCalculationStrategy<T extends AbstractAsSearchProfile> implements AsSearchProfileCalculationStrategy<T>
{
    public Serializable getCacheKeyFragment(AsSearchProfileContext context, T searchProfile)
    {
        return null;
    }
}
