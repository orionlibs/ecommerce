package de.hybris.platform.webservicescommons.mapping.config;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class FieldMapper
{
    private Class sourceClass;
    private Class destClass;
    private List<Class> sourceClassArguments;
    private List<Class> destClassArguments;
    private Map<String, String> fieldMapping;


    public Class getSourceClass()
    {
        return this.sourceClass;
    }


    public void setSourceClass(Class sourceClass)
    {
        this.sourceClass = sourceClass;
    }


    public Class getDestClass()
    {
        return this.destClass;
    }


    public void setDestClass(Class destClass)
    {
        this.destClass = destClass;
    }


    public Map<String, String> getFieldMapping()
    {
        return this.fieldMapping;
    }


    public void setFieldMapping(Map<String, String> fieldMapping)
    {
        this.fieldMapping = fieldMapping;
    }


    public List<Class> getSourceClassArguments()
    {
        return this.sourceClassArguments;
    }


    public void setSourceClassArguments(List<Class<?>> sourceClassArguments)
    {
        this.sourceClassArguments = sourceClassArguments;
    }


    public List<Class> getDestClassArguments()
    {
        return this.destClassArguments;
    }


    public void setDestClassArguments(List<Class<?>> destClassArguments)
    {
        this.destClassArguments = destClassArguments;
    }


    public Type[] getSourceActualTypeArguments()
    {
        if(this.sourceClassArguments != null)
        {
            return this.sourceClassArguments.<Type>toArray(new Type[0]);
        }
        return null;
    }


    public Type[] getDestActualTypeArguments()
    {
        if(this.sourceClassArguments != null)
        {
            return this.destClassArguments.<Type>toArray(new Type[0]);
        }
        return null;
    }
}
