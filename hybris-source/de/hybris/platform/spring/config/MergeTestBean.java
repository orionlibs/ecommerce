package de.hybris.platform.spring.config;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MergeTestBean
{
    private List<String> stringList;
    private List<Object> objectList;
    private List<BigDecimal> decimalList;
    private List<String> nullStringList;
    private Map<String, String> stringMap;
    private Map<String, Collection<String>> multiMap;
    private Map<String, String> nullStringMap;


    public List<String> getStringList()
    {
        return this.stringList;
    }


    public void setStringList(List<String> stringList)
    {
        this.stringList = stringList;
    }


    public List<Object> getObjectList()
    {
        return this.objectList;
    }


    public void setObjectList(List<Object> objectList)
    {
        this.objectList = objectList;
    }


    public void setDecimalList(List<BigDecimal> decimalList)
    {
        this.decimalList = decimalList;
    }


    public List<BigDecimal> getDecimalList()
    {
        return this.decimalList;
    }


    public Map<String, String> getStringMap()
    {
        return this.stringMap;
    }


    public void setStringMap(Map<String, String> stringMap)
    {
        this.stringMap = stringMap;
    }


    public Map<String, Collection<String>> getMultiMap()
    {
        return this.multiMap;
    }


    public void setMultiMap(Map<String, Collection<String>> multiMap)
    {
        this.multiMap = multiMap;
    }


    public List<String> getNullStringList()
    {
        return this.nullStringList;
    }


    public void setNullStringList(List<String> nullStringList)
    {
        this.nullStringList = nullStringList;
    }


    public Map<String, String> getNullStringMap()
    {
        return this.nullStringMap;
    }


    public void setNullStringMap(Map<String, String> nullStringMap)
    {
        this.nullStringMap = nullStringMap;
    }
}
