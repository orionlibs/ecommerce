package de.hybris.platform.core;

import java.util.Collections;
import java.util.Map;

public class GenericLiteralCondition extends GenericFieldCondition
{
    protected GenericLiteralCondition(GenericSearchField field, Operator operator)
    {
        super(field, operator);
    }


    protected final void checkOperator()
    {
        if(!getOperator().equals(Operator.IS_NOT_NULL) && !getOperator().equals(Operator.IS_NULL))
        {
            throw new IllegalArgumentException("GenericLiteralCondition is only responsible for handling EMPTY, IS_NULL or IS_NOT_NULL operators.");
        }
    }


    protected void toFlexibleSearchInternal(StringBuilder queryBuffer, Map typeIndexMap, Map values)
    {
        toFlexibleSearch(queryBuffer, typeIndexMap, values);
    }


    public Map getResettableValues()
    {
        return Collections.EMPTY_MAP;
    }


    public void setResettableValue(String key, Object value)
    {
    }
}
