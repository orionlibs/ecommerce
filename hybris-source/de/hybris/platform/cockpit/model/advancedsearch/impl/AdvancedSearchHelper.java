package de.hybris.platform.cockpit.model.advancedsearch.impl;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchModel;
import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchParameterContainer;
import de.hybris.platform.cockpit.model.advancedsearch.ConditionValue;
import de.hybris.platform.cockpit.model.advancedsearch.ConditionValueContainer;
import de.hybris.platform.cockpit.model.advancedsearch.SearchField;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.model.search.SearchParameterDescriptor;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.services.search.impl.GenericSearchParameterDescriptor;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdvancedSearchHelper
{
    private static final Logger LOG = LoggerFactory.getLogger(AdvancedSearchHelper.class);


    private static void createSearchParameterValuesOld(AdvancedSearchModel model, GenericSearchParameterDescriptor searchDescriptor, SearchField searchField, Object value, List<SearchParameterValue> paramValues, List<List<SearchParameterValue>> orValues)
    {
        if(model instanceof DefaultAdvancedSearchModel)
        {
            boolean range = "range".equalsIgnoreCase(((DefaultAdvancedSearchModel)model).getEditorMode(searchField, model
                            .getPropertyDescriptor(searchField)));
            if(!range && value instanceof Collection && !((Collection)value).isEmpty())
            {
                List<SearchParameterValue> orParams = new LinkedList<>();
                for(Object orValue : value)
                {
                    orParams.add(new SearchParameterValue((SearchParameterDescriptor)searchDescriptor, orValue, searchDescriptor.getDefaultOperator()));
                }
                orValues.add(orParams);
            }
            else if(value != null && (range || value.toString().length() > 0))
            {
                Operator operator = null;
                if(range)
                {
                    operator = Operator.BETWEEN;
                }
                else
                {
                    operator = searchDescriptor.getDefaultOperator();
                    if(operator == null && searchDescriptor.getOperators() != null && !searchDescriptor.getOperators().isEmpty())
                    {
                        operator = searchDescriptor.getOperators().get(0);
                    }
                }
                if(operator != null)
                {
                    SearchParameterValue paramValue = new SearchParameterValue((SearchParameterDescriptor)searchDescriptor, value, operator);
                    paramValues.add(paramValue);
                }
            }
        }
        else
        {
            LOG.warn("Could not create search parameter values, model is not a DefaultAdvancedSearchModel.");
        }
    }


    public static void createSearchParameterValues(AdvancedSearchModel model, AdvancedSearchParameterContainer parameterContainer, List<SearchParameterValue> paramValues, List<List<SearchParameterValue>> orValues)
    {
        if(paramValues == null || orValues == null)
        {
            throw new IllegalArgumentException("Parameter value lists can not be null.");
        }
        Map<SearchField, ConditionValueContainer> valueMap = parameterContainer.getSearchFieldValueMap();
        for(Map.Entry<SearchField, ConditionValueContainer> entry : valueMap.entrySet())
        {
            PropertyDescriptor propDescr = model.getPropertyDescriptor(entry.getKey());
            if(propDescr instanceof GenericSearchParameterDescriptor)
            {
                GenericSearchParameterDescriptor searchDescriptor = (GenericSearchParameterDescriptor)propDescr;
                ConditionValueContainer cvalues = entry.getValue();
                for(ConditionValue conditionValue : cvalues.getConditionValues())
                {
                    List<Object> values = conditionValue.getValues();
                    Operator explicitOperator = conditionValue.getOperator();
                    if(explicitOperator == null && CollectionUtils.isNotEmpty(values) && values.size() == 1)
                    {
                        Object value = values.get(0);
                        createSearchParameterValuesOld(model, searchDescriptor, entry.getKey(), value, paramValues, orValues);
                        continue;
                    }
                    paramValues.add(new SearchParameterValue((SearchParameterDescriptor)searchDescriptor, conditionValue, conditionValue.getOperator()));
                }
            }
        }
    }


    public static ConditionValueContainer createSingleConditionValueContainer(ConditionValue value)
    {
        return (ConditionValueContainer)new SingleConditionValueContainer(value);
    }


    public static ConditionValueContainer createSimpleConditionValue(Object value)
    {
        return (ConditionValueContainer)new SingleConditionValueContainer((ConditionValue)new SimpleConditionValue(value));
    }


    public static boolean isSingleSimpleCondition(ConditionValueContainer values)
    {
        return (CollectionUtils.isNotEmpty(values.getConditionValues()) && values.getConditionValues().size() == 1 &&
                        CollectionUtils.isNotEmpty(((ConditionValue)values.getConditionValues().iterator().next()).getValues()));
    }


    public static Object getSingleSimpleCondition(ConditionValueContainer values) throws IllegalArgumentException
    {
        if(isSingleSimpleCondition(values))
        {
            List<Object> cVals = ((ConditionValue)values.getConditionValues().iterator().next()).getValues();
            if(cVals.size() == 1)
            {
                return cVals.iterator().next();
            }
            return cVals;
        }
        throw new IllegalArgumentException("value not a single simple condition.");
    }
}
