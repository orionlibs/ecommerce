/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.model;

import com.hybris.cockpitng.collections.impl.IdentityArrayList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultComparisonState<I> implements ComparisonState<I>
{
    private final I reference;
    private final List<I> allObjects;
    private final List<I> comparedObjects;
    private final List<Object> comparedObjectIds;
    private Status status;


    public DefaultComparisonState(final I reference, final List<I> allObjects)
    {
        this.reference = reference;
        this.allObjects = allObjects;
        this.comparedObjects = new IdentityArrayList<>();
        this.status = Status.PREPARED;
        this.comparedObjectIds = new ArrayList<>();
    }


    @Override
    public I getReference()
    {
        return reference;
    }


    /**
     * @deprecated since 1811. Please use {@link #setObjectCompared(Object, Object)}
     */
    @Deprecated(since = "1811", forRemoval = true)
    public void setObjectCompared(final I object)
    {
        setObjectCompared(object, null);
    }


    public void setObjectCompared(final Object id, final I object)
    {
        if(!getComparedObjects().contains(object))
        {
            getComparedObjectsMutable().remove(object);
            getComparedObjectsMutable().add(object);
        }
        setObjectIdCompared(id);
    }


    protected void setObjectIdCompared(final Object id)
    {
        if(!getComparedObjectIds().contains(id))
        {
            getComparedObjectIdsMutable().add(id);
        }
    }


    /**
     * @deprecated since 1811. Please use {@link #removeObjectFromCompared(Object)}
     */
    @Deprecated(since = "1811", forRemoval = true)
    public void removeObjectFromCompared(final I object)
    {
        removeObjectFromCompared(object, null);
    }


    public void removeObjectFromCompared(final Object id, final I object)
    {
        getComparedObjectsMutable().remove(object);
        getComparedObjectIdsMutable().remove(id);
    }


    @Override
    public List<I> getComparedObjects()
    {
        return Collections.unmodifiableList(getComparedObjectsMutable());
    }


    @Override
    public List<Object> getComparedObjectIds()
    {
        return Collections.unmodifiableList(getComparedObjectIdsMutable());
    }


    protected List<Object> getComparedObjectIdsMutable()
    {
        return comparedObjectIds;
    }


    protected List<I> getComparedObjectsMutable()
    {
        return comparedObjects;
    }


    @Override
    public List<I> getAllObjects()
    {
        return Collections.unmodifiableList(getAllObjectsMutable());
    }


    protected List<I> getAllObjectsMutable()
    {
        return allObjects;
    }


    @Override
    public Status getStatus()
    {
        return status;
    }


    public void setStatus(final Status status)
    {
        this.status = status;
    }
}
