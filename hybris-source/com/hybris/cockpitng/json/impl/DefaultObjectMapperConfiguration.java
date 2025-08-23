/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.json.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hybris.cockpitng.json.ObjectMapperConfiguration;

/**
 * Creates {@link ObjectMapper} with configuration:
 * <ul>
 * <li>FAIL_ON_EMPTY_BEANS: false</li>
 * <li>FAIL_ON_UNKNOWN_PROPERTIES: false</li>
 * </ul>
 */
public class DefaultObjectMapperConfiguration implements ObjectMapperConfiguration
{
    public static final String JSON_CLASS_PROPERTY = "$class";


    @Override
    public ObjectMapper configureObjectMapper(final Class<?> objectType, final ObjectMapper mapper)
    {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
