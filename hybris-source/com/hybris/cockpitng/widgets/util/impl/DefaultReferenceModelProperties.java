/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.util.impl;

import com.hybris.cockpitng.core.impl.WidgetModelUtils;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.util.Range;
import com.hybris.cockpitng.widgets.util.ObjectNotFoundExceptionHandler;
import com.hybris.cockpitng.widgets.util.ReferenceModelProperties;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import org.apache.commons.collections.CollectionUtils;

public class DefaultReferenceModelProperties implements ReferenceModelProperties
{
    public static final String MODEL_ALL_REFERENCED_OBJECTS = "referencedObjects";
    public static final String MODEL_VALUE_CHANGED = "valueChanged";
    private WidgetModel widgetModel;


    @Override
    public void initialize(final WidgetModel widgetModel)
    {
        this.widgetModel = widgetModel;
    }


    @Override
    public void handleReferencedObjectDeletedEvent(final Object object)
    {
        final Set<String> properties = getReferencedModelProperties(object);
        if(CollectionUtils.isEmpty(properties))
        {
            return;
        }
        deleteObjectFromModel(object, properties);
    }


    public void deleteObjectFromModel(final Object object, final Set<String> properties)
    {
        properties.forEach(property -> WidgetModelUtils.clearObjectFromWidgetModel(widgetModel, object, property));
    }


    @Override
    public Set<String> getReferencedModelProperties(final Object referencedObject)
    {
        final Map<Object, Set<String>> allReferencedObjects = widgetModel.getValue(MODEL_ALL_REFERENCED_OBJECTS, Map.class);
        if(referencedObject == null || allReferencedObjects == null)
        {
            return Collections.emptySet();
        }
        if(referencedObject instanceof Collection)
        {
            return getReferencedModelProperties((Collection)referencedObject, allReferencedObjects);
        }
        else if(allReferencedObjects.containsKey(referencedObject))
        {
            return allReferencedObjects.get(referencedObject);
        }
        return Collections.emptySet();
    }


    @Override
    public void updateReferenceProperties(final Set<String> referenceProperties, final Object updatedObject,
                    final ObjectNotFoundExceptionHandler exceptionHandler)
    {
        final Map<String, Object> newValuesByProperties = getUpdatedPropertiesValues(referenceProperties, widgetModel,
                        updatedObject, exceptionHandler);
        if(newValuesByProperties.isEmpty())
        {
            return;
        }
        final Boolean modelModified = widgetModel.getValue(MODEL_VALUE_CHANGED, Boolean.class);
        newValuesByProperties.forEach((key, value) -> widgetModel.setValue(key, value));
        widgetModel.setValue(MODEL_VALUE_CHANGED, modelModified);
    }


    @Override
    public Set<Object> collectReferencedObjects(final DataAttribute genericAttribute, final Object valueObject)
    {
        Set<Object> referencedObjects = new HashSet<>();
        if(isSingle(genericAttribute))
        {
            referencedObjects.add(valueObject);
        }
        else if(isRange(genericAttribute, valueObject))
        {
            final Range rangeValue = (Range)valueObject;
            referencedObjects.add(rangeValue.getStart());
            referencedObjects.add(rangeValue.getEnd());
        }
        else
        {
            referencedObjects = WidgetModelUtils.collectSingleObjectReferences(valueObject);
        }
        return referencedObjects;
    }


    @Override
    public void updateReferencedObjects(final WidgetModel widgetModel, final String referencedModelProperty,
                    final Set<Object> referencedObjects)
    {
        final Map<Object, Set<String>> allReferencedObjects = getObjectsReferencedInWidgetModel(widgetModel);
        for(final Object object : referencedObjects)
        {
            Set<String> objectModelProperties = allReferencedObjects.get(object);
            if(objectModelProperties == null)
            {
                objectModelProperties = new HashSet<>();
            }
            objectModelProperties.add(referencedModelProperty);
            allReferencedObjects.put(object, objectModelProperties);
        }
        widgetModel.setValue(MODEL_ALL_REFERENCED_OBJECTS, allReferencedObjects);
    }


