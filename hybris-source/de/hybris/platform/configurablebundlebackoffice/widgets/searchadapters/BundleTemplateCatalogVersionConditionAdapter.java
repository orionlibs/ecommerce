package de.hybris.platform.configurablebundlebackoffice.widgets.searchadapters;

import com.google.common.collect.Lists;
import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.catalog.model.CatalogVersionModel;

public class BundleTemplateCatalogVersionConditionAdapter extends BundleTemplateConditionAdapter
{
    public boolean canHandle(NavigationNode node)
    {
        return node.getData() instanceof CatalogVersionModel;
    }


    public void addSearchCondition(AdvancedSearchData searchData, NavigationNode node)
    {
        super.addSearchCondition(searchData, node);
        CatalogVersionModel catalogVersionModel = (CatalogVersionModel)node.getData();
        SearchConditionData searchConditions = createSearchConditions("catalogVersion", catalogVersionModel
                        .getPk(), ValueComparisonOperator.EQUALS);
        searchData.addConditionList(ValueComparisonOperator.OR, Lists.newArrayList((Object[])new SearchConditionData[] {searchConditions}));
    }
}
