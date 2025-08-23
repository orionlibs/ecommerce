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
 * Indicates whether selecting or hovering on a node should change the node and/or it's label. If the option is not
 * provided, no change will be applied.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChosenNode implements Serializable
{
    /**
     * "true", "false" or javascript's function can be provided. If 'true', selecting or hovering on a node will change
     * its characteristics. if 'false', selecting or hovering on a node will not change its characteristics. If a
     * function is provided, it should have the following signature: function(values, id, selected, hovering)
     * Default value is null.
     */
    private final String node;
    /**
     * "true", "false" or javascript's function can be provided. If 'true', selecting or hovering on a node will change
     * its label's characteristics. if 'false', selecting or hovering on a node will not change its label's characteristics. If a
     * function is provided, it should have the following signature: function(values, id, selected, hovering)
     * Default value is null.
     */
    private final String label;


    @JsonCreator
    protected ChosenNode(@JsonProperty("node") final String node, @JsonProperty("label") final String label)
    {
        this.node = node;
        this.label = label;
    }


    public String getNode()
    {
        return node;
    }


    public String getLabel()
    {
        return label;
    }


    public static class Builder
    {
        private String node;
        private String label;


        /**
         * "true", "false" or javascript's function can be provided. If 'true', selecting or hovering on a node will change
         * its characteristics. if 'false', selecting or hovering on a node will not change its characteristics. If a
         * function is provided, it should have the following signature: function(values, id, selected, hovering)
         * Default value is null
         */
        public Builder withNode(final String node)
        {
            this.node = node;
            return this;
        }


        /**
         * "true", "false" or javascript's function can be provided. If 'true', selecting or hovering on a node will change
         * its label's characteristics. if 'false', selecting or hovering on a node will not change its label's characteristics. If a
         * function is provided, it should have the following signature: function(values, id, selected, hovering)
         * Default value is null
         */
        public Builder withLabel(final String label)
        {
            this.label = label;
            return this;
        }


        public ChosenNode build()
        {
            return new ChosenNode(node, label);
        }
    }
}
