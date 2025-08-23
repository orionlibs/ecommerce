package de.hybris.platform.adaptivesearch.strategies;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import java.io.Serializable;

public interface AsCacheAwareStrategy<T>
{
    Serializable getCacheKeyFragment(AsSearchProfileContext paramAsSearchProfileContext, T paramT);
}
