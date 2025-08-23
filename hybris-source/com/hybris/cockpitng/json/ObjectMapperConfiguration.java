/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.json;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Interface for objects which are able to configure {@link ObjectMapper}
 */
public interface ObjectMapperConfiguration
{
    /**
     *
     * @param objectType type of object that is going to be mapped
     * @param mapper mapper to be configured
     * @return fully configured mapper to be used for mapping
     */
    ObjectMapper configureObjectMapper(Class<?> objectType, ObjectMapper mapper);
}
