/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.impl;

import com.hybris.cockpitng.core.model.WidgetModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public final class WidgetModelUtils
{
    private WidgetModelUtils()
    {
        //NOOP
    }


    /**
     * Clears all references of <code>objectToDelete</code> from object that is stored in the <code>widgetModel</code>
     * using given <code>property</code>.
     *
     * @param widgetModel
     * @param objectToDelete
     * @param property
     */
    public static void clearObjectFromWidgetModel(final WidgetModel widgetModel, final Object objectToDelete,
                    final String property)
    {
        final Object previousModelValue = widgetModel.getValue(property, Object.class);
        final Object newModelValue = clearDeletedObject(previousModelValue, objectToDelete);
        widgetModel.setValue(property, newModelValue);
    }


    /**
     * Processes the previousValue object to remove its all reference(s) of deleted object. Handles embedded Lists and
     * Maps.
     *
     * @param previousValue
     * @param deletedObject
     * @return new value to be set in the model.
     */
    protected static Object clearDeletedObject(final Object previousValue, final Object deletedObject)
    {
        if(deletedObject == null)
        {
            return previousValue;
        }
        if(deletedObject instanceof Collection)
        {
            final Stream stream = ((Collection)deletedObject).stream();
            final Optional objectEqualToPreviousValue = stream.filter(object -> object.equals(previousValue)).findFirst();
            if(objectEqualToPreviousValue.isPresent())
            {
                return null;
            }
        }
        else if(deletedObject.equals(previousValue))
        {
            return null;
        }
        Object newModelValue = previousValue;
        if(previousValue instanceof Collection)
        {
            final Collection previousCollectionValue = (Collection)previousValue;
            if(previousCollectionValue.contains(deletedObject))
            {
                newModelValue = new ArrayList<Object>(previousCollectionValue);
                while(((Collection)newModelValue).contains(deletedObject))
                {
                    ((Collection)newModelValue).remove(deletedObject);
                }
            }
            else
            {
                newModelValue = new ArrayList<Object>(previousCollectionValue.size());
                for(Object object : previousCollectionValue)
                {
                    object = clearDeletedObject(object, deletedObject);
                    if(object != null)
                    {
                        ((Collection)newModelValue).add(object);
                    }
                }
            }
        }
        else if(previousValue instanceof Map)
        {
            final Map<Object, Object> previousMapValue = (Map)previousValue;
            if(previousMapValue.containsValue(deletedObject))
            {
                newModelValue = new HashMap<Object, Object>(previousMapValue);
                for(final Map.Entry<Object, Object> entry : previousMapValue.entrySet())
                {
                    if(deletedObject.equals(entry.getValue()))
                    {
                        ((Map<Object, Object>)newModelValue).remove(entry.getKey());
                    }
                }
            }
            else
            {
                newModelValue = new HashMap(previousMapValue.size());
                for(final Map.Entry<Object, Object> entry : previousMapValue.entrySet())
                {
                    ((Map)newModelValue).put(entry.getKey(), clearDeletedObject(entry.getValue(), deletedObject));
                }
            }
        }
        return newModelValue;
    }


    /**
     *
     * Returns a set of single object references that are present in the <code>targetObject</code>. Unwinds maps and
     * collections to return a one level set with objects.
     *
     * @param targetObject
     * @return
     */
    public static Set<Object> collectSingleObjectReferences(final Object targetObject)
    {
        final Set<Object> singleReferences = new HashSet();
        if(targetObject instanceof Map)
        {
            singleReferences.addAll(collectSingleObjectReferences(((Map)targetObject).values()));
        }
        else if(targetObject instanceof Collection)
        {
            for(final Object singleValueObject : (Collection)targetObject)
            {
                singleReferences.addAll(collectSingleObjectReferences(singleValueObject));
            }
        }
        else
        {
            singleReferences.add(targetObject);
        }
        return singleReferences;
    }
}
