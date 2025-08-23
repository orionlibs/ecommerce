package de.hybris.platform.adaptivesearch.strategies;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;

public interface AsSearchProfileCalculationStrategy<T extends de.hybris.platform.adaptivesearch.data.AbstractAsSearchProfile> extends AsCacheAwareStrategy<T>
{
    AsSearchProfileResult calculate(AsSearchProfileContext paramAsSearchProfileContext, T paramT);


    default AsSearchProfileResult map(AsSearchProfileContext context, AsSearchProfileResult result)
    {
        return result;
    }
}
