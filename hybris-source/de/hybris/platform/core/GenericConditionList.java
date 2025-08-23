package de.hybris.platform.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GenericConditionList extends GenericCondition
{
    private List<GenericCondition> conditionsList = Collections.emptyList();
    private static final Operator DEFAULT_OPERATOR = Operator.AND;


    protected GenericConditionList()
    {
        this(DEFAULT_OPERATOR, (Collection<GenericCondition>)null);
    }


    protected GenericConditionList(Operator operator, GenericCondition... conditions)
    {
        this(operator, Arrays.asList(conditions));
    }


    protected GenericConditionList(GenericCondition... conditions)
    {
        this(DEFAULT_OPERATOR, Arrays.asList(conditions));
    }


    protected GenericConditionList(Operator operator, Collection<GenericCondition> conditionsList)
    {
        super(operator);
        this.conditionsList = (conditionsList == null) ? Collections.<GenericCondition>emptyList() : new ArrayList<>(conditionsList);
    }


    protected final void checkOperator()
    {
        if(!getOperator().equals(Operator.AND) && !getOperator().equals(Operator.OR))
        {
            throw new IllegalArgumentException("Only AND and OR are permitted to conjunct Conditions.");
        }
    }


    public final void addToConditionList(GenericCondition condition)
    {
        this.conditionsList.add(condition);
    }


    public final List<GenericCondition> getConditionList()
    {
        return Collections.unmodifiableList(this.conditionsList);
    }


    public final boolean isEmpty()
    {
        return (getConditionList() == null || getConditionList().isEmpty());
    }


    public void toFlexibleSearch(StringBuilder queryBuffer, Map typeIndexMap, Map valueMap)
    {
        if(!isEmpty())
        {
            Iterator<GenericCondition> iterator = getConditionList().iterator();
            queryBuffer.append(" ").append("(");
            while(iterator.hasNext())
            {
                ((GenericCondition)iterator.next()).toFlexibleSearch(queryBuffer, typeIndexMap, valueMap);
                if(iterator.hasNext())
                {
                    queryBuffer.append(" " + getOperator().getSQLString() + " ");
                }
            }
            queryBuffer.append(" ").append(")");
        }
    }


    public void toPolyglotSearch(StringBuilder queryBuffer, Map typeIndexMap, Map valueMap)
    {
        if(!isEmpty())
        {
            Iterator<GenericCondition> iterator = getConditionList().iterator();
            queryBuffer.append(" ").append("(");
            while(iterator.hasNext())
            {
                ((GenericCondition)iterator.next()).toPolyglotSearch(queryBuffer, typeIndexMap, valueMap);
                if(iterator.hasNext())
                {
                    queryBuffer.append(" " + getOperator().getSQLString() + " ");
                }
            }
            queryBuffer.append(" ").append(")");
        }
    }


    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < this.conditionsList.size(); i++)
        {
            stringBuilder.append(this.conditionsList.get(i));
            if(i < this.conditionsList.size() - 1)
            {
                stringBuilder.append("_" + getOperator() + "_");
            }
        }
        return stringBuilder.toString();
    }


    public Map getResettableValues()
    {
        Map<Object, Object> valueMap = new HashMap<>();
        for(Iterator<GenericCondition> it = getConditionList().iterator(); it.hasNext(); )
        {
            valueMap.putAll(((GenericCondition)it.next()).getResettableValues());
        }
        return valueMap;
    }


    public void setResettableValue(String key, Object value)
    {
        for(Iterator<GenericCondition> it = getConditionList().iterator(); it.hasNext(); )
        {
            ((GenericCondition)it.next()).setResettableValue(key, value);
        }
    }


    public boolean isTranslatableToPolyglotDialect()
    {
        return getConditionList().stream().allMatch(GenericCondition::isTranslatableToPolyglotDialect);
    }
}
