/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.compare;

import com.hybris.cockpitng.compare.model.ComparisonResult;
import com.hybris.cockpitng.compare.model.GroupDescriptor;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Created based on ObjectCompareService.
 */
public interface ItemComparisonFacade
{
    /**
     * Performs comparison and builds result structure.
     *
     * @param referenceObject
     *           object for which comparison is performed
     * @param compareObjects
     *           objects to compare with <code>referenceObject</code>
     * @param groupDescriptors
     *           collection of the group descriptors to compare between <code>referenceObject</code> and
     *           <code>compareObject</code>
     * @return structure that contains differences between <code>referenceObject</code> and <code>compareObjects</code> for
     *         the <code>groupDescriptors</code>
     */
    <T> Optional<ComparisonResult> getCompareViewResult(final T referenceObject, final Collection<T> compareObjects,
                    final Collection<GroupDescriptor> groupDescriptors);


    default boolean isSameItem(final Object object1, final Object object2)
    {
        return object1 == object2;
    }


    default boolean isEqualItem(final Object object1, final Object object2)
    {
        return object1 != null && object1.equals(object2);
    }


    /**
     * Performs comparison and builds result structure.
     *
     * @param referenceObject
     *           object for which comparison is performed
     * @param compareObjects
     *           objects to compare with <code>referenceObject</code>
     * @param groupDescriptors
     *           collection of the group descriptors to compare between <code>referenceObject</code> and
     *           <code>compareObject</code>
     * @param objectsReferenceSupplier
     *           reference attributes of <code>referenceObject</code> and <code>compareObject</code>
     * @return structure that contains differences between <code>referenceObject</code> and <code>compareObjects</code> for
     *         the <code>groupDescriptors</code>
     */
    default <T> Optional<ComparisonResult> getCompareViewResult(final T referenceObject, final Collection<T> compareObjects,
                    final Collection<GroupDescriptor> groupDescriptors, final Supplier objectsReferenceSupplier)
    {
        return getCompareViewResult(referenceObject, compareObjects, groupDescriptors);
    }
}
