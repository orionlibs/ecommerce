package de.hybris.platform.configurablebundlebackoffice.widgets.searchadapters;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.catalog.model.CatalogModel;
import java.util.List;
import java.util.stream.Collectors;

public class BundleTemplateCatalogConditionAdapter extends BundleTemplateConditionAdapter
{
    public boolean canHandle(NavigationNode node)
    {
        return node.getData() instanceof CatalogModel;
    }


    public void addSearchCondition(AdvancedSearchData searchData, NavigationNode node)
    {
        super.addSearchCondition(searchData, node);
        CatalogModel catalog = (CatalogModel)node.getData();
        List<SearchConditionData> conditions = (List<SearchConditionData>)catalog.getCatalogVersions().stream().map(catalogVersion -> createSearchConditions("catalogVersion", catalogVersion.getPk(), ValueComparisonOperator.EQUALS)).collect(Collectors.toList());
        searchData.addConditionList(ValueComparisonOperator.OR, conditions);
    }
}
