package de.hybris.platform.adaptivesearchbackoffice.common.impl;

import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.adaptivesearch.enums.AsGroupMergeMode;
import de.hybris.platform.adaptivesearchbackoffice.common.ReplaceableDataHandler;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchResultData;

public abstract class AbstractAsGroupingReplaceableDataHandler<T> implements ReplaceableDataHandler<T>
{
    protected static final String SEARCH_RESULT = "searchResult";
    protected static final String CURRENT_OBJECT_GROUPMERGEMODE = "currentObject.groupMergeMode";


    public boolean isReplaceable(EditorContext<T> context)
    {
        SearchResultData searchResult = resolveSearchResult(context);
        AsGroupMergeMode asGroupMergeMode = resolveCurrentGroupMergeMode(context);
        boolean searchProfileResultExists = (searchResult != null && searchResult.getAsSearchResult() != null && searchResult.getAsSearchResult().getSearchProfileResult() != null);
        boolean isInheritMergeModeSelected = (AsGroupMergeMode.INHERIT == asGroupMergeMode);
        boolean searchProfileResultGroupExists = (searchProfileResultExists && searchResult.getAsSearchResult().getSearchProfileResult().getGroup() != null);
        boolean isReplaceMergeModeSelectedForSearchProfileResult = (searchProfileResultExists && AsGroupMergeMode.REPLACE == searchResult.getAsSearchResult().getSearchProfileResult().getGroupMergeMode());
        return (searchProfileResultExists && isInheritMergeModeSelected && searchProfileResultGroupExists && isReplaceMergeModeSelectedForSearchProfileResult);
    }


    protected SearchResultData resolveSearchResult(EditorContext<T> context)
    {
        WidgetInstanceManager widgetInstanceManager = getWidgetInstanceManager(context);
        return (SearchResultData)widgetInstanceManager.getWidgetslot().getViewModel().getValue("searchResult", SearchResultData.class);
    }


    protected AsGroupMergeMode resolveCurrentGroupMergeMode(EditorContext<T> context)
    {
        WidgetInstanceManager widgetInstanceManager = getWidgetInstanceManager(context);
        return (AsGroupMergeMode)widgetInstanceManager.getWidgetslot().getViewModel().getValue("currentObject.groupMergeMode", AsGroupMergeMode.class);
    }


    protected WidgetInstanceManager getWidgetInstanceManager(EditorContext<T> context)
    {
        return (WidgetInstanceManager)context.getParameter("wim");
    }
}
