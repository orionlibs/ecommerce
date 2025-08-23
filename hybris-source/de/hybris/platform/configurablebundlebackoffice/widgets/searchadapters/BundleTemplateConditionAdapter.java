package de.hybris.platform.configurablebundlebackoffice.widgets.searchadapters;

import com.google.common.collect.Lists;
import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.searchadapters.conditions.SearchConditionAdapter;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;

public abstract class BundleTemplateConditionAdapter extends SearchConditionAdapter
{
    public void addSearchCondition(AdvancedSearchData searchData, NavigationNode node)
    {
        SearchConditionData searchConditions = createSearchConditions("parentTemplate", null, ValueComparisonOperator.IS_EMPTY);
        searchData.addConditionList(ValueComparisonOperator.OR, Lists.newArrayList((Object[])new SearchConditionData[] {searchConditions}));
    }
}
