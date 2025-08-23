/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.impl;

import com.google.common.collect.Sets;
import com.hybris.cockpitng.core.config.MergeMode;
import com.hybris.cockpitng.core.config.annotations.Mergeable;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Utilities for merging objects based on {@link Mergeable} annotations.
 */
public class MergeUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(MergeUtils.class);
    private static final Collection<Class<?>> PRIMITIVES = Collections.unmodifiableSet(Sets.newHashSet(Boolean.class, Byte.class,
                    Character.class, Short.class, Integer.class, Long.class, Double.class, Float.class, Void.class, CharSequence.class,
                    Number.class, Date.class, URI.class, URL.class, Locale.class, Class.class));
    private Set<String> propertyBlacklist;


    private static void mergeArrayValue(final Object parent, final Object updateValue, final PropertyDescriptor property)
    {
        setPropertyValue(parent, property, updateValue);
    }


    private static void replaceCollectionValue(final Collection collection, final Object oldValue, final Object newValue)
    {
        if(collection instanceof List)
        {
            final int index = ((List)collection).indexOf(oldValue);
            ((List)collection).remove(index);
            ((List)collection).add(index, newValue);
        }
        else
        {
            collection.remove(oldValue);
            collection.add(newValue);
        }
    }


    private static void removeCollectionValue(final Collection<?> collection, final Object value)
    {
        collection.remove(value);
    }


    private static boolean objectsMatch(final Object parentValue, final Object updateValue, final Mergeable mergeInfo)
    {
        final Set<String> keys = getMergeKeySet(mergeInfo);
        if(!keys.isEmpty())
        {
            return objectsMatch(parentValue, updateValue, keys);
        }
        else
        {
            return true;
        }
    }


    private static boolean objectsMatch(final Object parentValue, final Object updateValue, final Set<String> keys)
    {
        Boolean hasValue = false;
        for(final String key : keys)
        {
            if(getPropertyValue(parentValue, key) != null)
            {
                hasValue = true;
                break;
            }
        }
        for(final String key : keys)
        {
            final Object value1 = getPropertyValue(parentValue, key);
            final Object value2 = getPropertyValue(updateValue, key);
            if(hasValue)
            {
                // Doesn't compare difference when property value in parentValue is null and other properties has value
                if(value1 != null && !Objects.equals(value1, value2))
                {
                    return false;
                }
            }
            else if(!Objects.equals(value1, value2))
            {
                return false;
            }
        }
        return true;
    }


    private static Set<String> getMergeKeySet(final Mergeable mergeInfo)
    {
        final String[] keyArray = mergeInfo.key();
        if(keyArray != null && keyArray.length > 0)
        {
            final Set<String> result = new HashSet<>(keyArray.length);
            Collections.addAll(result, keyArray);
            return result;
        }
        else
        {
            return Collections.emptySet();
        }
    }


    private static boolean isSingleProperty(final Class<?> clazz)
    {
        return (!clazz.isArray() && !Collection.class.isAssignableFrom(clazz));
    }


    private static Mergeable getMergeInfo(final Object object, final PropertyDescriptor property)
    {
        Method method = property.getWriteMethod();
        if(method == null)
        {
            method = property.getReadMethod();
        }
        Mergeable result = method.getAnnotation(Mergeable.class);
        if(result == null)
        {
            final Field field = getField(object, property.getName());
            if(field != null)
            {
                result = field.getAnnotation(Mergeable.class);
            }
        }
        return result;
    }


    private static Field getField(final Object object, final String property)
    {
        Field field = null;
        Class<?> clazz = object.getClass();
        while(clazz != null)
        {
            try
            {
                field = clazz.getDeclaredField(property);
                clazz = null;
            }
            catch(final Exception e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Error while getting declared field", e);
                }
                clazz = clazz.getSuperclass();
            }
        }
        return field;
    }


    private static MergeMode getMergeMode(final Mergeable mergeable, final Object object)
    {
        final String mergeModeProp = mergeable.mergeModeProperty();
        Object mergeMode = null;
        if(StringUtils.isEmpty(mergeModeProp))
        {
            // try fallback
            try
            {
                mergeMode = PropertyUtils.getProperty(object, "mergeMode");
            }
            catch(final Exception e)
            {
                // OK
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Exception occured: ", e);
                }
            }
        }
        else
        {
            try
            {
                mergeMode = PropertyUtils.getProperty(object, mergeModeProp);
            }
            catch(final Exception e)
            {
                LOG.warn("unable to resolve value of merge-mode property '" + mergeModeProp + "'", e);
            }
        }
        return (mergeMode == null ? null : MergeMode.get(mergeMode.toString()));
    }


    private static Object getPropertyValue(final Object object, final PropertyDescriptor property)
    {
        return getPropertyValue(object, property.getName());
    }


    private static Object getPropertyValue(final Object object, final String property)
    {
        try
        {
            return PropertyUtils.getProperty(object, property);
        }
        catch(final Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error while reading property value", e);
            }
            final Field field = getField(object, property);
            if(field != null)
            {
                field.setAccessible(true);
                try
                {
                    return field.get(object);
                }
                catch(final Exception e1)
                {
                    LOG.error("unable to resolve value of merge-mode property '" + property + "'", e1);
                }
            }
        }
        return null;
    }


    private static void setPropertyValue(final Object object, final PropertyDescriptor property, final Object value)
    {
        try
        {
            PropertyUtils.setProperty(object, property.getName(), value);
        }
        catch(final Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error while setting property value", e);
            }
            final Field field = getField(object, property.getName());
            if(field != null)
            {
                field.setAccessible(true);
                try
                {
                    field.set(object, value);
                }
                catch(final Exception e1)
                {
                    LOG.error("error checking field " + field, e1);
                }
            }
        }
    }


    private static boolean isValueEmpty(final Object value)
    {
        if(value == null)
        {
            return true;
        }
        else if(value.getClass().isArray() && Array.getLength(value) == 0)
        {
            return true;
        }
        else
        {
            return (value instanceof Collection) && ((Collection)value).isEmpty();
        }
    }


    private static boolean isValueSet(final Object object, final PropertyDescriptor property)
    {
        final Field field = getField(object, property.getName());
        if(field != null)
        {
            field.setAccessible(true);
            try
            {
                final Object value = field.get(object);
                return (value != null);
            }
            catch(final Exception e)
            {
                LOG.error("error checking field " + field, e);
            }
        }
        final Object value = getPropertyValue(object, property);
        return (value != null);
    }


    private static boolean isSimpleProperty(final Class<?> clazz)
    {
        Assert.notNull(clazz, "Class must not be null");
        return isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
    }


    private static boolean isSimpleValueType(final Class<?> clazz)
    {
        return clazz.isPrimitive() || clazz.isEnum() || PRIMITIVES.contains(clazz);
    }


    /**
     * Merges two objects. Updates the first object by applying the second one to it.
     *
     * @param parent
     *           the parent object which is to be updated
     * @param update
     *           the object to be applied to the parent object
     */
    public void merge(final Object parent, final Object update)
    {
        final PropertyDescriptor[] props = PropertyUtils.getPropertyDescriptors(parent.getClass());
        for(final PropertyDescriptor p : props)
        {
            if(!getBlacklist().contains(p.getName()))
            {
                mergeProperty(parent, update, p);
            }
        }
    }


    private void mergeProperty(final Object parent, final Object update, final PropertyDescriptor property)
    {
        final Mergeable mergeInfo = getMergeInfo(parent, property);
        if(mergeInfo != null)
        {
            mergeProperty(parent, update, property, mergeInfo);
        }
        else
        {
            // no merge info - override the value but only if update value is
            // set!
            if(isValueSet(update, property))
            {
                final Object updateValue = getPropertyValue(update, property);
                setPropertyValue(parent, property, updateValue);
            }
        }
    }


    private void mergeProperty(final Object parent, final Object update, final PropertyDescriptor property,
                    final Mergeable mergeInfo)
    {
        final Object updateValue = getPropertyValue(update, property);
        if(!isValueEmpty(updateValue))
        {
            final Object parentValue = getPropertyValue(parent, property);
            mergeValue(parent, parentValue, updateValue, property, mergeInfo);
        }
        else if(!isValueEmpty(getPropertyValue(parent, property)))
        {
            mergeProperty(update, parent, property, mergeInfo);
        }
    }


    private void mergeValue(final Object parent, final Object parentValue, final Object updateValue,
                    final PropertyDescriptor property, final Mergeable mergeInfo)
    {
        final Class<?> propertyType = property.getPropertyType();
        if(isSingleProperty(propertyType))
        {
            if(isSimpleProperty(propertyType))
            {
                setPropertyValue(parent, property, updateValue);
            }
            else
            {
                final MergeMode mergeMode = getMergeMode(mergeInfo, updateValue);
                if(mergeMode == MergeMode.REMOVE)
                {
                    setPropertyValue(parent, property, null);
                }
                else if(mergeMode == MergeMode.REPLACE)
                {
                    setPropertyValue(parent, property, updateValue);
                }
                else if(!isValueEmpty(parentValue) && objectsMatch(parentValue, updateValue, mergeInfo))
                {
                    merge(parentValue, updateValue);
                }
                else
                {
                    setPropertyValue(parent, property, updateValue);
                }
            }
        }
        else
        {
            mergeMultipleValue(parent, parentValue, updateValue, property, mergeInfo);
        }
    }


    private void mergeMultipleValue(final Object parent, final Object parentValue, final Object updateValue,
                    final PropertyDescriptor property, final Mergeable mergeInfo)
    {
        final Class<?> propertyType = property.getPropertyType();
        if(propertyType.isArray())
        {
            mergeArrayValue(parent, updateValue, property);
        }
        else if(Collection.class.isAssignableFrom(propertyType))
        {
            mergeCollectionValue(parent, property, parentValue, updateValue, mergeInfo);
        }
        else
        {
            LOG.error("Unknown value type to merge: {}, {}", propertyType, property);
        }
    }


    private void mergeCollectionValue(final Object parent, final PropertyDescriptor property, final Object parentValue,
                    final Object updateValue, final Mergeable mergeInfo)
    {
        if(parentValue == null)
        {
            ((Collection)updateValue).forEach(object -> {
                if(getMergeMode(mergeInfo, object) == MergeMode.REMOVE)
                {
                    ((Collection)updateValue).remove(object);
                }
            });
            setPropertyValue(parent, property, updateValue);
        }
        else
        {
            mergeCollectionValue(parentValue, updateValue, mergeInfo);
        }
    }


    private void mergeCollectionValue(final Object parentValue, final Object updateValue, final Mergeable mergeInfo)
    {
        for(final Object object : new ArrayList<>((Collection)updateValue))
        {
            Object matchingObject = null;
            for(final Object parentObject : (Collection)parentValue)
            {
                if(objectsMatch(parentObject, object, mergeInfo))
                {
                    matchingObject = parentObject;
                    break;
                }
            }
            final MergeMode mergeMode = getMergeMode(mergeInfo, object);
            if(matchingObject != null)
            {
                if(mergeMode == MergeMode.REMOVE)
                {
                    removeCollectionValue((Collection)parentValue, matchingObject);
                }
                else if(mergeMode == MergeMode.REPLACE || isSimpleProperty(object.getClass()))
                {
                    replaceCollectionValue((Collection)parentValue, matchingObject, object);
                }
                else
                {
                    merge(matchingObject, object);
                }
            }
            else if(mergeMode != MergeMode.REMOVE)
            {
                ((Collection)parentValue).add(object);
            }
        }
    }


    public Set<String> getBlacklist()
    {
        if(propertyBlacklist == null)
        {
            propertyBlacklist = new HashSet<>();
            propertyBlacklist.add("class");
        }
        return propertyBlacklist;
    }


    public void setBlacklist(final Set<String> blacklist)
    {
        this.propertyBlacklist = blacklist;
    }
}
