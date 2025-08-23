/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * Represents single setting which consists of key and value.
 */
public class Setting implements Serializable
{
    private final String key;
    private final Serializable value;


    @JsonCreator
    public Setting(@JsonProperty("key") final String key, @JsonProperty("value") final Serializable value)
    {
        this.key = key;
        this.value = value;
    }


    public String getKey()
    {
        return key;
    }


    public Serializable getValue()
    {
        return value;
    }
}
