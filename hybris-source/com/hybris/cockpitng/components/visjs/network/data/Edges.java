/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

/**
 * Object used in {@link com.hybris.cockpitng.components.visjs.network.event.DeselectEdgesEvent} and {@link com.hybris.cockpitng.components.visjs.network.event.RemoveEdgesEvent}.
 * Represents list of edges.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Edges implements Serializable
{
    /**
     * List of edges which should be deselected or removed, depending of event.
     */
    private final List<Edge> listOfEdges;


    @JsonCreator
    public Edges(@JsonProperty("edges") final List<Edge> edges)
    {
        this.listOfEdges = edges;
    }


    public List<Edge> getEdges()
    {
        return listOfEdges;
    }
}
