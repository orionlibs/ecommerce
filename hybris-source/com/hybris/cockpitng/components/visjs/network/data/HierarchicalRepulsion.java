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
 * Hierarchical Repulsion model used in {@link Physics}. In this model levels are taken into account and forces are
 * normalized.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HierarchicalRepulsion implements Serializable
{
    /**
     * Central gravity attractor to pull the network to center. Default value is 0.0.
     */
    private final Double centralGravity;
    /**
     * Spring length which represents length of edge. Default value is 100.
     */
    private final Double springLength;
    /**
     * Indicates how strongly the spring is. Default value is 0.01.
     */
    private final Double springConstant;
    /**
     * Range of influence for the repulsion. Default value is 120.
     */
    private final Double nodeDistance;
    /**
     * Accepted range [0..1]. Indicates how much the previous physics simulation carries over the next iteration. Default
     * value is 0.09.
     */
    private final Double damping;


    @JsonCreator
    protected HierarchicalRepulsion(@JsonProperty("centralGravity") final Double centralGravity,
                    @JsonProperty("springLength") final Double springLength, @JsonProperty("springConstant") final Double springConstant,
                    @JsonProperty("nodeDistance") final Double nodeDistance, @JsonProperty("damping") final Double damping)
    {
        this.centralGravity = centralGravity;
        this.springLength = springLength;
        this.springConstant = springConstant;
        this.damping = damping;
        this.nodeDistance = nodeDistance;
    }


    public Double getCentralGravity()
    {
        return centralGravity;
    }


    public Double getSpringLength()
    {
        return springLength;
    }


    public Double getSpringConstant()
    {
        return springConstant;
    }


    public Double getDamping()
    {
        return damping;
    }


    public Double getNodeDistance()
    {
        return nodeDistance;
    }


    public static class Builder
    {
        private Double centralGravity;
        private Double springLength;
        private Double springConstant;
        private Double nodeDistance;
        private Double damping;


        /**
         * Central gravity attractor to pull the network to center. Default value is 0.0.
         */
        public Builder withCentralGravity(final Double centralGravity)
        {
            this.centralGravity = centralGravity;
            return this;
        }


        /**
         * Spring length which represents length of edge. Default value is 100.
         */
        public Builder withSpringLength(final Double springLength)
        {
            this.springLength = springLength;
            return this;
        }


        /**
         * Indicates how strongly the spring is. Default value is 0.01.
         */
        public Builder withSpringConstant(final Double springConstant)
        {
            this.springConstant = springConstant;
            return this;
        }


        /**
         * Accepted range [0..1]. Indicates how much the previous physics simulation carries over the next iteration. Default
         * value is 0.09.
         */
        public Builder withDamping(final Double damping)
        {
            this.damping = damping;
            return this;
        }


        /**
         * Range of influence for the repulsion. Default value is 120.
         */
        public Builder withNodeDistance(final Double nodeDistance)
        {
            this.nodeDistance = nodeDistance;
            return this;
        }


        public HierarchicalRepulsion build()
        {
            return new HierarchicalRepulsion(centralGravity, springLength, springConstant, nodeDistance, damping);
        }
    }
}
