/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.model;

import java.util.Collections;
import java.util.List;

/**
 * Interface that indicates current state of comparison process. CompareView widget performs multiple comparison
 * operations in background. This interface allows to check what is the current state of this process.
 */
public interface ComparisonState<I>
{
    /**
     * Gets a reference object against which a comparison is being performed
     *
     * @return reference object
     */
    I getReference();


    /**
     * Gets current status of comparison - if it is not yest started, still in progress of done.
     *
     * @return comparison status
     */
    Status getStatus();


    /**
     * Gets a list of objects that has been compared against reference up until now.
     *
     * @return list of compared objects
     */
    List<I> getComparedObjects();


    /**
     * Gets a list of object ids that has been compared against reference up until now.
     *
     * @return list of compared objects
     */
    default List<Object> getComparedObjectIds()
    {
        return Collections.emptyList();
    }


    /**
     * Gets all objects that should be compared. Returned list contains those that are already compared and those that will
     * be compared in the future.
     *
     * @return list of all objects to be compared
     */
    List<I> getAllObjects();


    enum Status
    {
        PREPARED, RUNNING, FINISHED
    }
}
