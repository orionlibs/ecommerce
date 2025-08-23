/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;

/**
 * Convenience methods dealing with java reflection api.
 */
public final class ReflectionUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(ReflectionUtils.class);


    private ReflectionUtils()
    {
        // Utility class should not be instantiated
        throw new AssertionError();
    }


    /**
     * Getting all declared fields from the specified class and all its superclasses.
     *
     * @see Class#getDeclaredFields()
     */
    public static Field[] getAllDeclaredFields(final Class<?> clazz)
    {
        final List<Field> ret = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        Class<?> current = clazz.getSuperclass();
        while(!(current == null || Object.class.equals(current)))
        {
            ret.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        return ret.toArray(new Field[ret.size()]);
    }


    /**
     * Looks for declared field in specified class and all its superclasses.
     *
     * @see Class#getDeclaredFields()
     */
    public static Field getDeclaredField(final Class<?> clazz, final String fieldName) throws NoSuchFieldException
    {
        try
        {
            return clazz.getDeclaredField(fieldName);
        }
        catch(final NoSuchFieldException ex)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error while reading declared field", ex);
            }
            final Class<?> current = clazz.getSuperclass();
            if(current != null && !Object.class.equals(current))
            {
                return getDeclaredField(current, fieldName);
            }
            else
            {
                throw new NoSuchFieldException(fieldName);
            }
        }
    }


    /**
     * Getting a fields value regardless its accessibility.
     *
     * @param field
     *           The field which value should be read.
     * @param object
     *           The object at which the field value should be read
     * @throws IllegalArgumentException
     * @see Field#get(Object)
     */
    public static <V> V getField(final Field field, final Object object)
    {
        final boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try
        {
            return (V)field.get(object);
        }
        catch(final IllegalAccessException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
        finally
        {
            field.setAccessible(accessible);
        }
    }


    /**
     * Setting a fields value regardless its accessibility.
     *
     * @param field
     *           The field which value should be set.
     * @param object
     *           The object at which the field value should be set
     * @param value
     *           The value
     * @throws IllegalArgumentException
     * @see Field#set(Object, Object)
     */
    public static void setField(final Field field, final Object object, final Object value) throws IllegalArgumentException
    {
        setField(field, object, value, null);
    }


    /**
     * Setting a fields value regardless its accessibility.
     *
     * @param field
     *           The field which value should be set.
     * @param object
     *           The object at which the field value should be set
     * @param value
     *           The value
     * @param annotationConstraint
     *           A annotation class serving as constraint, i.e. the field value will only be set, if this annotation is
     *           present.
     * @see Field#set(Object, Object)
     */
    public static void setField(final Field field, final Object object, final Object value,
                    final Class<? extends Annotation> annotationConstraint)
    {
        if(annotationConstraint != null && !field.isAnnotationPresent(annotationConstraint))
        {
            return;
        }
        final boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try
        {
            field.set(object, value);
        }
        catch(final IllegalAccessException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
        }
        finally
        {
            field.setAccessible(accessible);
        }
    }


    /**
     * Simplified version of method {@link #extractGenericParameterType(Class, Class, int)}. A superclass of provided class
     * is taken as a baseClass.
     */
    public static <K> Class<K> extractGenericParameterType(final Class<?> clazz, final int position)
    {
        final Class<? extends Object> child = clazz;
        final Class<Object> base = (Class<Object>)child.getSuperclass();
        return extractGenericParameterType(child, base, position);
    }


    /**
     * Simplified version of method {@link #extractGenericParameterType(Class, Class, int)}. First generic parameter is
     * assumed (position 0).
     */
    public static <P, C extends P, K> Class<K> extractGenericParameterType(final Class<C> clazz, final Class<P> baseClass)
    {
        return extractGenericParameterType(clazz, baseClass, 0);
    }


    /**
     * Helper method to fetch generic argument's type of a class. For example when invoked on a class that extends from
     * ArrayList&lt;String&gt;:
     * <p>
     * <code>
     * ReflectionUtils.extractGenericParameterType(new ArrayList&lt;String&gt;(){}.getClass(), 0)
     * </code>
     * </p>
     * <p>
     * returns class java.lang.String.
     * </p>
     * Method may resolve only those parameters that are already set in class definition. If a class defines a generic
     * parameter by itself, then i may not be resolved, i.e.
     * <p>
     * <code>
     * ReflectionUtils.extractGenericParameterType(new ArrayList&lt;String&gt;().getClass(), 0)
     * </code>
     * </p>
     * returns null.
     *
     * @param clazz
     *           type to be scanned
     * @param baseClass
     *           type which parameter is to be found. Ff a class implements multiple generic interfaces, or deeper class
     *           hesitancy is involved, then parameter is used to implicitly specify whose generic parameter is to be
     *           extracted
     * @param position
     *           generic argument's position
     * @return generic argument's type or <code>null</code> if argument cannot be resolved
     */
    public static <P, C extends P, K> Class<K> extractGenericParameterType(final Class<C> clazz, final Class<P> baseClass,
                    final int position)
    {
        final Class<?>[] typeArguments = GenericTypeResolver.resolveTypeArguments(clazz, baseClass);
        return typeArguments != null ? (Class<K>)typeArguments[position] : null;
    }
}
