/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents list of changes which should be applied to networkchart
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NetworkUpdates implements Serializable
{
    public static final NetworkUpdates EMPTY = new NetworkUpdates(new ArrayList<>());
    /**
     * List of changes which should be applied to networkchart
     */
    private final List<NetworkUpdate> updates;


    /**
     * @param updates
     *           List of changes which should be applied to networkchart
     */
    @JsonCreator
    public NetworkUpdates(@JsonProperty("updates") final List<NetworkUpdate> updates)
    {
        this.updates = updates;
    }


    /**
     * @param update
     *           single change which should be applied to networkchart
     */
    public NetworkUpdates(final NetworkUpdate update)
    {
        this.updates = Arrays.asList(update);
    }


    public List<NetworkUpdate> getUpdates()
    {
        return updates;
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
        final NetworkUpdates that = (NetworkUpdates)o;
        return Objects.equals(updates, that.updates);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(updates);
    }
}
