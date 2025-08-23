package de.hybris.platform.core;

import de.hybris.platform.util.Config;
import de.hybris.platform.util.FlexibleSearchUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class GenericValueCondition extends GenericFieldCondition
{
    private static final String PARAM_PREFIX = "gs.param.";
    private Object value;
    private String valueQualifier;


    protected GenericValueCondition(GenericSearchField field, Operator operator, Object value)
    {
        this(field, operator, value, null, false);
    }


    protected GenericValueCondition(GenericSearchField field, Operator operator, Object value, String valueQualifier)
    {
        this(field, operator, value, valueQualifier, false);
    }


    protected GenericValueCondition(GenericSearchField field, Operator operator, Object value, boolean caseInsensitive)
    {
        this(field, operator, value, null, caseInsensitive);
    }


    protected GenericValueCondition(GenericSearchField field, Operator operator, Object value, String valueQualifier, boolean caseInsensitive)
    {
        super(field, operator, caseInsensitive);
        setValueQualifier(valueQualifier);
        setValue(value);
    }


    protected final void checkOperator()
    {
        if(getOperator().equals(Operator.IS_NOT_NULL) || getOperator().equals(Operator.IS_NULL))
        {
            throw new IllegalArgumentException("GenericValueCondition is not responsible for handling EMPTY, IS_NULL or IS_NOT_NULL operators.");
        }
        if(this.value instanceof Collection && (getOperator().equals(Operator.IN) || getOperator().equals(Operator.NOT_IN)))
        {
            throw new IllegalArgumentException("Collection value is permitted only for IN or NOT_IN operators.");
        }
    }


    public Object getValue()
    {
        return this.value;
    }


    void setValue(Object object)
    {
        checkValue(object);
        this.value = object;
    }


    public void toFlexibleSearch(StringBuilder queryBuffer, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        handleValueConditions((x$0, x$1, x$2) -> super.toFlexibleSearch(x$0, x$1, x$2), queryBuffer, typeIndexMap, valueMap);
    }


    public void toPolyglotSearch(StringBuilder queryBuffer, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        handleValueConditions((x$0, x$1, x$2) -> super.toPolyglotSearch(x$0, x$1, x$2), queryBuffer, typeIndexMap, valueMap);
    }


    private void handleValueConditions(ConditionHandlingSuperMethod superMethod, StringBuilder queryBuffer, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        if(this.value instanceof Collection)
        {
            StringBuilder result = new StringBuilder();
            if(optimizedForOracle(result, typeIndexMap, valueMap))
            {
                queryBuffer.append(result);
                return;
            }
            superMethod.runSuperMethod(queryBuffer, typeIndexMap, valueMap);
            queryBuffer.append("(");
            Iterator iterator = ((Collection)this.value).iterator();
            while(iterator.hasNext())
            {
                addConditionToQuery(queryBuffer, valueMap, iterator.next());
                appendComma(queryBuffer, iterator);
            }
            queryBuffer.append(")");
        }
        else
        {
            superMethod.runSuperMethod(queryBuffer, typeIndexMap, valueMap);
            addConditionToQuery(queryBuffer, valueMap, this.value);
        }
    }


    private void addConditionToQuery(StringBuilder queryBuffer, Map<String, Object> valueMap, Object value)
    {
        String qualifier = "gs.param." + valueMap.size() + 1;
        queryBuffer.append("?").append(qualifier);
        valueMap.put(qualifier, getAdjustedValueInternal(value));
    }


    private void appendComma(StringBuilder queryBuffer, Iterator iterator)
    {
        if(iterator.hasNext())
        {
            queryBuffer.append(",");
        }
    }


    private boolean optimizedForOracle(StringBuilder result, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        StringBuilder query = new StringBuilder();
        super.toFlexibleSearch(query, typeIndexMap, valueMap);
        query.append("(?param)");
        String optimizedQuery = FlexibleSearchUtils.buildOracleCompatibleCollectionStatement(query.toString(), "param", "OR", (Collection)this.value, valueMap);
        if(!optimizedQuery.equals(query.toString()))
        {
            result.setLength(0);
            result.append(optimizedQuery);
            return true;
        }
        return false;
    }


    private void checkValue(Object value)
    {
        if(value == null || (value instanceof Collection && ((Collection)value).isEmpty()))
        {
            throw new IllegalArgumentException("NULL as value is not permitted for GenericValueCondition !");
        }
        if(value instanceof Collection)
        {
            Iterator iterator = ((Collection)value).iterator();
            while(iterator.hasNext())
            {
                checkValue(iterator.next());
            }
        }
        if((Operator.LIKE.equals(getOperator()) || Operator.STARTS_WITH.equals(getOperator()) || Operator.ENDS_WITH
                        .equals(getOperator())) && !(value instanceof String) && !(value instanceof de.hybris.platform.util.ItemPropertyValue))
        {
            throw new IllegalArgumentException("Illegal class value for Operator " + getOperator().getCode() + ": " + value
                            .getClass()
                            .getName() + " [possible classes are String, ItemPropertyValue]");
        }
    }


    private Object getAdjustedValueInternal(Object thisValue)
    {
        if(thisValue instanceof String)
        {
            String adjustedValue = (String)thisValue;
            if(isCaseInsensitive())
            {
                adjustedValue = adjustedValue.toLowerCase();
            }
            if(Operator.CONTAINS.equals(getOperator()))
            {
                adjustedValue = "%" + adjustedValue + "%";
            }
            if(Operator.STARTS_WITH.equals(getOperator()))
            {
                adjustedValue = adjustedValue + "%";
            }
            if(Operator.ENDS_WITH.equals(getOperator()))
            {
                adjustedValue = "%" + adjustedValue;
            }
            if(Operator.LIKE.equals(getOperator()) && Config.isSQLServerUsed() && adjustedValue.contains("["))
            {
                adjustedValue = adjustedValue.replace("[", "[[]");
            }
            return adjustedValue;
        }
        return thisValue;
    }


    public String toString()
    {
        return super.toString() + " value: '" + super.toString();
    }


    public String getValueQualifier()
    {
        return this.valueQualifier;
    }


    public void setValueQualifier(String valueQualifier)
    {
        this.valueQualifier = valueQualifier;
    }


    public Map getResettableValues()
    {
        if(getValueQualifier() != null)
        {
            return Collections.singletonMap(getValueQualifier(), getValue());
        }
        return Collections.emptyMap();
    }


    public void setResettableValue(String key, Object value)
    {
        if(key.equals(getValueQualifier()))
        {
            setValue(value);
        }
    }
}
