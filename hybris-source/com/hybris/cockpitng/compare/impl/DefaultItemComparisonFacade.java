/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.compare.impl;

import com.google.common.collect.Sets;
import com.hybris.cockpitng.compare.ItemComparisonFacade;
import com.hybris.cockpitng.compare.ObjectAttributeComparator;
import com.hybris.cockpitng.compare.model.CompareAttributeDescriptor;
import com.hybris.cockpitng.compare.model.CompareLocalizedAttributeDescriptor;
import com.hybris.cockpitng.compare.model.ComparisonResult;
import com.hybris.cockpitng.compare.model.GroupDescriptor;
import com.hybris.cockpitng.compare.model.ObjectAttributesValueContainer;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.type.ObjectValueService;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation for {@link ItemComparisonFacade}
 */
public class DefaultItemComparisonFacade implements ItemComparisonFacade
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultItemComparisonFacade.class);
    private ObjectValueService objectValueService;
    private ObjectAttributeComparator objectAttributeComparator;
    private ObjectFacade objectFacade;


    /**
     * Builds the compare view result structure.
     *
     * @param referenceObject
     *           object for which we build result.
     * @param compareObjects
     *           collection of objects to compare
     * @param groupDescriptors
     *           collection of group descriptors to compare with <code>referenceObject</code>
     * @return structure contains differences between <code>referenceObject</code> and <code>compareObjects</code> for the
     *         <code>groupDescriptors</code>
     */
    @Override
    public <T> Optional<ComparisonResult> getCompareViewResult(final T referenceObject, final Collection<T> compareObjects,
                    final Collection<GroupDescriptor> groupDescriptors)
    {
        final Set<CompareAttributeDescriptor> attributeDescriptors = groupDescriptors.stream()
                        .map(GroupDescriptor::getCompareAttributes).flatMap(List::stream).collect(Collectors.toSet());
        final List<Object> objectsToCompare = compareObjects.stream().distinct().collect(Collectors.toList());
        objectsToCompare.remove(referenceObject);
        final ObjectAttributesValueContainer refObjectAttrsValueContainer = createObjectAttributesValueContainer(referenceObject,
                        groupDescriptors);
        final List<ObjectAttributesValueContainer> objectAttributesValueContainers = prepareObjectAttributesValueContainerList(
                        objectsToCompare, groupDescriptors);
        if(CollectionUtils.isNotEmpty(objectAttributesValueContainers))
        {
            final ComparisonResult comparisonResult = computeCompareViewResult(refObjectAttrsValueContainer,
                            objectAttributesValueContainers, attributeDescriptors, new HashSet<>(groupDescriptors));
            return Optional.of(comparisonResult);
        }
        return Optional.empty();
    }


    protected List<ObjectAttributesValueContainer> prepareObjectAttributesValueContainerList(final Collection allObjects,
                    final Collection<GroupDescriptor> groupDescriptors)
    {
        return (List<ObjectAttributesValueContainer>)allObjects.stream()
                        .map(obj -> createObjectAttributesValueContainer(obj, groupDescriptors)).collect(Collectors.toList());
    }


    protected ObjectAttributesValueContainer createObjectAttributesValueContainer(final Object object,
                    final Collection<GroupDescriptor> groupDescriptors)
    {
        final Object objectId = getObjectFacade().getObjectId(object);
        final ObjectAttributesValueContainer valueContainer = new ObjectAttributesValueContainer(objectId);
        groupDescriptors.stream().map(GroupDescriptor::getCompareAttributes).flatMap(Collection::stream)
                        .forEach(attributeDescriptor -> updateObjectAttributeValue(valueContainer, object, attributeDescriptor));
        return valueContainer;
    }


    protected void updateObjectAttributeValue(final ObjectAttributesValueContainer valueContainer, final Object item,
                    final CompareAttributeDescriptor attributeDescriptor)
    {
        Object value = getObjectValueService().getValue(attributeDescriptor.getQualifier(), item);
        if(value instanceof Map && attributeDescriptor instanceof CompareLocalizedAttributeDescriptor)
        {
            final Map<Locale, Object> localizedValues = (Map)value;
            value = localizedValues.get(((CompareLocalizedAttributeDescriptor)attributeDescriptor).getLocale());
        }
        valueContainer.getAttributeValues().put(attributeDescriptor, value);
    }


    protected ComparisonResult computeCompareViewResult(final ObjectAttributesValueContainer referenceContainer,
                    final List<ObjectAttributesValueContainer> compareContainers,
                    final Collection<CompareAttributeDescriptor> compareAttributeDescriptors, final Set<GroupDescriptor> groupDescriptors)
    {
        final Map<ObjectAttributesValueContainer, Set<CompareAttributeDescriptor>> differences = getDifferencesForAttributes(
                        referenceContainer, compareContainers, compareAttributeDescriptors);
        return new ComparisonResult(referenceContainer.getObjectId(), differences, groupDescriptors);
    }


    protected Map<ObjectAttributesValueContainer, Set<CompareAttributeDescriptor>> getDifferencesForAttributes(
                    final ObjectAttributesValueContainer referenceContainer, final List<ObjectAttributesValueContainer> compareContainers,
                    final Collection<CompareAttributeDescriptor> compareAttributeDescriptors)
    {
        final Map<ObjectAttributesValueContainer, Set<CompareAttributeDescriptor>> differences = new HashedMap();
        compareAttributeDescriptors.forEach(compareAttrDescriptor -> {
            final Object referenceAttrValue = referenceContainer.getAttributeValues().get(compareAttrDescriptor);
            compareReferenceValueWithCompareObjects(referenceAttrValue, compareContainers, compareAttrDescriptor, differences);
        });
        return differences;
    }


    private void compareReferenceValueWithCompareObjects(final Object referenceAttrValue,
                    final List<ObjectAttributesValueContainer> compareObjectContainers,
                    final CompareAttributeDescriptor compareAttrDescriptor,
                    final Map<ObjectAttributesValueContainer, Set<CompareAttributeDescriptor>> differences)
    {
        compareObjectContainers.forEach(compareObjectContainer -> compareReferenceValueWithCompareObject(referenceAttrValue,
                        compareObjectContainer, compareAttrDescriptor, differences));
    }


    protected void compareReferenceValueWithCompareObject(final Object referenceAttrValue,
                    final ObjectAttributesValueContainer compareObjectContainer, final CompareAttributeDescriptor compareAttrDescriptor,
                    final Map<ObjectAttributesValueContainer, Set<CompareAttributeDescriptor>> differences)
    {
        final Object compareAttrValue = compareObjectContainer.getAttributeValues().get(compareAttrDescriptor);
        if(!getObjectAttributeComparator().isEqual(referenceAttrValue, compareAttrValue))
        {
            differences.computeIfAbsent(compareObjectContainer, key -> Sets.newHashSet()).add(compareAttrDescriptor);
        }
    }


    protected Optional<Object> getObjectById(final Object id)
    {
        if(id == null)
        {
            return Optional.empty();
        }
        try
        {
            return Optional.ofNullable(getObjectFacade().load(String.valueOf(id)));
        }
        catch(final ObjectNotFoundException e)
        {
            LOGGER.error("Object not found by id " + id, e);
            return Optional.empty();
        }
    }


    public ObjectValueService getObjectValueService()
    {
        return objectValueService;
    }


    @Required
    public void setObjectValueService(final ObjectValueService objectValueService)
    {
        this.objectValueService = objectValueService;
    }


    public ObjectAttributeComparator getObjectAttributeComparator()
    {
        return objectAttributeComparator;
    }


    @Required
    public void setObjectAttributeComparator(final ObjectAttributeComparator objectAttributeComparator)
    {
        this.objectAttributeComparator = objectAttributeComparator;
    }


    protected ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    @Required
    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }
}
