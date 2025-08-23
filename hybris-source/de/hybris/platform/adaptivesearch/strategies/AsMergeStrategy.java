package de.hybris.platform.adaptivesearch.strategies;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsMergeConfiguration;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import java.util.List;

public interface AsMergeStrategy
{
    default AsSearchProfileResult merge(AsSearchProfileContext context, List<AsSearchProfileResult> results)
    {
        return merge(context, results, null);
    }


    AsSearchProfileResult merge(AsSearchProfileContext paramAsSearchProfileContext, List<AsSearchProfileResult> paramList, AsMergeConfiguration paramAsMergeConfiguration);
}
