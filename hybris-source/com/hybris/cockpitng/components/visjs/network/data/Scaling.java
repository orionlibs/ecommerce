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
 * Represents option for scaling edges and nodes according to their properties.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Scaling implements Serializable
{
    /**
     * Minimum value of scaling factor. Default value for edge is 1. Default value for node is 10.
     */
    private final Integer min;
    /**
     * Maximum value of scaling factor. Default value for edge is 15. Default value for node is 30.
     */
    private final Integer max;
    private final ScalingLabel label;
    /**
     * JavaScript's function which determines how nodes/edges are scaled. The function should have the following
     * signature: function(min, max, total, value). Default value is null.
     */
    private final String customScalingFunction;


    @JsonCreator
    public Scaling(@JsonProperty("min") final Integer min, @JsonProperty("max") final Integer max,
                    @JsonProperty("label") final ScalingLabel label,
                    @JsonProperty("customScalingFunction") final String customScalingFunction)
    {
        this.min = min;
        this.max = max;
        this.label = label;
        this.customScalingFunction = customScalingFunction;
    }


    public Integer getMin()
    {
        return min;
    }


    public Integer getMax()
    {
        return max;
    }


    public ScalingLabel getLabel()
    {
        return label;
    }


    public String getCustomScalingFunction()
    {
        return customScalingFunction;
    }


    public static class Builder
    {
        private Integer min;
        private Integer max;
        private ScalingLabel label;
        private String customScalingFunction;


        /**
         * Minimum value of scaling factor. Default value for edge is 1. Default value for node is 10.
         */
        public Builder withMin(final Integer min)
        {
            this.min = min;
            return this;
        }


        /**
         * Maximum value of scaling factor. Default value for edge is 15. Default value for node is 30.
         */
        public Builder withMax(final Integer max)
        {
            this.max = max;
            return this;
        }


        public Builder withLabel(final ScalingLabel label)
        {
            this.label = label;
            return this;
        }


        /**
         * JavaScript's function which determines how nodes/edges are scaled. The function should have the following
         * signature: function(min, max, total, value). Default value is null.
         */
        public Builder withCustomScalingFunction(final String customScalingFunction)
        {
            this.customScalingFunction = customScalingFunction;
            return this;
        }


        public Scaling build()
        {
            return new Scaling(min, max, label, customScalingFunction);
        }
    }
}
