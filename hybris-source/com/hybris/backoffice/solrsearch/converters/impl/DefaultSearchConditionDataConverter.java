package com.hybris.backoffice.solrsearch.converters.impl;

import com.hybris.backoffice.solrsearch.converters.SearchConditionDataConverter;
import com.hybris.backoffice.solrsearch.dataaccess.SearchConditionData;
import com.hybris.backoffice.solrsearch.dataaccess.SolrSearchCondition;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.solrfacetsearch.enums.SolrPropertiesTypes;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSearchConditionDataConverter implements SearchConditionDataConverter
{
    private Set<ValueComparisonOperator> fqApplicableOperators;
    private Set<SolrPropertiesTypes> fqApplicablePropertiesTypes;


    public SearchConditionData convertConditions(List<SolrSearchCondition> conditions, SearchQuery.Operator globalOperator)
    {
        SearchConditionData searchConditionData = new SearchConditionData();
        List<SolrSearchCondition> flattenedConditions = flattenConditions(conditions);
        flattenedConditions.forEach(cnd -> {
            if(isFQApplicableCondition(cnd))
            {
                searchConditionData.addFilterQueryCondition(cnd);
            }
            else
            {
                searchConditionData.addQueryCondition(cnd);
            }
        });
        return searchConditionData;
    }


    protected boolean isFQApplicableCondition(SolrSearchCondition condition)
    {
        if(condition.isFilterQueryCondition())
        {
            return true;
        }
        if(!condition.isNestedCondition())
        {
            SolrPropertiesTypes type = SolrPropertiesTypes.valueOf(condition.getAttributeType());
            Optional<SolrSearchCondition.ConditionValue> anyNotEqualsOperator = condition.getConditionValues().stream().filter(cv -> !this.fqApplicableOperators.contains(cv.getComparisonOperator())).findAny();
            return (!anyNotEqualsOperator.isPresent() && this.fqApplicablePropertiesTypes.contains(type));
        }
        return false;
    }


    protected List<SolrSearchCondition> flattenConditions(List<SolrSearchCondition> conditions)
    {
        List<SolrSearchCondition> flattened = new ArrayList<>();
        for(SolrSearchCondition condition : conditions)
        {
            if(condition.isNestedCondition())
            {
                flattened.addAll(flattenConditions(condition.getNestedConditions()));
                continue;
            }
            flattened.add(condition);
        }
        return flattened;
    }


    @Required
    public void setFqApplicableOperators(Set<ValueComparisonOperator> fqApplicableOperators)
    {
        this.fqApplicableOperators = fqApplicableOperators;
    }


    @Required
    public void setFqApplicablePropertiesTypes(Set<SolrPropertiesTypes> fqApplicablePropertiesTypes)
    {
        this.fqApplicablePropertiesTypes = fqApplicablePropertiesTypes;
    }
}
