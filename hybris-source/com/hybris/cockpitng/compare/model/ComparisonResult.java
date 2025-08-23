/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.compare.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * Class holds information about differences between reference object and particular compared objects
 * <code>referenceObjectId</code>.
 */
public class ComparisonResult
{
    private final Object referenceObjectId;
    protected final Set<Object> differentObjectsIds;
    protected final Set<String> differentGroups;
    protected final Map<CompareAttributeDescriptor, Collection<ObjectAttributesValueContainer>> differentObjectsForAttributes;
    protected final Map<String, List<CompareAttributeDescriptor>> groupAttributesByGroupNames;


    public ComparisonResult(final Object referenceObjectId,
                    final Map<ObjectAttributesValueContainer, Set<CompareAttributeDescriptor>> differences,
                    final Set<GroupDescriptor> groupDescriptors)
    {
        this.referenceObjectId = referenceObjectId;
        groupAttributesByGroupNames = groupDescriptors.stream()
                        .collect(Collectors.toMap(GroupDescriptor::getName, GroupDescriptor::getCompareAttributes, (c1, c2) -> c2));
        final Set<CompareAttributeDescriptor> differentAttributes = differences.values().stream().flatMap(Set::stream)
                        .collect(Collectors.toSet());
        differentGroups = groupDescriptors.stream()
                        .filter(group -> !Collections.disjoint(group.getCompareAttributes(), differentAttributes))
                        .map(GroupDescriptor::getName).collect(Collectors.toSet());
        differentObjectsForAttributes = differences.entrySet().stream()
                        .flatMap(entry -> entry.getValue().stream().map(attr -> new ImmutablePair<>(entry.getKey(), attr))).collect(Collectors
                                        .toMap(ImmutablePair::getValue, pair -> new HashSet<>(Collections.singleton(pair.getKey())), (a1, a2) -> {
                                            a1.addAll(a2);
                                            return a1;
                                        }));
        differentObjectsIds = differences.keySet().stream().map(ObjectAttributesValueContainer::getObjectId)
                        .collect(Collectors.toSet());
    }


    /**
     * @deprecated since 1811. Please use {@link #getObjectsIdWithDifferences()}
     */
    @Deprecated(since = "1811", forRemoval = true)
    public Collection<Object> getObjectsWithDifferences()
    {
        return getObjectsIdWithDifferences();
    }


    /**
     * @return collection of the different object ids for <code>referenceObjectId</code>, otherwise empty collection
     */
    public Collection<Object> getObjectsIdWithDifferences()
    {
        return Collections.unmodifiableCollection(differentObjectsIds);
    }


    public Set<CompareAttributeDescriptor> getAttributesWithDifferences()
    {
        return Collections.unmodifiableSet(differentObjectsForAttributes.keySet());
    }


    /**
     * @deprecated since 1811. Please use {@link #getObjectsIdWithDifferences()}
     */
    @Deprecated(since = "1811", forRemoval = true)
    public Collection<ObjectAttributesValueContainer> getObjectsWithDifferences(
                    final CompareAttributeDescriptor attributeDescriptor)
    {
        return getObjectsIdWithDifferences(attributeDescriptor);
    }


    /**
     * @param attributeDescriptor
     *           comparing attribute descriptor
     * @return collection of differences {@link ObjectAttributesValueContainer} for an <code>attributeDescriptor</code>,
     *         otherwise empty collection
     */
    public Collection<ObjectAttributesValueContainer> getObjectsIdWithDifferences(
                    final CompareAttributeDescriptor attributeDescriptor)
    {
        final Collection<ObjectAttributesValueContainer> containers = differentObjectsForAttributes.get(attributeDescriptor);
        return containers != null ? Collections.unmodifiableCollection(containers) : Collections.emptyList();
    }


    /**
     * @deprecated since 1811. Please use {@link #getReferenceObjectId()}
     */
    @Deprecated(since = "1811", forRemoval = true)
    public Object getReferenceObject()
    {
        return getReferenceObjectId();
    }


    public Object getReferenceObjectId()
    {
        return referenceObjectId;
    }


    /**
     * @return collection of difference group for <code>referenceObjectId</code>, otherwise empty collection
     */
    public Set<String> getGroupsWithDifferences()
    {
        return Collections.unmodifiableSet(differentGroups);
    }


    public synchronized boolean merge(final ComparisonResult addition, final Object comparedObjectId)
    {
        if(getReferenceObjectId().equals(addition.getReferenceObjectId()))
        {
            updateObjectsForAttributesDifferences(addition, comparedObjectId);
            updateGroupsDifferences(addition);
            updateDifferentObjects();
            return true;
        }
        return false;
    }


    protected void updateObjectsForAttributesDifferences(final ComparisonResult addition, final Object comparedObjectId)
    {
        final Set<CompareAttributeDescriptor> keysToRemove = new HashSet<>();
        differentObjectsForAttributes.forEach((key, value) -> {
            value.removeIf(objectContainer -> objectContainer.getObjectId().equals(comparedObjectId));
            if(value.isEmpty())
            {
                keysToRemove.add(key);
            }
        });
        keysToRemove.forEach(differentObjectsForAttributes::remove);
        addition.differentObjectsForAttributes
                        .forEach((key, value) -> differentObjectsForAttributes.merge(key, new HashSet<>(value), CollectionUtils::union));
    }


    protected void updateGroupsDifferences(final ComparisonResult addition)
    {
        final Predicate<String> isGroupRemovalNeeded = group -> groupAttributesByGroupNames
                        .getOrDefault(group, Collections.emptyList()).stream()
                        .map(groupName -> differentObjectsForAttributes.getOrDefault(groupName, Collections.emptyList()))
                        .filter(Objects::nonNull).allMatch(Collection::isEmpty);
        differentGroups.removeIf(isGroupRemovalNeeded);
        differentGroups.addAll(addition.getGroupsWithDifferences());
    }


    protected void updateDifferentObjects()
    {
        final Set<Object> newDifferentObjectIds = differentObjectsForAttributes.values().stream().flatMap(Collection::stream)
                        .map(ObjectAttributesValueContainer::getObjectId).collect(Collectors.toSet());
        differentObjectsIds.clear();
        differentObjectsIds.addAll(newDifferentObjectIds);
    }
}
