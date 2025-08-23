/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.advancedsearch.impl;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class AdvancedSearchDataConditionEvaluator
{
    private AdvancedSearchDataConditionEvaluator()
    {
        //Utility class
    }


    public static boolean isMandatory(final AdvancedSearch config, final String field)
    {
        return getAllMandatoryFields(config).contains(field);
    }


    public static boolean atLeastOneConditionProvided(final AdvancedSearchData data, final String field)
    {
        for(final SearchConditionData condition : data.getConditions(field))
        {
            if(!isEmpty(condition))
            {
                return true;
            }
        }
        return false;
    }


    static boolean isEmpty(final SearchConditionData condition)
    {
        final boolean noValueProvided = condition.getValue() == null || isMapAndContainsNullValue(condition.getValue());
        final boolean emptyOperator = condition.getOperator().equals(ValueComparisonOperator.IS_EMPTY);
        return noValueProvided && !emptyOperator;
    }


    static boolean isMapAndContainsNullValue(final Object value)
    {
        return value instanceof Map && ((Map)value).containsValue(null);
    }


    static Collection<String> getAllMandatoryFields(final AdvancedSearch config)
    {
        final List<String> mandatoryFields = new ArrayList<>();
        if(config != null && config.getFieldList() != null && config.getFieldList().getField() != null)
        {
            for(final FieldType field : config.getFieldList().getField())
            {
                if(field.isMandatory())
                {
                    mandatoryFields.add(field.getName());
                }
            }
        }
        return mandatoryFields;
    }
}
