package de.hybris.platform.spring.config;

import java.util.Map;
import org.springframework.beans.factory.InitializingBean;

public class MapMergeDirective implements InitializingBean
{
    private Object key;
    private Object value;
    private String mapPropertyDescriptor;
    private String fieldName;
    private Map<Object, Object> sourceMap;


    public Object getKey()
    {
        return this.key;
    }


    public void setKey(Object key)
    {
        this.key = key;
    }


    public Object getValue()
    {
        return this.value;
    }


    public void setValue(Object value)
    {
        this.value = value;
    }


    public String getMapPropertyDescriptor()
    {
        return this.mapPropertyDescriptor;
    }


    public void setMapPropertyDescriptor(String mapPropertyDescriptor)
    {
        this.mapPropertyDescriptor = mapPropertyDescriptor;
    }


    public String getFieldName()
    {
        return this.fieldName;
    }


    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }


    public Map<Object, Object> getSourceMap()
    {
        return this.sourceMap;
    }


    public void setSourceMap(Map<Object, Object> sourceMap)
    {
        this.sourceMap = sourceMap;
    }


    public void afterPropertiesSet() throws Exception
    {
        if(getSourceMap() == null && getKey() == null && getValue() == null)
        {
            throw new IllegalStateException("Either key/value or sourceMap parameters should be injected into MapMergeDirective.");
        }
    }
}
