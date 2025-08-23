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
 * Indicates whether edge should be drawn as a quadratic bezier curve. Performance of these edges are worse, but they
 * look better.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Smooth implements Serializable
{
    /**
     * Enables quadratic bezier curves. Default value is true.
     */
    private final Boolean enabled;
    /**
     * Possible types are "dynamic", "continuous", "discrete", "diagonalCross", "straightCross", "horizontal",
     * "vertical", "curvedCW", "curvedCCW", "cubicBezier". Default value is "dynamic"
     */
    private final String type;
    /**
     * Possible values are "horizontal", "vertical", "none". The options are taken into account only when type is set to
     * cubicBezier.
     */
    private final String forceDirection;
    /**
     * Accepted range is [0..1]. Default value is 0.5
     */
    private final Double roundness;


    @JsonCreator
    protected Smooth(@JsonProperty("enabled") final Boolean enabled, @JsonProperty("type") final String type,
                    @JsonProperty("forceDirection") final String forceDirection, @JsonProperty("roundness") final Double roundness)
    {
        this.enabled = enabled;
        this.type = type;
        this.forceDirection = forceDirection;
        this.roundness = roundness;
    }


    public Boolean getEnabled()
    {
        return enabled;
    }


    public String getType()
    {
        return type;
    }


    public String getForceDirection()
    {
        return forceDirection;
    }


    public Double getRoundness()
    {
        return roundness;
    }


    public static class Builder
    {
        private Boolean enabled;
        private String type;
        private String forceDirection;
        private Double roundness;


        /**
         * Enables quadratic bezier curves. Default value is true.
         */
        public Builder withEnabled(final Boolean enabled)
        {
            this.enabled = enabled;
            return this;
        }


        /**
         * Possible types are "dynamic", "continuous", "discrete", "diagonalCross", "straightCross", "horizontal",
         * "vertical", "curvedCW", "curvedCCW", "cubicBezier". Default value is "dynamic"
         */
        public Builder withType(final String type)
        {
            this.type = type;
            return this;
        }


        /**
         * Possible values are "horizontal", "vertical", "none". The options are taken into account only when type is set
         * to cubicBezier.
         */
        public Builder withForceDirection(final String forceDirection)
        {
            this.forceDirection = forceDirection;
            return this;
        }


        /**
         * Accepted range is [0..1]. Default value is 0.5
         */
        public Builder withRoundness(final Double roundness)
        {
            this.roundness = roundness;
            return this;
        }


        public Smooth build()
        {
            return new Smooth(enabled, type, forceDirection, roundness);
        }
    }
}
