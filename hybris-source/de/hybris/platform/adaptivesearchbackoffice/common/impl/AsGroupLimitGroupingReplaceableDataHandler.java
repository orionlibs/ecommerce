package de.hybris.platform.adaptivesearchbackoffice.common.impl;

import com.hybris.cockpitng.editors.EditorContext;
import de.hybris.platform.adaptivesearch.data.AsGroup;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchResultData;

public class AsGroupLimitGroupingReplaceableDataHandler extends AbstractAsGroupingReplaceableDataHandler<Integer>
{
    public Integer getValue(EditorContext<Integer> context)
    {
        SearchResultData searchResultData = resolveSearchResult(context);
        if(isReplaceable(context))
        {
            return ((AsGroup)searchResultData.getAsSearchResult().getSearchProfileResult().getGroup().getConfiguration()).getLimit();
        }
        return (Integer)context.getInitialValue();
    }
}
