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
 * It's a quadtree based gravity model. It's default and recommended model for non-hierarchical layout.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BarnesHut implements Serializable
{
    /**
     * Gravity attracts. Notice that value should be negative. If you want the repulsion to be stronger, decrease the
     * value. Default value is -2000.
     */
    private final Double gravitationalConstant;
    /**
     * Central gravity attractor to pull the network to center. Default value is 0.3.
     */
    private final Double centralGravity;
    /**
     * Spring length which represents length of edge. Default value is 200.
     */
    private final Double springLength;
    /**
     * Indicates how strongly the spring is. Default value is 0.04.
     */
    private final Double springConstant;
    /**
     * Accepted range [0..1]. Indicates how much the previous physics simulation carries over the next iteration. Default value is 0.09.
     */
    private final Double damping;
    /**
     * Accepted range [0..1]. When provided value is larger than 0 then the size of a node is taken into account. Default value is 0.
     */
    private final Double avoidOverlap;


    @JsonCreator
    protected BarnesHut(@JsonProperty("gravitationalConstant") final Double gravitationalConstant,
                    @JsonProperty("centralGravity") final Double centralGravity, @JsonProperty("springLength") final Double springLength,
                    @JsonProperty("springConstant") final Double springConstant, @JsonProperty("damping") final Double damping,
                    @JsonProperty("avoidOverlap") final Double avoidOverlap)
    {
        this.gravitationalConstant = gravitationalConstant;
        this.centralGravity = centralGravity;
        this.springLength = springLength;
        this.springConstant = springConstant;
        this.damping = damping;
        this.avoidOverlap = avoidOverlap;
    }


    public Double getGravitationalConstant()
    {
        return gravitationalConstant;
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


    public Double getAvoidOverlap()
    {
        return avoidOverlap;
    }


    public static class Builder
    {
        private Double gravitationalConstant;
        private Double centralGravity;
        private Double springLength;
        private Double springConstant;
        private Double damping;
        private Double avoidOverlap;


        /**
         * Gravity attracts. Notice that value should be negative. If you want the repulsion to be stronger, decrease the
         * value. Default value is -2000
         */
        public Builder withGravitationalConstant(final Double gravitationalConstant)
        {
            this.gravitationalConstant = gravitationalConstant;
            return this;
        }


        /**
         * Central gravity attractor to pull the network to center. Default value is 0.3
         */
        public Builder withCentralGravity(final Double centralGravity)
        {
            this.centralGravity = centralGravity;
            return this;
        }


        /**
         * Edge is represented by springs. THe springLength is the length of the spring. Default value is 95
         */
        public Builder withSpringLength(final Double springLength)
        {
            this.springLength = springLength;
            return this;
        }


        /**
         * Indicates how strongly the spring is. Default value is 0.04
         */
        public Builder withSpringConstant(final Double springConstant)
        {
            this.springConstant = springConstant;
            return this;
        }


        /**
         * Accepted range [0..1]. Indicates how much the previous physics simulation carries over the next iteration. Default value is 0.09
         */
        public Builder withDamping(final Double damping)
        {
            this.damping = damping;
            return this;
        }


        /**
         * Accepted range [0..1]. When provided value is larger than 0 then the size of a node is taken into account. Default value is 0
         */
        public Builder withAvoidOverlap(final Double avoidOverlap)
        {
            this.avoidOverlap = avoidOverlap;
            return this;
        }


        public BarnesHut build()
        {
            return new BarnesHut(gravitationalConstant, centralGravity, springLength, springConstant, damping, avoidOverlap);
        }
    }
}
