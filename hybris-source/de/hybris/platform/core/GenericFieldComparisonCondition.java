package de.hybris.platform.core;

import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class GenericFieldComparisonCondition extends GenericFieldCondition
{
    private GenericSearchField comparisonField;
    private static final Set<Operator> ALLOWED_OPERATORS = (Set<Operator>)ImmutableSet.copyOf(Arrays.asList(new Operator[] {Operator.EQUAL, Operator.UNEQUAL, Operator.LIKE, Operator.NOT_LIKE, Operator.STARTS_WITH, Operator.ENDS_WITH, Operator.GREATER_OR_EQUAL, Operator.LESS_OR_EQUAL}));


    protected GenericFieldComparisonCondition(GenericSearchField field, Operator operator, GenericSearchField comparisonField)
    {
        super(field, operator);
        this.comparisonField = comparisonField;
    }


    protected GenericFieldComparisonCondition(GenericSearchField field, Operator operator, GenericSearchField comparisonField, boolean upper)
    {
        super(field, operator, upper);
        this.comparisonField = comparisonField;
    }


    protected void checkOperator()
    {
        if(!ALLOWED_OPERATORS.contains(getOperator()))
        {
            throw new IllegalArgumentException("GenericFieldComparisonCondition is only compatible with following operators: [EQUAL,UNEQUAL,LIKE,NOT_LIKE,STARTS_WITH,ENDS_WITH,GREATER_OR_EQUAL,LESS_OR_EQUAL]");
        }
    }


    public GenericSearchField getComparisonField()
    {
        return this.comparisonField;
    }


    public void setComparisonField(GenericSearchField comparisonField)
    {
        this.comparisonField = comparisonField;
    }


    public void toFlexibleSearch(StringBuilder queryBuffer, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        super.toFlexibleSearch(queryBuffer, typeIndexMap, valueMap);
        if(isCaseInsensitive())
        {
            queryBuffer.append("LOWER(");
        }
        getComparisonField().toFlexibleSearch(queryBuffer, typeIndexMap, valueMap);
        if(isCaseInsensitive())
        {
            queryBuffer.append(")");
        }
    }


    public boolean equals(Object obj)
    {
        if(obj == null)
        {
            return false;
        }
        if(!getClass().equals(obj.getClass()))
        {
            return false;
        }
        return (((GenericFieldComparisonCondition)obj).getComparisonField().equals(getComparisonField()) && super
                        .equals(obj));
    }


    public int hashCode()
    {
        return (new HashCodeBuilder()).appendSuper(super.hashCode()).append(getComparisonField()).toHashCode();
    }


    public Map getResettableValues()
    {
        return Collections.emptyMap();
    }


    public void setResettableValue(String key, Object value)
    {
    }


    public boolean isTranslatableToPolyglotDialect()
    {
        return false;
    }
}
