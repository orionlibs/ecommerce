package com.hybris.backoffice.solrsearch.adapters.conditions.product;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.searchadapters.conditions.SearchConditionAdapter;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2105", forRemoval = true)
public class SolrSearchClassificationSystemConditionAdapter extends SearchConditionAdapter
{
    private String classificationSystemPropertyName;
    private ValueComparisonOperator operator;


    public boolean canHandle(NavigationNode node)
    {
        return node.getData() instanceof ClassificationSystemModel;
    }


    public void addSearchCondition(AdvancedSearchData searchData, NavigationNode node)
    {
        ClassificationSystemModel classificationSystem = (ClassificationSystemModel)node.getData();
        SearchConditionData catalogCondition = createSearchConditions(this.classificationSystemPropertyName, classificationSystem
                        .getPk(), this.operator);
        searchData.addCondition(catalogCondition.getFieldType(), catalogCondition.getOperator(), catalogCondition.getValue());
    }


    @Required
    public void setClassificationSystemPropertyName(String classificationSystemPropertyName)
    {
        this.classificationSystemPropertyName = classificationSystemPropertyName;
    }


    @Required
    public void setOperator(ValueComparisonOperator operator)
    {
        this.operator = operator;
    }
}
