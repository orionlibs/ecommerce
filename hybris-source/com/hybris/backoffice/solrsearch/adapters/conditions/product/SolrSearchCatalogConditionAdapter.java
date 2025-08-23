package com.hybris.backoffice.solrsearch.adapters.conditions.product;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.searchadapters.conditions.SearchConditionAdapter;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.catalog.model.CatalogModel;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2105", forRemoval = true)
public class SolrSearchCatalogConditionAdapter extends SearchConditionAdapter
{
    private String catalogPropertyName;
    private ValueComparisonOperator operator;


    public boolean canHandle(NavigationNode node)
    {
        return node.getData() instanceof CatalogModel;
    }


    public void addSearchCondition(AdvancedSearchData searchData, NavigationNode node)
    {
        CatalogModel catalog = (CatalogModel)node.getData();
        SearchConditionData catalogCondition = createSearchConditions(this.catalogPropertyName, catalog.getPk(), this.operator);
        searchData.addCondition(catalogCondition.getFieldType(), catalogCondition.getOperator(), catalogCondition.getValue());
    }


    @Required
    public void setCatalogPropertyName(String catalogPropertyName)
    {
        this.catalogPropertyName = catalogPropertyName;
    }


    @Required
    public void setOperator(ValueComparisonOperator operator)
    {
        this.operator = operator;
    }
}
