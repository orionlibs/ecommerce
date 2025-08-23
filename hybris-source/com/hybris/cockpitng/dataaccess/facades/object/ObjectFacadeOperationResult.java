/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.object;

import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectAccessException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ObjectFacadeOperationResult<T>
{
    private final List<T> successfulObjects = new ArrayList<>();
    private final Map<T, ObjectAccessException> failedObjects = new HashMap<>();


    public boolean hasError()
    {
        return !failedObjects.isEmpty();
    }


    public List<T> getSuccessfulObjects()
    {
        return Collections.unmodifiableList(successfulObjects);
    }


    public void addSuccessfulObject(final T object)
    {
        successfulObjects.add(object);
    }


    public Set<T> getFailedObjects()
    {
        return Collections.unmodifiableSet(failedObjects.keySet());
    }


    public ObjectAccessException getErrorForObject(final T object)
    {
        return failedObjects.get(object);
    }


    public void addFailedObject(final T object, final ObjectAccessException cause)
    {
        failedObjects.put(object, cause);
    }


    public int countSuccessfulObjects()
    {
        return successfulObjects.size();
    }


    public int countFailureObjects()
    {
        return failedObjects.size();
    }
}
