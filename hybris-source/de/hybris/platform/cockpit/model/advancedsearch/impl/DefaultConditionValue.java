package de.hybris.platform.cockpit.model.advancedsearch.impl;

import de.hybris.platform.cockpit.model.search.Operator;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DefaultConditionValue extends AbstractConditionValue
{
    private final Object[] values;


    public DefaultConditionValue(Operator operator, Object... values)
    {
        this.operator = operator;
        this.values = values;
    }


    public List<Object> getValues()
    {
        return (this.values == null) ? Collections.EMPTY_LIST : Arrays.<Object>asList(this.values);
    }
}
