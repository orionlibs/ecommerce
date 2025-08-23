/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.NetworkEntity;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents single change on networkchart. The change consists of {@link Action} and {@link NetworkEntity} which is
 * parent class of {@link Edge} and {@link Node}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NetworkUpdate implements Serializable
{
    /**
     * Represents possible actions while interacting with network chart
     */
    private final Action action;
    /**
     * Represents {@link Edge} or {@link Node}
     */
    private final NetworkEntity entity;


    /**
     * @param action
     *           Represents possible actions while interacting with network chart
     * @param entity
     *           Represents {@link Edge} or {@link Node}
     */
    @JsonCreator
    public NetworkUpdate(@JsonProperty("action") final Action action, @JsonProperty("entity") final NetworkEntity entity)
    {
        this.action = action;
        this.entity = entity;
    }


    public Action getAction()
    {
        return action;
    }


    public NetworkEntity getEntity()
    {
        return entity;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final NetworkUpdate that = (NetworkUpdate)o;
        return action == that.action && Objects.equals(entity, that.entity);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(action, entity);
    }
}
