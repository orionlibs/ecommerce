package de.hybris.platform.webservicescommons.mapping.config;

import java.util.Set;

public class SubclassMapping
{
    private Class parentClass;
    private Set<Class> subclassesSet;


    public Class getParentClass()
    {
        return this.parentClass;
    }


    public void setParentClass(Class parentClass)
    {
        this.parentClass = parentClass;
    }


    public Set<Class> getSubclassesSet()
    {
        return this.subclassesSet;
    }


    public void setSubclassesSet(Set<Class<?>> subclassesSet)
    {
        this.subclassesSet = subclassesSet;
    }
}
