package de.hybris.platform.solrfacetsearch.provider;

import org.apache.commons.lang.builder.ToStringBuilder;

public class FieldValue
{
    private final String fieldName;
    private final Object value;


    public FieldValue(String fieldName, Object value)
    {
        this.fieldName = fieldName;
        this.value = value;
    }


    public String getFieldName()
    {
        return this.fieldName;
    }


    public Object getValue()
    {
        return this.value;
    }


    public String toString()
    {
        return (new ToStringBuilder(this)).append("field", this.fieldName).append("value", this.value).toString();
    }
}
