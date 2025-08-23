/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.jaxb;

import java.util.Set;

/**
 * Defines registry that contains information about all schema locations used across entire framework.
 */
public interface SchemaLocationRegistry
{
    /**
     * Returns all schemas locations defined for current {@link SchemaLocationRegistry}.
     *
     * @return all schemas locations.
     */
    Set<String> getSchemaLocations();


    /**
     * Returns schema location by given id.
     *
     * @return schema location.
     */
    String getSchemaLocation(String id);
}
