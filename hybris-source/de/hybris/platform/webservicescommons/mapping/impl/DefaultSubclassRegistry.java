package de.hybris.platform.webservicescommons.mapping.impl;

import de.hybris.platform.webservicescommons.mapping.SubclassRegistry;
import de.hybris.platform.webservicescommons.mapping.config.SubclassMapping;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

public class DefaultSubclassRegistry implements SubclassRegistry, ApplicationContextAware
{
    private static final String PARAMETER_PARENT_CLASS_NOT_NULL_MESSAGE = "Parameter parentClass cannot be null.";
    private static final String PARAMETER_SUBCLASS_NOT_NULL_MESSAGE = "Parameter subclass cannot be null.";
    private static final String PARAMETER_SUBCLASSES_NOT_NULL_MESSAGE = "Parameter subclasses cannot be null.";
    private ApplicationContext applicationContext;
    private final Map<Class, Set<Class>> subclassesMap = (Map)new HashMap<>();


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }


    public void init()
    {
        buildSubclassesMap(this.applicationContext);
    }


    protected void buildSubclassesMap(ApplicationContext applicationContext)
    {
        Map<String, SubclassMapping> mappings = BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)applicationContext, SubclassMapping.class);
        for(SubclassMapping subclassMapping : mappings.values())
        {
            registerSubclasses(subclassMapping.getParentClass(), subclassMapping.getSubclassesSet());
        }
    }


    public Set<Class> getSubclasses(Class parentClass)
    {
        Assert.notNull(parentClass, "Parameter parentClass cannot be null.");
        Set<Class<?>> subclassesSet = this.subclassesMap.get(parentClass);
        return (subclassesSet == null) ? Collections.<Class<?>>emptySet() : (Set)new HashSet<>(subclassesSet);
    }


    public Set<Class> getAllSubclasses(Class parentClass)
    {
        Assert.notNull(parentClass, "Parameter parentClass cannot be null.");
        Set<Class<?>> subclassesSet = new HashSet<>();
        createSubclassesSet(parentClass, subclassesSet);
        return subclassesSet;
    }


    protected void createSubclassesSet(Class parentClass, Set<Class<?>> allSubclasses)
    {
        Set<Class<?>> baseSubclassesSet = this.subclassesMap.getOrDefault(parentClass, Collections.emptySet());
        for(Class<?> subclass : baseSubclassesSet)
        {
            if(!allSubclasses.contains(subclass))
            {
                allSubclasses.add(subclass);
                createSubclassesSet(subclass, allSubclasses);
            }
        }
    }


    public void registerSubclass(Class parentClass, Class<?> subclass)
    {
        Assert.notNull(parentClass, "Parameter parentClass cannot be null.");
        Assert.notNull(subclass, "Parameter subclass cannot be null.");
        Set<Class<?>> subclassesSet = this.subclassesMap.computeIfAbsent(parentClass, key -> new HashSet());
        subclassesSet.add(subclass);
    }


    public void registerSubclasses(Class parentClass, Set<Class<?>> subclasses)
    {
        Assert.notNull(parentClass, "Parameter parentClass cannot be null.");
        Assert.notNull(subclasses, "Parameter subclasses cannot be null.");
        Set<Class<?>> subclassesSet = this.subclassesMap.computeIfAbsent(parentClass, key -> new HashSet());
        subclassesSet.addAll(subclasses);
    }


    public void unregisterSubclass(Class parentClass, Class subclass)
    {
        Assert.notNull(parentClass, "Parameter parentClass cannot be null.");
        Assert.notNull(subclass, "Parameter subclass cannot be null.");
        Set<Class<?>> subclassesSet = this.subclassesMap.get(parentClass);
        if(subclassesSet != null)
        {
            subclassesSet.remove(subclass);
        }
    }


    public void unregisterSubclasses(Class parentClass, Set<Class<?>> subclasses)
    {
        Assert.notNull(parentClass, "Parameter parentClass cannot be null.");
        Assert.notNull(subclasses, "Parameter subclasses cannot be null.");
        Set<Class<?>> subclassesSet = this.subclassesMap.get(parentClass);
        if(subclassesSet != null)
        {
            for(Class<?> subclass : subclasses)
            {
                subclassesSet.remove(subclass);
            }
        }
    }


    public void unregisterSubclasses(Class parentClass)
    {
        Assert.notNull(parentClass, "Parameter parentClass cannot be null.");
        this.subclassesMap.remove(parentClass);
    }
}
