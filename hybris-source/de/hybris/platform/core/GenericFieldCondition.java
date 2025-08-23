package de.hybris.platform.core;

import java.util.Map;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class GenericFieldCondition extends GenericCondition
{
    protected static final boolean DEFAULT_CI = false;
    protected boolean caseInsensitive;
    private GenericSearchField field;


    protected GenericFieldCondition(GenericSearchField field, Operator operator)
    {
        this(field, operator, false);
    }


    protected GenericFieldCondition(GenericSearchField field, Operator operator, boolean caseInsensitive)
    {
        super(operator);
        this.field = field;
        this.caseInsensitive = caseInsensitive;
    }


    public GenericSearchField getField()
    {
        return this.field;
    }


    protected void setField(GenericSearchField field)
    {
        this.field = field;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public boolean isUpper()
    {
        return this.caseInsensitive;
    }


    public boolean isCaseInsensitive()
    {
        return this.caseInsensitive;
    }


    @Deprecated(since = "ages", forRemoval = true)
    protected void setUpper(boolean isUppercase)
    {
        setCaseInsensitive(isUppercase);
    }


    protected void setCaseInsensitive(boolean ignoreCase)
    {
        this.caseInsensitive = ignoreCase;
    }


    public void toFlexibleSearch(StringBuilder queryBuffer, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        if(getField() != null)
        {
            if(Operator.IS_NULL.equals(getOperator()) && getField().getFieldTypes().contains(GenericSearchFieldType.LOCALIZED))
            {
                getField().addFieldType(GenericSearchFieldType.OUTER_JOIN);
            }
            if(isCaseInsensitive())
            {
                queryBuffer.append("LOWER(");
            }
            getField().toFlexibleSearch(queryBuffer, typeIndexMap, valueMap);
            if(isCaseInsensitive())
            {
                queryBuffer.append(")");
            }
        }
        getOperator().toFlexibleSearch(queryBuffer, typeIndexMap, valueMap);
    }


    public void toPolyglotSearch(StringBuilder queryBuffer, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        if(getField() != null)
        {
            if(Operator.IS_NULL.equals(getOperator()) && getField().getFieldTypes().contains(GenericSearchFieldType.LOCALIZED))
            {
                getField().addFieldType(GenericSearchFieldType.OUTER_JOIN);
            }
            if(isCaseInsensitive())
            {
                queryBuffer.append("LOWER(");
            }
            getField().toPolyglotSearch(queryBuffer, typeIndexMap, valueMap);
            if(isCaseInsensitive())
            {
                queryBuffer.append(")");
            }
        }
        getOperator().toPolyglotSearch(queryBuffer, typeIndexMap, valueMap);
    }


    public boolean equals(Object arg0)
    {
        if(arg0 == null || getClass() != arg0.getClass())
        {
            return false;
        }
        if(this == arg0)
        {
            return true;
        }
        return (((GenericFieldCondition)arg0).getField().equals(getField()) && ((GenericFieldCondition)arg0)
                        .getOperator().equals(getOperator()));
    }


    public int hashCode()
    {
        return (new HashCodeBuilder()).append(getField()).append(getOperator()).toHashCode();
    }


    public boolean isTranslatableToPolyglotDialect()
    {
        if(this.field == null || !this.field.isTranslatableToPolyglotDialect())
        {
            return false;
        }
        Operator operator = getOperator();
        if(operator == null || !operator.isTranslatableToPolyglotDialect())
        {
            return false;
        }
        if(this.caseInsensitive)
        {
            return false;
        }
        return getField().getFieldTypes().isEmpty();
    }
}
