package de.hybris.platform.spring.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanNameReference;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;

class BeanDefinitionValueResolver
{
    private final ConfigurableBeanFactory beanFactory;
    private final String beanName;
    private final BeanDefinition beanDefinition;
    private final TypeConverter typeConverter;


    public BeanDefinitionValueResolver(ConfigurableBeanFactory beanFactory, String beanName, BeanDefinition beanDefinition, TypeConverter typeConverter)
    {
        this.beanFactory = beanFactory;
        this.beanName = beanName;
        this.beanDefinition = beanDefinition;
        this.typeConverter = typeConverter;
    }


    public Object resolveValueIfNecessary(Object argName, Object value)
    {
        if(value instanceof RuntimeBeanReference)
        {
            RuntimeBeanReference ref = (RuntimeBeanReference)value;
            return resolveReference(argName, ref);
        }
        if(value instanceof RuntimeBeanNameReference)
        {
            String refName = ((RuntimeBeanNameReference)value).getBeanName();
            if(!this.beanFactory.containsBean(refName))
            {
                throw new BeanDefinitionStoreException("Invalid bean name '" + refName + "' in bean reference for " + argName);
            }
            return refName;
        }
        if(value instanceof org.springframework.beans.factory.config.BeanDefinitionHolder)
        {
            throw new BeanCreationException("Error creating value for [" + argName + "] property. BeanDefinitionHolder type is not supported in *MergeDirective. Please refactor the bean definition.");
        }
        if(value instanceof BeanDefinition)
        {
            throw new BeanCreationException("Error creating value for [" + argName + "] property. BeanDefinition type is not supported in *MergeDirective. Please refactor the bean definition.");
        }
        if(value instanceof org.springframework.beans.factory.support.ManagedArray)
        {
            throw new BeanCreationException("Error creating value for [" + argName + "] property. ManagedArray type is not supported in *MergeDirective. Please refactor the bean definition.");
        }
        if(value instanceof org.springframework.beans.factory.support.ManagedList)
        {
            return resolveManagedList(argName, (List)value);
        }
        if(value instanceof org.springframework.beans.factory.support.ManagedSet)
        {
            return resolveManagedSet(argName, (Set)value);
        }
        if(value instanceof org.springframework.beans.factory.support.ManagedMap)
        {
            return resolveManagedMap(argName, (Map<?, ?>)value);
        }
        if(value instanceof org.springframework.beans.factory.support.ManagedProperties)
        {
            Properties original = (Properties)value;
            Properties copy = new Properties();
            for(Map.Entry<Object, Object> propEntry : original.entrySet())
            {
                Object propKey = propEntry.getKey();
                Object propValue = propEntry.getValue();
                if(propKey instanceof TypedStringValue)
                {
                    propKey = ((TypedStringValue)propKey).getValue();
                }
                if(propValue instanceof TypedStringValue)
                {
                    propValue = ((TypedStringValue)propValue).getValue();
                }
                copy.put(propKey, propValue);
            }
            return copy;
        }
        if(value instanceof TypedStringValue)
        {
            TypedStringValue typedStringValue = (TypedStringValue)value;
            Object valueObject = typedStringValue.getValue();
            try
            {
                Class<?> resolvedTargetType = resolveTargetType(typedStringValue);
                if(resolvedTargetType != null)
                {
                    return this.typeConverter.convertIfNecessary(valueObject, resolvedTargetType);
                }
                return valueObject;
            }
            catch(Throwable ex)
            {
                throw new BeanCreationException(this.beanDefinition.getResourceDescription(), this.beanName, "Error converting typed String value for " + argName, ex);
            }
        }
        return value;
    }


    protected Class<?> resolveTargetType(TypedStringValue value) throws ClassNotFoundException
    {
        if(value.hasTargetType())
        {
            return value.getTargetType();
        }
        return value.resolveTargetType(this.beanFactory.getBeanClassLoader());
    }


    private Object resolveReference(Object argName, RuntimeBeanReference ref)
    {
        try
        {
            String refName = ref.getBeanName();
            if(ref.isToParent())
            {
                if(this.beanFactory.getParentBeanFactory() == null)
                {
                    throw new BeanCreationException(this.beanDefinition.getResourceDescription(), this.beanName, "Can't resolve reference to bean '" + refName + "' in parent factory: no parent factory available");
                }
                return this.beanFactory.getParentBeanFactory().getBean(refName);
            }
            Object bean = this.beanFactory.getBean(refName);
            return bean;
        }
        catch(BeansException ex)
        {
            throw new BeanCreationException(this.beanDefinition.getResourceDescription(), this.beanName, "Cannot resolve reference to bean '" + ref
                            .getBeanName() + "' while setting " + argName, ex);
        }
    }


    private List<?> resolveManagedList(Object argName, List<?> ml)
    {
        List<Object> resolved = new ArrayList(ml.size());
        for(int i = 0; i < ml.size(); i++)
        {
            resolved.add(resolveValueIfNecessary(new KeyedArgName(argName, Integer.valueOf(i)), ml.get(i)));
        }
        return resolved;
    }


    private Set<?> resolveManagedSet(Object argName, Set<?> ms)
    {
        Set<Object> resolved = new LinkedHashSet(ms.size());
        int i = 0;
        for(Object m : ms)
        {
            resolved.add(resolveValueIfNecessary(new KeyedArgName(argName, Integer.valueOf(i)), m));
            i++;
        }
        return resolved;
    }


    private Map<?, ?> resolveManagedMap(Object argName, Map<?, ?> mm)
    {
        Map<Object, Object> resolved = new LinkedHashMap<>(mm.size());
        for(Map.Entry<?, ?> entry : mm.entrySet())
        {
            Object resolvedKey = resolveValueIfNecessary(argName, entry.getKey());
            Object resolvedValue = resolveValueIfNecessary(new KeyedArgName(argName, entry.getKey()), entry.getValue());
            resolved.put(resolvedKey, resolvedValue);
        }
        return resolved;
    }
}
