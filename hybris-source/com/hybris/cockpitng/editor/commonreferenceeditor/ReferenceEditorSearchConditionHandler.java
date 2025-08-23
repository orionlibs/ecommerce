/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.commonreferenceeditor;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.simplesearch.Field;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.simplesearch.SimpleSearch;
import com.hybris.cockpitng.dataaccess.services.PropertyValueService;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryConditionList;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import groovy.json.StringEscapeUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Helper class for {@link com.hybris.cockpitng.editor.commonreferenceeditor.AbstractReferenceEditor}
 */
public class ReferenceEditorSearchConditionHandler
{
    public static final String PARAM_SEARCH_CONDITION_PREFIX = "referenceSearchCondition_";
    public static final String PARAM_SEARCH_CONDITION_GROUP_OPERATOR = "referenceSearchConditionGroupOperator";
    public static final String SEARCH_CONDITION_DELIMITER = "_";
    private PropertyValueService propertyValueService;
    private static final Pattern SPEL_SEARCH_CONDITION_REGEXP = Pattern.compile("\\{((.*))\\}");


    /**
     * Provides full list of search conditions for
     * {@link com.hybris.backoffice.widgets.advancedsearch.engine.AdvancedSearchQueryData}
     */
    public List<SearchQueryCondition> getAllSearchQueryConditions(final String textQuery,
                    final Map<String, Object> searchConditions, final SimpleSearch simpleSearch, final Map<String, Object> contextMap)
    {
        final List<SearchQueryCondition> queryConditions = searchConditions.keySet().stream()
                        .filter(key -> !key.equals(PARAM_SEARCH_CONDITION_GROUP_OPERATOR)).map(key -> {
                            final SearchQueryCondition searchQueryCondition = new SearchQueryCondition();
                            searchQueryCondition.setOperator(getConditionOperator(key));
                            searchQueryCondition.setValue(getValue(searchConditions, contextMap, key));
                            final String qualifier = StringUtils.substringBeforeLast(key, SEARCH_CONDITION_DELIMITER);
                            searchQueryCondition.setDescriptor(new SearchAttributeDescriptor(qualifier));
                            return searchQueryCondition;
                        }).collect(Collectors.toList());
        final SearchQueryConditionList queryConditionList = new SearchQueryConditionList(getSearchOperator(searchConditions),
                        queryConditions);
        final SearchQueryConditionList textConditionList = getTextQuery(textQuery, simpleSearch);
        if(CollectionUtils.isNotEmpty(queryConditions) && textConditionList != null)
        {
            return Arrays.asList(queryConditionList, textConditionList);
        }
        else if(textConditionList != null)
        {
            return Collections.singletonList(textConditionList);
        }
        else
        {
            return Collections.singletonList(queryConditionList);
        }
    }


    public ValueComparisonOperator getConditionOperator(final String key)
    {
        final int dashIndex = key.lastIndexOf(SEARCH_CONDITION_DELIMITER);
        if(dashIndex == -1)
        {
            return ValueComparisonOperator.EQUALS;
        }
        else
        {
            final int i = dashIndex + 1;
            return ValueComparisonOperator.getOperatorByCode(key.substring(i));
        }
    }


    public Object getValue(final Map<String, Object> searchConditions, final Map<String, Object> contextMap, final String key)
    {
        final Object value = searchConditions.get(key);
        if(value instanceof String)
        {
            final String trimmedValue = ((String)value).trim();
            if(isParametrizedValue(trimmedValue))
            {
                final String regularKey = getRegularKey(trimmedValue);
                return getPropertyValueService().readValue(contextMap, regularKey);
            }
            return StringEscapeUtils.unescapeJava(trimmedValue);
        }
        return value;
    }


    private boolean isParametrizedValue(final String keyValue)
    {
        final Matcher matcher = SPEL_SEARCH_CONDITION_REGEXP.matcher(keyValue);
        return matcher.matches();
    }


    private String getRegularKey(final String keyValue)
    {
        String key = keyValue;
        final Matcher matcher = SPEL_SEARCH_CONDITION_REGEXP.matcher(keyValue);
        if(matcher.find())
        {
            key = matcher.group(1);
        }
        return key;
    }


    private SearchQueryConditionList getTextQuery(final String textQuery, final SimpleSearch simpleSearch)
    {
        if(StringUtils.isNotBlank(textQuery))
        {
            final List<SearchQueryCondition> simpleSearchConditions = Lists.newArrayList();
            for(final Field field : simpleSearch.getField())
            {
                final SearchAttributeDescriptor searchAttributeDescriptor = new SearchAttributeDescriptor(field.getName());
                final SearchQueryCondition searchQueryCondition = new SearchQueryCondition();
                searchQueryCondition.setOperator(ValueComparisonOperator.CONTAINS);
                searchQueryCondition.setValue(textQuery);
                searchQueryCondition.setDescriptor(searchAttributeDescriptor);
                simpleSearchConditions.add(searchQueryCondition);
            }
            return SearchQueryConditionList.or(simpleSearchConditions);
        }
        else
        {
            return null;
        }
    }


    /**
     * Provides predefined operator for editor reference search condition set or gives default one (AND)
     */
    public ValueComparisonOperator getSearchOperator(final Map<String, Object> searchConditions)
    {
        return getSearchOperator(searchConditions, ValueComparisonOperator.AND);
    }


    /**
     * Provides predefined operator for editor reference search condition set or gives default passed as parameter
     */
    public ValueComparisonOperator getSearchOperator(final Map<String, Object> searchConditions,
                    final ValueComparisonOperator defaultOperator)
    {
        if(searchConditions.keySet().contains(PARAM_SEARCH_CONDITION_GROUP_OPERATOR))
        {
            return ValueComparisonOperator.getOperatorByCode((String)searchConditions.get(PARAM_SEARCH_CONDITION_GROUP_OPERATOR));
        }
        return defaultOperator;
    }


    /**
     * Provides filtered list of properties that are about editor reference search conditions.
     */
    public Map<String, Object> getSearchConditions(final Map<String, Object> parametersFromConfig, final boolean keysWithoutPrefix)
    {
        final Map<String, Object> searchConditions = new LinkedHashMap<>();
        parametersFromConfig.keySet().stream().filter(key -> isReferenceSearchConditionKey(key)).forEach(key -> searchConditions
                        .put(keysWithoutPrefix ? getSearchConditionKeyWithoutPrefix(key) : key, parametersFromConfig.get(key)));
        return searchConditions;
    }


    public Map<String, Object> getSearchConditions(final Map<String, Object> parametersFromConfig)
    {
        return getSearchConditions(parametersFromConfig, true);
    }


    private boolean isReferenceSearchConditionKey(final String key)
    {
        return key.contains(PARAM_SEARCH_CONDITION_PREFIX) || key.contains(PARAM_SEARCH_CONDITION_GROUP_OPERATOR);
    }


    private String getSearchConditionKeyWithoutPrefix(final String key)
    {
        return key.replaceFirst(PARAM_SEARCH_CONDITION_PREFIX, StringUtils.EMPTY);
    }


    public PropertyValueService getPropertyValueService()
    {
        return propertyValueService;
    }


    @Required
    public void setPropertyValueService(final PropertyValueService propertyValueService)
    {
        this.propertyValueService = propertyValueService;
    }
}
