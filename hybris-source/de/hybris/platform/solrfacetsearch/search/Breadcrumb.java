package de.hybris.platform.solrfacetsearch.search;

import java.io.Serializable;

public class Breadcrumb implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final String fieldName;
    private final String value;
    private final String displayValue;


    protected Breadcrumb(String fieldName, String value)
    {
        this(fieldName, value, null);
    }


    protected Breadcrumb(String fieldName, String value, String displayValue)
    {
        this.fieldName = fieldName;
        this.value = value;
        this.displayValue = displayValue;
    }


    public String getFieldName()
    {
        return this.fieldName;
    }


    public String getValue()
    {
        return this.value;
    }


    public String getDisplayValue()
    {
        return (this.displayValue == null) ? this.value : this.displayValue;
    }
}
