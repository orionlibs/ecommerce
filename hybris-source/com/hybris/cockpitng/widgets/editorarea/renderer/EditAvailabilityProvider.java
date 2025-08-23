/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.editorarea.renderer;

import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;

/**
 * The interface defines methods to evaluate if an instance attribute is editable or not.
 *
 */
public interface EditAvailabilityProvider<T>
{
    /**
     * Returns true if property of the specified attribute at the given instance is allowed to be edited.
     *
     * @param attribute
     *           attribute of object
     * @param instance
     *           object which property is change
     * @return true if instance attribute is editable, false otherwise
     */
    boolean isAllowedToEdit(DataAttribute attribute, T instance);
}
