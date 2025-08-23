/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.util;

import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import java.util.Set;

/**
 * Performs operations of reference model
 */
public interface ReferenceModelProperties
{
    /**
     * Initializes object with appropriate widget model.
     * This method needs to be called before using any method from this interface.
     *
     * @param widgetModel
     */
    void initialize(final WidgetModel widgetModel);


    /**
     * Gets referenced model properties.
     *
     * @param referencedObject reference object
     * @return properties
     */
    Set<String> getReferencedModelProperties(final Object referencedObject);


    /**
     * Handles referenced object deleted event.
     *
     * @param deletedObject object to be deleted
     */
    void handleReferencedObjectDeletedEvent(final Object deletedObject);


    /**
     * Updates reference properties.
     *
     * @param referenceProperties properties
     * @param updatedObject updated object
     * @param exceptionHandler exception handler, which allows to handle ObjectNotFound exception
     */
    void updateReferenceProperties(final Set<String> referenceProperties, final Object updatedObject,
                    final ObjectNotFoundExceptionHandler exceptionHandler);


    /**
     * Collects referenced objects.
     *
     * @param genericAttribute attribute
     * @param valueObject value object
     * @return referenced objects
     */
    Set<Object> collectReferencedObjects(final DataAttribute genericAttribute, final Object valueObject);


    /**
     * Updates referenced objects.
     *
     * @param widgetModel widget model
     * @param referencedModelProperty referenced model property
     * @param referencedObjects referenced objects
     */
    void updateReferencedObjects(final WidgetModel widgetModel, final String referencedModelProperty,
                    final Set<Object> referencedObjects);


    /**
     * Checks if given objects are the same. If given object is a collection, at least one element from this collection
     * needs to be equal to current object.
     *
     * @param objectOfComparison
     * @param currentObject object or collection of objects to be compared with {@code objectOfComparison}
     * @return boolean true if objects are the same
     */
    boolean isEdited(final Object objectOfComparison, final Object currentObject);


    /**
     * Checks if attribute is a reference editor
     * @param attribute data attribute
     * @return boolean if attribute is a reference editor
     */
    boolean isReferenceEditor(final DataAttribute attribute);
}
