/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.util;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.core.ResolvableType;

public class BeanLookup implements ListableBeanFactory
{
    private final Map<String, Object> beans = new HashMap<>();


    void addBean(final String name, final Object bean)
    {
        if(beans.containsKey(name))
        {
            throw new BeanDefinitionValidationException("Bean already defined with provided name: ".concat(name));
        }
        beans.put(name, bean);
    }


    @Override
    public boolean containsBeanDefinition(final String name)
    {
        return beans.containsKey(name);
    }


    @Override
    public int getBeanDefinitionCount()
    {
        return beans.size();
    }


    @Override
    public String[] getBeanDefinitionNames()
    {
        return beans.keySet().toArray(new String[0]);
    }


    @Override
    public <T> ObjectProvider<T> getBeanProvider(Class<T> aClass, boolean b)
    {
        return null;
    }


    @Override
    public <T> ObjectProvider<T> getBeanProvider(ResolvableType resolvableType, boolean b)
    {
        return null;
    }


    @Override
    public String[] getBeanNamesForType(final Class<?> type)
    {
        return beans.entrySet().stream().filter(entry -> type.isAssignableFrom(entry.getValue().getClass())).map(Map.Entry::getKey)
                        .collect(Collectors.toList()).toArray(new String[0]);
    }


    @Override
    public String[] getBeanNamesForType(final Class<?> type, final boolean includePrototypes, final boolean allowEagerInit)
    {
        return getBeanNamesForType(type);
    }


    @Override
    public <T> Map<String, T> getBeansOfType(final Class<T> type)
    {
        return beans.entrySet().stream().filter(entry -> type.isAssignableFrom(entry.getValue().getClass()))
                        .collect(Collectors.<Map.Entry, String, T>toMap(entry -> (String)entry.getKey(), entry -> (T)entry.getValue()));
    }


    @Override
    public <T> Map<String, T> getBeansOfType(final Class<T> type, final boolean includePrototypes, final boolean allowEagerInit)
    {
        return getBeansOfType(type);
    }


    @Override
    public String[] getBeanNamesForAnnotation(final Class<? extends Annotation> annotationType)
    {
        return beans.entrySet().stream().filter(entry -> entry.getValue().getClass().isAnnotationPresent(annotationType))
                        .map(Map.Entry::getKey).collect(Collectors.<String>toList()).toArray(new String[0]);
    }


    @Override
    public Map<String, Object> getBeansWithAnnotation(final Class<? extends Annotation> annotationType)
    {
        return beans.entrySet().stream().filter(entry -> entry.getValue().getClass().isAnnotationPresent(annotationType))
                        .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
    }


    @Override
    public <A extends Annotation> A findAnnotationOnBean(final String name, final Class<A> annotationType)
    {
        final Object bean = getBean(name);
        return bean != null ? bean.getClass().getAnnotation(annotationType) : null;
    }


    @Override
    public <A extends Annotation> A findAnnotationOnBean(final String name, final Class<A> annotationType, boolean b)
                    throws NoSuchBeanDefinitionException
    {
        final Object bean = getBean(name);
        return bean != null ? bean.getClass().getAnnotation(annotationType) : null;
    }


    @Override
    public Object getBean(final String name)
    {
        final Object result = beans.get(name);
        if(result == null)
        {
            throw new NoSuchBeanDefinitionException(name);
        }
        return result;
    }


    @Override
    public <T> T getBean(final String name, final Class<T> requiredType)
    {
        final Object bean = getBean(name);
        if(bean != null && requiredType.isAssignableFrom(bean.getClass()))
        {
            return (T)bean;
        }
        else
        {
            throw new NoSuchBeanDefinitionException(name);
        }
    }


    @Override
    public <T> T getBean(final Class<T> requiredType)
    {
        final Optional<Object> first = beans.values().stream().filter(bean -> requiredType.isAssignableFrom(bean.getClass()))
                        .findFirst();
        if(!first.isPresent())
        {
            throw new NoSuchBeanDefinitionException(requiredType);
        }
        return (T)first.get();
    }


