/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.adapters.impl;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.config.impl.jaxb.adapters.PositionedSort;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

/**
 * Default {@code PositionedSort} implementation Assumptions:
 * <ul>
 *
 * <li>GIVEN</li>
 * <li>ObjectA and ObjectB does not have specified position, ObjectA appears before ObjectB in input list</li>
 * <li>THEN </li>
 * <li>ObjectA appears before ObjectB in output list</li>
 *
 * <li>GIVEN </li>
 * <li>ObjectA.position &gt; ObjectB.position </li>
 * <li>THEN </li>
 * <li>ObjectA appears before ObjectB in output list</li>
 *
 * <li>GIVEN</li>
 * <li>ObjectA.position == ObjectB.position and ObjectB appears after ObjectA in input list</li>
 * <li>THEN</li>
 * <li>ObjectB should be displayed before ObjectA</li>
 *
 * </ul>
 */
public class DefaultPositionedSort<T extends Positioned> implements PositionedSort<T>
{
    @Override
    public void sort(final List<T> allValues)
    {
        if(CollectionUtils.isEmpty(allValues))
        {
            return;
        }
        final List<T> notIndexed = Lists.newLinkedList();
        final List<T> indexed = Lists.newLinkedList();
        fillListsToMerge(allValues, notIndexed, indexed);
        allValues.clear();
        allValues.addAll(notIndexed);
        mergeIndexedIntoResult(allValues, indexed);
    }


    private void fillListsToMerge(final List<T> elements, final List<T> notIndexed, final List<T> orderd)
    {
        for(final T element : elements)
        {
            final BigInteger position = element.getPosition();
            if(position == null)
            {
                notIndexed.add(element);
            }
            else
            {
                if(position.signum() < 0)
                {
                    throw new IllegalArgumentException("Negative values not supported");
                }
                orderd.add(element);
            }
        }
        Collections.sort(orderd, new Comparator<Positioned>()
        {
            @Override
            public int compare(final Positioned a, final Positioned b)
            {
                return a.getPosition().compareTo(b.getPosition());
            }
        });
    }


    private void mergeIndexedIntoResult(final List<T> list, final List<T> ordered)
    {
        int lastInsert = 0;
        for(final T element : ordered)
        {
            final long expectedPossition = element.getPosition().longValue();
            int insertAt = list.size();
            for(int i = lastInsert; i < list.size(); i++)
            {
                final long listPosition = list.get(i).getPosition() != null ? list.get(i).getPosition().longValue() : -1;
                if(listPosition >= expectedPossition || (i >= expectedPossition && listPosition == -1))
                {
                    insertAt = i;
                    break;
                }
            }
            lastInsert = insertAt;
            list.add(insertAt, element);
        }
    }
}
