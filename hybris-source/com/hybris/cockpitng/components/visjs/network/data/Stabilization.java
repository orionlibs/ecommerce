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
 * Indicates whether network should be stabilized on load using default settings
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Stabilization implements Serializable
{
    /**
     * Enables stabilization. Default value is true
     */
    private final Boolean enabled;
    /**
     * Network tries to stabilize itself with the maximum number of iterations. Default value is 1000.
     */
    private final Integer iterations;
    /**
     * The interval determines after how many iterations the 'stabilizationProgress' event is triggered. Default value is
     * 50
     */
    private final Integer updateInterval;
    /**
     * If you have fixed positions of all nodes and you want to stabilize only edges then set the value to true. Default
     * value is false
     */
    private final Boolean onlyDynamicEdges;
    /**
     * Indicates whether the view should be zoom to fit all nodes when a stabilization is finished. Default value is true
     */
    private final Boolean fit;


    @JsonCreator
    public Stabilization(@JsonProperty("enabled") final Boolean enabled, @JsonProperty("iterations") final Integer iterations,
                    @JsonProperty("updateInterval") final Integer updateInterval,
                    @JsonProperty("onlyDynamicEdges") final Boolean onlyDynamicEdges, @JsonProperty("fit") final Boolean fit)
    {
        this.enabled = enabled;
        this.iterations = iterations;
        this.updateInterval = updateInterval;
        this.onlyDynamicEdges = onlyDynamicEdges;
        this.fit = fit;
    }


    public Boolean getEnabled()
    {
        return enabled;
    }


    public Integer getIterations()
    {
        return iterations;
    }


    public Integer getUpdateInterval()
    {
        return updateInterval;
    }


    public Boolean getOnlyDynamicEdges()
    {
        return onlyDynamicEdges;
    }


    public Boolean getFit()
    {
        return fit;
    }


    public static class Builder
    {
        private Boolean enabled;
        private Integer iterations;
        private Integer updateInterval;
        private Boolean onlyDynamicEdges;
        private Boolean fit;


        /**
         * Enables stabilization. Default value is true
         */
        public Builder withEnabled(final Boolean enabled)
        {
            this.enabled = enabled;
            return this;
        }


        /**
         * Network tries to stabilize itself with the maximum number of iterations. Default value is 1000
         */
        public Builder withIterations(final Integer iterations)
        {
            this.iterations = iterations;
            return this;
        }


        /**
         * The interval determines after how many iterations the 'stabilizationProgress' event is triggered. Default value
         * is 50
         */
        public Builder withUpdateInterval(final Integer updateInterval)
        {
            this.updateInterval = updateInterval;
            return this;
        }


        /**
         * If you have fixed positions of all nodes and you want to stabilize only edges then set the value to true.
         * Default value is false
         */
        public Builder withOnlyDynamicEdges(final Boolean onlyDynamicEdges)
        {
            this.onlyDynamicEdges = onlyDynamicEdges;
            return this;
        }


        /**
         * Indicates whether the view should be zoom to fit all nodes when a stabilization is finished. Default value is true
         */
        public Builder withFit(final Boolean fit)
        {
            this.fit = fit;
            return this;
        }


        public Stabilization build()
        {
            return new Stabilization(enabled, iterations, updateInterval, onlyDynamicEdges, fit);
        }
    }
}
