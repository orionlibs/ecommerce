/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.compare.impl;

import com.hybris.cockpitng.compare.ItemComparisonFacade;
import com.hybris.cockpitng.compare.ObjectAttributeComparator;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default comparator for {@link ItemComparisonFacade}
 */
public class DefaultObjectAttributeComparator implements ObjectAttributeComparator<Object>
{
    private ObjectAttributeComparator<Map> mapComparator;


    /**
     * Checks, if two values are considered equal.
     *
     * @param object1
     *           first object value
     * @param object2
     *           second object value
     * @return true, if equal, false otherwise
     */
    @Override
    public boolean isEqual(final Object object1, final Object object2)
    {
        if(object1 instanceof Collection && object2 instanceof Collection)
        {
            return compareCollections((Collection)object1, (Collection)object2);
        }
        if(object1 instanceof Map && object2 instanceof Map)
        {
            return compareMapObjects((Map)object1, (Map)object2);
        }
        else
        {
            return compareSingleObjects(object1, object2);
        }
    }


    /**
     * Compare Map type objects.
     *
     * @param map1
     *           first map object
     * @param map2
     *           second map object
     * @return true, if equal, false otherwise
     */
    protected boolean compareMapObjects(final Map map1, final Map map2)
    {
        return getMapComparator().isEqual(map1, map2);
    }


    /**
     * Compares single (non-collection) values.
     *
     * @param object1
     *           first object value
     * @param object2
     *           second object value
     * @return true, if equal, false otherwise
     */
    protected boolean compareSingleObjects(final Object object1, final Object object2)
    {
        return Objects.equals(object1, object2);
    }


    /**
     * Compares collections
     *
     * @param coll1
     *           the first collection
     * @param coll2
     *           the second collection
     * @return true, if equal, false otherwise
     */
    protected boolean compareCollections(final Collection coll1, final Collection coll2)
    {
        final boolean ret;
        if(coll1.size() == coll2.size())
        {
            if(coll1 instanceof List && coll2 instanceof List)
            {
                ret = compareListObjects((List)coll1, (List)coll2);
            }
            else
            {
                ret = coll1.containsAll(coll2);
            }
        }
        else
        {
            ret = false;
        }
        return ret;
    }


    private boolean compareListObjects(final List list1, final List list2)
    {
        boolean listsEqual = true;
        for(int i = 0; i < list1.size(); i++)
        {
            final Object coll1Object = list1.get(i);
            final Object coll2Object = list2.get(i);
            if(!isEqual(coll1Object, coll2Object))
            {
                listsEqual = false;
                break;
            }
        }
        return listsEqual;
    }


    protected ObjectAttributeComparator<Map> getMapComparator()
    {
        return mapComparator;
    }


    @Required
    public void setMapComparator(final ObjectAttributeComparator<Map> mapComparator)
    {
        this.mapComparator = mapComparator;
    }
}
