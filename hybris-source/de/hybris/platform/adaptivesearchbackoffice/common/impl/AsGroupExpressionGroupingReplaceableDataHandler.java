package de.hybris.platform.adaptivesearchbackoffice.common.impl;

import com.hybris.cockpitng.editors.EditorContext;
import de.hybris.platform.adaptivesearch.data.AsGroup;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchResultData;

public class AsGroupExpressionGroupingReplaceableDataHandler extends AbstractAsGroupingReplaceableDataHandler<Object>
{
    public Object getValue(EditorContext<Object> context)
    {
        SearchResultData searchResultData = resolveSearchResult(context);
        if(isReplaceable(context))
        {
            return ((AsGroup)searchResultData.getAsSearchResult().getSearchProfileResult().getGroup().getConfiguration()).getExpression();
        }
        return context.getInitialValue();
    }
}