    @Override
    public boolean isEdited(final Object objectOfComparison, final Object currentObject)
    {
        if(objectOfComparison instanceof Collection)
        {
            final Collection items = (Collection)objectOfComparison;
            if(items.isEmpty())
            {
                return false;
            }
            final Predicate objectsAreTheSame = object -> Objects.equals(object, currentObject);
            final Optional theSameObjectAsCurrentObject = items.stream().filter(objectsAreTheSame).findAny();
            return theSameObjectAsCurrentObject.isPresent();
        }
        else
        {
            return Objects.equals(objectOfComparison, currentObject);
        }
    }


    @Override
    public boolean isReferenceEditor(final DataAttribute attribute)
    {
        return attribute.getValueType() != null && !attribute.getValueType().isAtomic();
    }


    private Map<Object, Set<String>> getObjectsReferencedInWidgetModel(final WidgetModel widgetModel)
    {
        Map<Object, Set<String>> referencedObjectsMap = widgetModel.getValue(MODEL_ALL_REFERENCED_OBJECTS, Map.class);
        if(referencedObjectsMap == null)
        {
            referencedObjectsMap = new HashMap<>();
        }
        return referencedObjectsMap;
    }


    private boolean isRange(final DataAttribute genericAttribute, final Object valueObject)
    {
        return DataAttribute.AttributeType.RANGE.equals(genericAttribute.getAttributeType()) && valueObject instanceof Range;
    }


    private boolean isSingle(final DataAttribute genericAttribute)
    {
        return DataAttribute.AttributeType.SINGLE.equals(genericAttribute.getAttributeType());
    }


    private Map<String, Object> getUpdatedPropertiesValues(final Set<String> referenceProperties, final WidgetModel widgetModel,
                    final Object updatedObject, final ObjectNotFoundExceptionHandler exceptionHandler)
    {
        final Map<String, Object> valuesByProperties = new HashMap<>(referenceProperties.size());
        for(final String referenceProperty : referenceProperties)
        {
            try
            {
                final Object currentPropertyValue = widgetModel.getValue(referenceProperty, Object.class);
                valuesByProperties.put(referenceProperty, updatePropertyValue(currentPropertyValue, updatedObject));
            }
            catch(final ObjectNotFoundException e)
            {
                exceptionHandler.handleObjectNotFoundException(e, updatedObject);
            }
        }
        return valuesByProperties;
    }


    private Object updatePropertyValue(final Object currentPropertyValue, final Object updatedObject)
                    throws ObjectNotFoundException
    {
        if(currentPropertyValue instanceof Map)
        {
            final Map updatedMap = new HashMap<>(((Map)currentPropertyValue).size());
            for(final Object key : ((Map)currentPropertyValue).keySet())
            {
                updatedMap.put(key, updatePropertyValue(((Map)currentPropertyValue).get(key), updatedObject));
            }
            return updatedMap;
        }
        else if(currentPropertyValue instanceof Collection)
        {
            return updateCollectionPropertyValue((Collection)currentPropertyValue, updatedObject);
        }
        else
        {
            return updatedObject;
        }
    }


    private List<Object> updateCollectionPropertyValue(final Collection currentPropertyValue, final Object updatedObject)
                    throws ObjectNotFoundException
    {
        final List<Object> updatedCollection = new ArrayList<>(currentPropertyValue.size());
        for(final Object object : currentPropertyValue)
        {
            if(Objects.equals(object, updatedObject))
            {
                updatedCollection.add(updatedObject);
            }
            else
            {
                updatedCollection.add(object);
            }
        }
        return updatedCollection;
    }


    private Set<String> getReferencedModelProperties(final Collection referencedObject,
                    final Map<Object, Set<String>> allReferencedObjects)
    {
        // allReferencedObjects uses modifiable objects as key, which can result with hash problem inside HashMap
        // in that state HashMap::equals on keyValue returns true and HashMap::get returns null
        // ItemModel calculate HashCode diferently when model is saved and not saved. If you set unsaved ItemModel
        // as a key and save it after that, you can't find this with mathods like: contains, get
        for(final Object refObject : referencedObject)
        {
            for(final Map.Entry<Object, Set<String>> entry : allReferencedObjects.entrySet())
            {
                if(refObject.equals(entry.getKey()))
                {
                    return entry.getValue();
                }
            }
        }
        return Collections.emptySet();
    }
}
