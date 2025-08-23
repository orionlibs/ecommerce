/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Collection;

/**
 * Represents list of settings.
 */
public class NetworkSettings implements Serializable
{
    private final Collection<Setting> settings;


    @JsonCreator
    public NetworkSettings(@JsonProperty("settings") final Collection<Setting> settings)
    {
        this.settings = settings;
    }


    public Collection<Setting> getSettings()
    {
        return settings;
    }
}
