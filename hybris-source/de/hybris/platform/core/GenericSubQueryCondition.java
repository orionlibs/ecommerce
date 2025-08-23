package de.hybris.platform.core;

import java.util.Collections;
import java.util.Map;

public class GenericSubQueryCondition extends GenericFieldCondition
{
    private GenericQuery subQuery;


    protected GenericSubQueryCondition(GenericSearchField field, Operator operator, GenericQuery query)
    {
        super(field, operator);
        setSubQuery(query);
    }


    protected void checkOperator()
    {
        if(!getOperator().equals(Operator.IN) && !getOperator().equals(Operator.NOT_IN) && !getOperator().equals(Operator.EXISTS) &&
                        !getOperator().equals(Operator.NOT_EXISTS))
        {
            throw new IllegalArgumentException("GenericSubQueryCondition is only compatible with following operators: [IN, NOT_IN, EXISTS, NOT_EXISTS]");
        }
    }


    public void setSubQuery(GenericQuery query)
    {
        if(query == null)
        {
            throw new IllegalArgumentException("GenericQuery object must not be null.");
        }
        this.subQuery = query;
    }


    public GenericQuery getSubQuery()
    {
        return this.subQuery;
    }


    public void toFlexibleSearch(StringBuilder queryBuffer, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        super.toFlexibleSearch(queryBuffer, typeIndexMap, valueMap);
        queryBuffer.append("({{ ");
        getSubQuery().toFlexibleSearch(queryBuffer, typeIndexMap, valueMap);
        queryBuffer.append(" }})");
    }


    public Map getResettableValues()
    {
        if(getSubQuery().getCondition() != null)
        {
            return getSubQuery().getCondition().getResettableValues();
        }
        return Collections.EMPTY_MAP;
    }


    public void setResettableValue(String key, Object value)
    {
        if(getSubQuery().getCondition() != null)
        {
            getSubQuery().getCondition().setResettableValue(key, value);
        }
    }
}
