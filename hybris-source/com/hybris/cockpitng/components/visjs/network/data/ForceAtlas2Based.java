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
 * Represents physics model similar to {@link BarnesHut}. The main difference is that repulsion is linear instead of
 * quadratic. Moreover, weight of node is multiplied by number of edges plus one.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ForceAtlas2Based implements Serializable
{
    /**
     * Gravity attracts. Notice that value should be negative. If you want the repulsion to be stronger, decrease the
     * value. Default value is -50.
     */
    private final Double gravitationalConstant;
    /**
     * Central gravity attractor to pull the network to center. Default value is 0.01.
     */
    private final Double centralGravity;
    /**
     * Spring length which represents length of edge. Default value is 100.
     */
    private final Double springLength;
    /**
     * Indicates how strongly the spring is. Default value is 0.08.
     */
    private final Double springConstant;
    /**
     * Accepted range [0..1]. Indicates how much the previous physics simulation carries over the next iteration. Default
     * value is 0.4.
     */
    private final Double damping;
    /**
     * Accepted range [0..1]. When provided value is larger than 0 then the size of a node is taken into account. Default
     * value is 0.
     */
    private final Double avoidOverlap;


    @JsonCreator
    protected ForceAtlas2Based(@JsonProperty("gravitationalConstant") final Double gravitationalConstant,
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
         * value. Default value is -50.
         */
        public Builder withGravitationalConstant(final Double gravitationalConstant)
        {
            this.gravitationalConstant = gravitationalConstant;
            return this;
        }


        /**
         * Central gravity attractor to pull the network to center. Default value is 0.01.
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
         * Indicates how strongly the spring is. Default value is 0.08.
         */
        public Builder withSpringConstant(final Double springConstant)
        {
            this.springConstant = springConstant;
            return this;
        }


        /**
         * Accepted range [0..1]. Indicates how much the previous physics simulation carries over the next iteration.
         * Default value is 0.4.
         */
        public Builder withDamping(final Double damping)
        {
            this.damping = damping;
            return this;
        }


        /**
         * Accepted range [0..1]. When provided value is larger than 0 then the size of a node is taken into account.
         * Default value is 0.
         */
        public Builder withAvoidOverlap(final Double avoidOverlap)
        {
            this.avoidOverlap = avoidOverlap;
            return this;
        }


        public ForceAtlas2Based build()
        {
            return new ForceAtlas2Based(gravitationalConstant, centralGravity, springLength, springConstant, damping, avoidOverlap);
        }
    }
}
