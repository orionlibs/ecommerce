/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * Configuration class for a canvas
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Configure implements Serializable
{
    /**
     * Enables configuration. Default value is true.
     */
    private final Boolean enabled;
    /**
     * 'true', 'false', String, Array or javascript's function. 'true' means all options. 'false' means no options. If
     * string is supplied, any combination is allowed: edges, layout, interaction, manipulation, physics, selection,
     * renderer. When function is provided, then it should have the following signature: function(option, path). Default
     * value is true.
     */
    private final String filter;
    /**
     * Shows options button at the bottom of the configurator. Default value is true.
     */
    private final Boolean showButton;


    @JsonCreator
    protected Configure(@JsonProperty("enabled") final Boolean enabled, @JsonProperty("filter") final String filter,
                    @JsonProperty("showButton") final Boolean showButton)
    {
        this.enabled = enabled;
        this.filter = filter;
        this.showButton = showButton;
    }


    public Boolean getEnabled()
    {
        return enabled;
    }


    public String getFilter()
    {
        return filter;
    }


    public Boolean getShowButton()
    {
        return showButton;
    }


    public static class Builder
    {
        private Boolean enabled;
        private String filter;
        private Boolean showButton;


        /**
         * Enables configuration. Default value is true
         */
        public Builder withEnabled(final Boolean enabled)
        {
            this.enabled = enabled;
            return this;
        }


        /**
         * 'true', 'false', String, Array or javascript's function. 'true' means all options. 'false' means no options. If
         * string is supplied, any combination is allowed: edges, layout, interaction, manipulation, physics, selection,
         * renderer. When function is provided, then it should have the following signature: function(option, path).
         * Default value is true
         */
        public Builder withFilter(final String filter)
        {
            this.filter = filter;
            return this;
        }


        /**
         * Shows options button at the bottom of the configurator. Default value is true
         */
        public Builder withShowButton(final Boolean showButton)
        {
            this.showButton = showButton;
            return this;
        }


        public Configure build()
        {
            return new Configure(enabled, filter, showButton);
        }
    }
}
