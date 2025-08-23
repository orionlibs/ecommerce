/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

/**
 * Represents network which will be drawn. The network consists of nodes and edges.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class Network implements Serializable
{
    public static final Network EMPTY = new Network(Collections.emptyList(), Collections.emptyList());
    /**
     * List of nodes which are part of network.
     */
    private final Collection<Node> nodes;
    /**
     * List of edges between nodes which are part of network.
     */
    private final Collection<Edge> edges;


    @JsonCreator
    public Network(@JsonProperty("nodes") final Collection<Node> nodes, @JsonProperty("edges") final Collection<Edge> edges)
    {
        this.nodes = nodes;
        this.edges = edges;
    }


    public Collection<Node> getNodes()
    {
        return nodes;
    }


    public Collection<Edge> getEdges()
    {
        return edges;
    }
}
