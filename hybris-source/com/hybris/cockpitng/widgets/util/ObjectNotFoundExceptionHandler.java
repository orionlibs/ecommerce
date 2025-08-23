/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.util;

import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;

@FunctionalInterface
public interface ObjectNotFoundExceptionHandler
{
    void handleObjectNotFoundException(final ObjectNotFoundException e, final Object updatedObject);
}
