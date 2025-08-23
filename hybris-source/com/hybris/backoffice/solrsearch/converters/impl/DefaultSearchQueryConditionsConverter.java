package com.hybris.backoffice.solrsearch.converters.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hybris.backoffice.solrsearch.converters.SearchQueryConditionsConverter;
import com.hybris.backoffice.solrsearch.dataaccess.SolrSearchCondition;
import com.hybris.backoffice.solrsearch.utils.BackofficeSolrUtil;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryConditionList;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DefaultSearchQueryConditionsConverter implements SearchQueryConditionsConverter
{
    public List<SolrSearchCondition> convert(List<? extends SearchQueryCondition> conditions, SearchQuery.Operator globalOperator, IndexedType indexedType)
    {
        List<SolrSearchCondition> converted = Lists.newArrayList();
        convertConditions(converted, conditions, globalOperator, indexedType);
        return converted;
    }


    protected void convertConditions(List<SolrSearchCondition> convertedConditions, List<? extends SearchQueryCondition> conditions, SearchQuery.Operator globalOperator, IndexedType indexedType)
    {
        Map<String, List<SearchQueryCondition>> conditionsByAttribute = groupConditionsByAttribute(conditions, indexedType
                        .getIndexedProperties());
        List<SearchQueryConditionList> nestedConditions = extractNestedConditions(conditions);
        conditionsByAttribute.forEach((attributeName, attrConditions) -> appendAttributeCondition(convertedConditions, attrConditions, (IndexedProperty)indexedType.getIndexedProperties().get(attributeName), globalOperator));
        nestedConditions.forEach(conditionList -> {
            List<SolrSearchCondition> innerConditions = Lists.newArrayList();
            SearchQuery.Operator operator = BackofficeSolrUtil.convertToSolrOperator(conditionList.getOperator());
            convertConditions(innerConditions, conditionList.getConditions(), operator, indexedType);
            SolrSearchCondition query = new SolrSearchCondition(innerConditions, operator, conditionList.isFilteringCondition());
            convertedConditions.add(query);
        });
    }


    protected Map<String, List<SearchQueryCondition>> groupConditionsByAttribute(List<? extends SearchQueryCondition> conditions, Map<String, IndexedProperty> properties)
    {
        Predicate<SearchQueryCondition> indexedAttribute = condition -> (condition.getDescriptor() != null && properties.get(condition.getDescriptor().getAttributeName()) != null);
        return (Map<String, List<SearchQueryCondition>>)conditions.stream().filter(indexedAttribute)
                        .collect(Collectors.groupingBy(condition -> condition.getDescriptor().getAttributeName()));
    }


    protected List<SearchQueryConditionList> extractNestedConditions(List<? extends SearchQueryCondition> conditions)
    {
        return (List<SearchQueryConditionList>)conditions.stream().filter(c -> c instanceof SearchQueryConditionList).map(c -> (SearchQueryConditionList)c)
                        .collect(Collectors.toList());
    }


    protected void appendAttributeCondition(List<SolrSearchCondition> convertedConditions, List<SearchQueryCondition> conditions, IndexedProperty property, SearchQuery.Operator operator)
    {
        if(property.isLocalized())
        {
            Map<Locale, List<SearchQueryCondition>> conditionsByLanguage = splitConditionsByLanguage(conditions);
            conditionsByLanguage.forEach((language, localizedConditions) -> {
                SolrSearchCondition condition = createConditionForProperty(property, localizedConditions, operator, language);
                convertedConditions.add(condition);
            });
        }
        else
        {
            SolrSearchCondition query = createConditionForProperty(property, conditions, operator, null);
            convertedConditions.add(query);
        }
    }


    protected Map<Locale, List<SearchQueryCondition>> splitConditionsByLanguage(List<SearchQueryCondition> conditions)
    {
        Map<Locale, List<SearchQueryCondition>> conditionsByLanguage = Maps.newHashMap();
        conditions.forEach(condition -> {
            Locale language = extractValueLocale(condition.getValue());
            List<SearchQueryCondition> searchQueryConditions = (List<SearchQueryCondition>)conditionsByLanguage.get(language);
            if(searchQueryConditions == null)
            {
                searchQueryConditions = Lists.newArrayList();
                conditionsByLanguage.put(language, searchQueryConditions);
            }
            searchQueryConditions.add(condition);
        });
        return conditionsByLanguage;
    }


    protected SolrSearchCondition createConditionForProperty(IndexedProperty indexedProperty, List<SearchQueryCondition> conditions, SearchQuery.Operator operator, Locale locale)
    {
        SolrSearchCondition convertedCondition = new SolrSearchCondition(indexedProperty.getName(), indexedProperty.getType(), indexedProperty.isMultiValue(), locale, operator, conditions.stream().allMatch(SearchQueryCondition::isFilteringCondition));
        conditions.forEach(condition -> {
            Object value = (locale != null) ? extractLocalizedValue(condition.getValue()) : condition.getValue();
            if(value != null || !condition.getOperator().isRequireValue())
            {
                convertedCondition.addConditionValue(value, condition.getOperator());
            }
        });
        return convertedCondition;
    }


    protected Object extractLocalizedValue(Object value)
    {
        if(value instanceof Map)
        {
            Map localizedValue = (Map)value;
            if(localizedValue.size() == 1)
            {
                return localizedValue.values().iterator().next();
            }
        }
        return value;
    }


    protected Locale extractValueLocale(Object value)
    {
        if(value instanceof Map)
        {
            Map<Locale, ?> map = (Map<Locale, ?>)value;
            if(map.size() == 1)
            {
                return map.keySet().iterator().next();
            }
        }
        return null;
    }
}
