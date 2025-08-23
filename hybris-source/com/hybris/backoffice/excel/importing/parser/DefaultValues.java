package com.hybris.backoffice.excel.importing.parser;

import java.util.Collection;
import java.util.Map;

public class DefaultValues
{
    private final String defaultValuesCellValues;
    private final String referenceFormat;
    private final Map<String, String> parsedDefaultValues;


    public DefaultValues(String defaultValuesCellValues, String referenceFormat, Map<String, String> parsedDefaultValues)
    {
        this.defaultValuesCellValues = defaultValuesCellValues;
        this.referenceFormat = referenceFormat;
        this.parsedDefaultValues = parsedDefaultValues;
    }


    public String getDefaultValues()
    {
        return this.defaultValuesCellValues;
    }


    public String getReferenceFormat()
    {
        return this.referenceFormat;
    }


    public Map<String, String> toMap()
    {
        return this.parsedDefaultValues;
    }


    public String getDefaultValue(String key)
    {
        return this.parsedDefaultValues.get(key);
    }


    public Collection<String> getKeys()
    {
        return this.parsedDefaultValues.keySet();
    }


    public Collection<String> getValues()
    {
        return this.parsedDefaultValues.values();
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        DefaultValues that = (DefaultValues)o;
        if((this.defaultValuesCellValues != null) ? !this.defaultValuesCellValues.equals(that.defaultValuesCellValues) : (that.defaultValuesCellValues != null))
        {
            return false;
        }
        if((this.referenceFormat != null) ? !this.referenceFormat.equals(that.referenceFormat) : (that.referenceFormat != null))
        {
            return false;
        }
        return (this.parsedDefaultValues != null) ? this.parsedDefaultValues.equals(that.parsedDefaultValues) : (
                        (that.parsedDefaultValues == null));
    }


    public int hashCode()
    {
        int result = (this.defaultValuesCellValues != null) ? this.defaultValuesCellValues.hashCode() : 0;
        result = 31 * result + ((this.referenceFormat != null) ? this.referenceFormat.hashCode() : 0);
        result = 31 * result + ((this.parsedDefaultValues != null) ? this.parsedDefaultValues.hashCode() : 0);
        return result;
    }
}
