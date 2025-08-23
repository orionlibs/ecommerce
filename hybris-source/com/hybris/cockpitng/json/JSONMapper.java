/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.json;

/**
 * Interface of mapper from JSON objects to Java objects and vice versa
 *
 */
public interface JSONMapper
{
    <T> T fromJSONString(final String json, Class<T> resultType);


    String toJSONString(Object object);
}
