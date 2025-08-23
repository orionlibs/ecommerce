package com.hybris.backoffice.solrsearch.decorators.impl;

import com.hybris.backoffice.solrsearch.dataaccess.SearchConditionData;
import com.hybris.backoffice.solrsearch.dataaccess.SolrSearchCondition;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultContainsConditionDecorator extends AbstractOrderedSearchConditionDecorator
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultContainsConditionDecorator.class);


    public void decorate(SearchConditionData searchConditionData, SearchQueryData queryData, IndexedType indexedType)
    {
        LOG.debug("Analyzing search conditions");
        searchConditionData.setSearchConditionData(decorateConditions(searchConditionData.getQueryConditions()));
        LOG.debug("Analyzing filter query conditions");
        searchConditionData.setFilterQueryConditions(decorateConditions(searchConditionData.getFilterQueryConditions()));
    }


    protected List<SolrSearchCondition> decorateConditions(List<SolrSearchCondition> conditions)
    {
        List<SolrSearchCondition> decoratedConditions = new ArrayList<>();
        for(SolrSearchCondition condition : conditions)
        {
            if(hasContainsOperator(condition))
            {
                LOG.debug("Contains operator found in condition for attribute {}", condition.getAttributeName());
                decoratedConditions.addAll(convertToContainsOrEquals(condition));
                continue;
            }
            decoratedConditions.add(condition);
        }
        return decoratedConditions;
    }


    protected boolean hasContainsOperator(SolrSearchCondition condition)
    {
        return condition.getConditionValues().stream()
                        .anyMatch(conditionValue -> ValueComparisonOperator.CONTAINS.equals(conditionValue.getComparisonOperator()));
    }


    protected List<SolrSearchCondition> convertToContainsOrEquals(SolrSearchCondition condition)
    {
        return (List<SolrSearchCondition>)condition.getConditionValues().stream().map(conditionValue -> extractConditionFromValue(condition, conditionValue))
                        .collect(Collectors.toList());
    }


    protected SolrSearchCondition extractConditionFromValue(SolrSearchCondition condition, SolrSearchCondition.ConditionValue conditionValue)
    {
        SolrSearchCondition extractedCondition;
        if(ValueComparisonOperator.CONTAINS.equals(conditionValue.getComparisonOperator()))
        {
            extractedCondition = new SolrSearchCondition(condition.getAttributeName(), condition.getAttributeType(), condition.isMultiValue(), condition.getLanguage(), SearchQuery.Operator.OR, condition.isFilterQueryCondition());
            extractedCondition.addConditionValue(conditionValue.getValue(), conditionValue.getComparisonOperator());
            extractedCondition.addConditionValue(conditionValue.getValue(), ValueComparisonOperator.EQUALS);
        }
        else
        {
            extractedCondition = new SolrSearchCondition(condition.getAttributeName(), condition.getAttributeType(), condition.isMultiValue(), condition.getLanguage(), condition.getOperator(), condition.isFilterQueryCondition());
            extractedCondition.addConditionValue(conditionValue.getValue(), conditionValue.getComparisonOperator());
        }
        return extractedCondition;
    }
}
