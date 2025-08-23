package de.hybris.platform.spring.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class ListMergeDirective
{
    private Object add;
    private String listPropertyDescriptor;
    private String fieldName;
    private List<String> afterBeanNames;
    private List<String> beforeBeanNames;
    private List<Object> afterValues;
    private List<Object> beforeValues;
    private List<Class> afterClasses;
    private List<Class> beforeClasses;


    public Object getAdd()
    {
        return this.add;
    }


    @Required
    public void setAdd(Object add)
    {
        this.add = add;
    }


    public List<String> getAfterBeanNames()
    {
        return this.afterBeanNames;
    }


    public void setAfterBeanNames(List<String> after)
    {
        this.afterBeanNames = after;
    }


    public List<String> getBeforeBeanNames()
    {
        return this.beforeBeanNames;
    }


    public void setBeforeBeanNames(List<String> beforeBeanNames)
    {
        this.beforeBeanNames = beforeBeanNames;
    }


    public List<Class> getAfterClasses()
    {
        return this.afterClasses;
    }


    public void setAfterClasses(List<Class<?>> afterClasses)
    {
        this.afterClasses = afterClasses;
    }


    public List<Class> getBeforeClasses()
    {
        return this.beforeClasses;
    }


    public void setBeforeClasses(List<Class<?>> beforeClasses)
    {
        this.beforeClasses = beforeClasses;
    }


    public String getListPropertyDescriptor()
    {
        return this.listPropertyDescriptor;
    }


    public void setListPropertyDescriptor(String listPropertyDescriptor)
    {
        this.listPropertyDescriptor = listPropertyDescriptor;
    }


    public List<Object> getAfterValues()
    {
        return this.afterValues;
    }


    public void setAfterValues(List<Object> afterValues)
    {
        this.afterValues = afterValues;
    }


    public List<Object> getBeforeValues()
    {
        return this.beforeValues;
    }


    public void setBeforeValues(List<Object> beforeValues)
    {
        this.beforeValues = beforeValues;
    }


    public String getFieldName()
    {
        return this.fieldName;
    }


    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }
}