    @Override
    public Object getBean(final String name, final Object... objects)
    {
        return getBean(name);
    }


    @Override
    public <T> T getBean(final Class<T> requiredType, final Object... objects)
    {
        return getBean(requiredType);
    }


    @Override
    public <T> ObjectProvider<T> getBeanProvider(final Class<T> requiredType)
    {
        return getBeanProvider(ResolvableType.forRawClass(requiredType));
    }


    @Override
    public <T> ObjectProvider<T> getBeanProvider(final ResolvableType requiredType)
    {
        return new BeanLookupObjectProvider<T>(this, requiredType);
    }


    @Override
    public boolean containsBean(final String name)
    {
        return beans.containsKey(name);
    }


    @Override
    public boolean isSingleton(final String name)
    {
        if(!containsBean(name))
        {
            throw new NoSuchBeanDefinitionException(name);
        }
        return true;
    }


    @Override
    public boolean isPrototype(final String name)
    {
        if(!containsBean(name))
        {
            throw new NoSuchBeanDefinitionException(name);
        }
        return false;
    }


    @Override
    public boolean isTypeMatch(final String name, final Class<?> typeToMatch)
    {
        return typeToMatch.isAssignableFrom(getBean(name).getClass());
    }


    @Override
    public Class<?> getType(final String name)
    {
        return getBean(name).getClass();
    }


    @Override
    public Class<?> getType(final String name, final boolean allowFactoryBeanInit) throws NoSuchBeanDefinitionException
    {
        return getType(name);
    }


    @Override
    public String[] getAliases(final String name)
    {
        return new String[0];
    }


    @Override
    public boolean isTypeMatch(final String name, final ResolvableType resolvableType)
    {
        return isTypeMatch(name, resolvableType.getRawClass());
    }


    @Override
    public String[] getBeanNamesForType(final ResolvableType resolvableType)
    {
        return getBeanNamesForType(resolvableType.getRawClass());
    }


    @Override
    public String[] getBeanNamesForType(final ResolvableType resolvableType, final boolean includeNonSingletons, final boolean allowEagerInit)
    {
        return getBeanNamesForType(resolvableType);
    }


    static class BeanLookupObjectProvider<T> implements ObjectProvider<T>
    {
        private final BeanLookup parent;
        private final ResolvableType requiredType;


        public BeanLookupObjectProvider(final BeanLookup parent, final ResolvableType requiredType)
        {
            this.parent = parent;
            this.requiredType = requiredType;
        }


        @Override
        public T getObject(final Object... args) throws BeansException
        {
            final String[] beanNames = parent.getBeanNamesForType(requiredType);
            if(beanNames.length == 1)
            {
                return (T)parent.getBean(beanNames[0], args);
            }
            else if(beanNames.length > 1)
            {
                throw new NoUniqueBeanDefinitionException(requiredType, beanNames);
            }
            else
            {
                throw new NoSuchBeanDefinitionException(requiredType);
            }
        }


        @Override
        public T getIfAvailable() throws BeansException
        {
            final String[] beanNames = parent.getBeanNamesForType(requiredType);
            if(beanNames.length == 1)
            {
                return (T)parent.getBean(beanNames[0]);
            }
            else if(beanNames.length > 1)
            {
                throw new NoUniqueBeanDefinitionException(requiredType, beanNames);
            }
            else
            {
                return null;
            }
        }


        @Override
        public T getIfUnique() throws BeansException
        {
            final String[] beanNames = parent.getBeanNamesForType(requiredType);
            if(beanNames.length == 1)
            {
                return (T)parent.getBean(beanNames[0]);
            }
            else
            {
                return null;
            }
        }


        @Override
        public T getObject() throws BeansException
        {
            final String[] beanNames = parent.getBeanNamesForType(requiredType);
            if(beanNames.length == 1)
            {
                return (T)parent.getBean(beanNames[0], requiredType.resolve());
            }
            else if(beanNames.length > 1)
            {
                throw new NoUniqueBeanDefinitionException(requiredType, beanNames);
            }
            else
            {
                throw new NoSuchBeanDefinitionException(requiredType);
            }
        }
    }
}
