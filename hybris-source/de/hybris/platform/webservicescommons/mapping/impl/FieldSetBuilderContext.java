package de.hybris.platform.webservicescommons.mapping.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FieldSetBuilderContext implements Serializable
{
    private Map<String, Class> typeVariableMap;
    private int recurrencyLevel = 4;
    private Map<Class, Integer> recurrencyMap = (Map)new HashMap<>();
    private int fieldCounter = 0;
    private int maxFieldSetSize = 50000;


    public void addToRecurrencyMap(Class clazz)
    {
        if(this.recurrencyMap != null)
        {
            if(this.recurrencyMap.containsKey(clazz))
            {
                Integer value = this.recurrencyMap.get(clazz);
                value = Integer.valueOf(value.intValue() + 1);
                this.recurrencyMap.put(clazz, value);
            }
            else
            {
                this.recurrencyMap.put(clazz, Integer.valueOf(1));
            }
        }
    }


    public void removeFromRecurrencyMap(Class clazz)
    {
        if(this.recurrencyMap != null && this.recurrencyMap.containsKey(clazz))
        {
            Integer value = this.recurrencyMap.get(clazz);
            value = Integer.valueOf(value.intValue() - 1);
            this.recurrencyMap.put(clazz, value);
        }
    }


    public boolean isRecurencyLevelExceeded(Class clazz)
    {
        if(this.recurrencyMap != null)
        {
            Integer value = this.recurrencyMap.get(clazz);
            if(value != null)
            {
                return (value.intValue() > this.recurrencyLevel);
            }
        }
        return false;
    }


    public void resetRecurrencyMap()
    {
        this.recurrencyMap = (Map)new HashMap<>();
    }


    public Map<String, Class> getTypeVariableMap()
    {
        return this.typeVariableMap;
    }


    public void setTypeVariableMap(Map<String, Class<?>> typeVariableMap)
    {
        this.typeVariableMap = typeVariableMap;
    }


    public int getRecurrencyLevel()
    {
        return this.recurrencyLevel;
    }


    public void setRecurrencyLevel(int recurrencyLevel)
    {
        this.recurrencyLevel = recurrencyLevel;
    }


    public void resetFieldCounter()
    {
        this.fieldCounter = 0;
    }


    public void incrementFieldCounter()
    {
        this.fieldCounter++;
    }


    public boolean isMaxFieldSetSizeExceeded()
    {
        return (this.fieldCounter > this.maxFieldSetSize);
    }


    public int getMaxFieldSetSize()
    {
        return this.maxFieldSetSize;
    }


    public void setMaxFieldSetSize(int maxFieldSetSize)
    {
        this.maxFieldSetSize = maxFieldSetSize;
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
        FieldSetBuilderContext that = (FieldSetBuilderContext)o;
        if(this.recurrencyLevel != that.recurrencyLevel)
        {
            return false;
        }
        if(this.typeVariableMap == null)
        {
            return (that.typeVariableMap == null);
        }
        return this.typeVariableMap.equals(that.typeVariableMap);
    }


    public int hashCode()
    {
        int result = (this.typeVariableMap != null) ? this.typeVariableMap.hashCode() : 0;
        result = 31 * result + this.recurrencyLevel;
        return result;
    }
}
