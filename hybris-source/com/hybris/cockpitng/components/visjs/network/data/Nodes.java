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
 * Represents list of nodes. Used in {@link com.hybris.cockpitng.components.visjs.network.event.DeselectNodesEvent} and
 * {@link com.hybris.cockpitng.components.visjs.network.event.RemoveNodesEvent}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Nodes implements Serializable
{
    /**
     * Represents list of nodes. Used in {@link com.hybris.cockpitng.components.visjs.network.event.DeselectNodesEvent}
     * and {@link com.hybris.cockpitng.components.visjs.network.event.RemoveNodesEvent}.
     */
    private final List<Node> listOfNodes;


    @JsonCreator
    public Nodes(@JsonProperty("nodes") final List<Node> nodes)
    {
        this.listOfNodes = nodes;
    }


    public List<Node> getNodes()
    {
        return listOfNodes;
    }
}
