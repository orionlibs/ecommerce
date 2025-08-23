/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.data;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.Serializable;

/**
 * Abstract class for a node and an edge entity.
 */
public abstract class NetworkEntity implements Serializable
{
    /**
     * Identifier of entity.
     */
    protected final String id;
    /**
     * Generic object which can store additional information about an edge or a node.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "dataClass")
    protected final Serializable data;
    /**
     * Indicates type of entity. Possible values are node and edge.
     */
    protected final String type;


    public NetworkEntity(final String id, final Serializable data, final String type)
    {
        this.id = id;
        this.data = data;
        this.type = type;
    }


    public String getId()
    {
        return id;
    }


    public Object getData()
    {
        return data;
    }


    public String getType()
    {
        return type;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(o.getClass() != this.getClass())
        {
            return false;
        }
        final NetworkEntity networkEntity = (NetworkEntity)o;
        return getId() != null ? getId().equals(networkEntity.getId()) : (networkEntity.getId() == null);
    }


    @Override
    public int hashCode()
    {
        return getId() != null ? getId().hashCode() : 0;
    }
}
