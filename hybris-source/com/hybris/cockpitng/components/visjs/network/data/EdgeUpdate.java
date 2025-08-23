/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * Object used in {@link com.hybris.cockpitng.components.visjs.network.event.EditNodeEvent}. Represents previous and
 * newly established connection between nodes.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EdgeUpdate implements Serializable
{
    /**
     * Previous connection between two nodes.
     */
    private final Edge previous;
    /**
     * Newly established connection between two nodes.
     */
    private final Edge current;


    public EdgeUpdate(@JsonProperty("previous") final Edge previous, @JsonProperty("current") final Edge current)
    {
        this.previous = previous;
        this.current = current;
    }


    public Edge getPrevious()
    {
        return previous;
    }


    public Edge getCurrent()
    {
        return current;
    }
}
