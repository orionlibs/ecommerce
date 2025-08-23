package com.hybris.backoffice.solrsearch.adapters.conditions.product;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.searchadapters.conditions.SearchConditionAdapter;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2105", forRemoval = true)
public class SolrSearchClassificationSystemVersionConditionAdapter extends SearchConditionAdapter
{
    private String classificationSystemVersionPropertyName;
    private ValueComparisonOperator operator;


    public boolean canHandle(NavigationNode node)
    {
        return node.getData() instanceof ClassificationSystemVersionModel;
    }


    public void addSearchCondition(AdvancedSearchData searchData, NavigationNode node)
    {
        ClassificationSystemVersionModel classificationSystemVersion = (ClassificationSystemVersionModel)node.getData();
        SearchConditionData catalogCondition = createSearchConditions(this.classificationSystemVersionPropertyName, classificationSystemVersion
                        .getPk(), this.operator);
        searchData.addCondition(catalogCondition.getFieldType(), catalogCondition.getOperator(), catalogCondition.getValue());
    }


    @Required
    public void setClassificationSystemVersionPropertyName(String classificationSystemVersionPropertyName)
    {
        this.classificationSystemVersionPropertyName = classificationSystemVersionPropertyName;
    }


    @Required
    public void setOperator(ValueComparisonOperator operator)
    {
        this.operator = operator;
    }
}
