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
 * Indicates whether selecting or hovering on an edge should change the edge and/or it's label. If the option is not
 * provided, no change will be applied.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChosenEdge implements Serializable
{
    /**
     * "true", "false" or javascript's function can be provided. If 'true', selecting or hovering on an edge will change
     * its characteristics. if 'false', selecting or hovering on an edge will not change its characteristics. If a
     * function is provided, it should have the following signature: function(values, id, selected, hovering)
     * Default value is null.
     */
    private final String edge;
    /**
     * "true", "false" or javascript's function can be provided. If 'true', selecting or hovering on an edge will change
     * its label's characteristics. if 'false', selecting or hovering on an edge will not change its label's characteristics. If a
     * function is provided, it should have the following signature: function(values, id, selected, hovering)
     * Default value is null.
     */
    private final String label;


    @JsonCreator
    protected ChosenEdge(@JsonProperty("edge") final String edge, @JsonProperty("label") final String label)
    {
        this.edge = edge;
        this.label = label;
    }


    public String getEdge()
    {
        return edge;
    }


    public String getLabel()
    {
        return label;
    }


    public static class Builder
    {
        private String edge;
        private String label;


        /**
         * "true", "false" or javascript's function can be provided. If 'true', selecting or hovering on an edge will
         * change its characteristics. if 'false', selecting or hovering on an edge will not change its characteristics.
         * If a function is provided, it should have the following signature: function(values, id, selected, hovering)
         * Default value is null
         */
        public Builder withEdge(final String edge)
        {
            this.edge = edge;
            return this;
        }


        /**
         * "true", "false" or javascript's function can be provided. If 'true', selecting or hovering on an edge will change
         * its label's characteristics. if 'false', selecting or hovering on an edge will not change its label's characteristics. If a
         * function is provided, it should have the following signature: function(values, id, selected, hovering)
         * Default value is null
         */
        public Builder withLabel(final String label)
        {
            this.label = label;
            return this;
        }


        public ChosenEdge build()
        {
            return new ChosenEdge(edge, label);
        }
    }
}
