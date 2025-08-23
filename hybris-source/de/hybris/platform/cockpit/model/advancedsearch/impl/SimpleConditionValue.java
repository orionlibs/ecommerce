package de.hybris.platform.cockpit.model.advancedsearch.impl;

import de.hybris.platform.cockpit.model.search.Operator;
import java.util.Collections;
import java.util.List;

public class SimpleConditionValue extends AbstractConditionValue
{
    private final Object value;


    public SimpleConditionValue(Object value, Operator operator)
    {
        this.value = value;
        this.operator = operator;
    }


    public SimpleConditionValue(Object value)
    {
        this(value, null);
    }


    public List<Object> getValues()
    {
        return Collections.singletonList(this.value);
    }
}
