/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.editorarea.renderer;

import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;

/**
 * The interface provides methods to get {@link EditAvailabilityProvider}.
 *
 */
public interface EditAvailabilityProviderFactory
{
    /**
     * Returns {@link EditAvailabilityProvider} for the specified attribute at the given instance.
     *
     * @param attribute
     *           attribute of object
     * @param instance
     *           object
     * @return {@link EditAvailabilityProvider} for the specified instance attribute
     */
    EditAvailabilityProvider getProvider(DataAttribute attribute, Object instance);
}
