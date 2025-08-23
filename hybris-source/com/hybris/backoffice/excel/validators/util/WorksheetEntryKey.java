package com.hybris.backoffice.excel.validators.util;

import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import java.util.Map;
import java.util.Objects;

public class WorksheetEntryKey
{
    private final RequiredAttribute requiredAttribute;
    private final Map<String, String> uniqueAttributesValues;


    public WorksheetEntryKey(RequiredAttribute requiredAttribute, Map<String, String> uniqueAttributesValues)
    {
        this.requiredAttribute = requiredAttribute;
        this.uniqueAttributesValues = uniqueAttributesValues;
    }


    public RequiredAttribute getRequiredAttribute()
    {
        return this.requiredAttribute;
    }


    public Map<String, String> getUniqueAttributesValues()
    {
        return this.uniqueAttributesValues;
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
        WorksheetEntryKey that = (WorksheetEntryKey)o;
        return (Objects.equals(this.requiredAttribute, that.requiredAttribute) &&
                        Objects.equals(this.uniqueAttributesValues, that.uniqueAttributesValues));
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {getRequiredAttribute(), getUniqueAttributesValues()});
    }
}
