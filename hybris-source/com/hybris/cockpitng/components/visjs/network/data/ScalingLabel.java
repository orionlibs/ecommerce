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
 * Represents options for scaling label of nodes and edges.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ScalingLabel implements Serializable
{
    /**
     * Enables scaling of label. Default value is false.
     */
    private final Boolean enabled;
    /**
     * Minimum font-size of label. Default value is 14.
     */
    private final Integer min;
    /**
     * Maximum font-size of label. Default value is 30.
     */
    private final Integer max;
    /**
     * Maximum font-size while zooming in a view. Default value is 30.
     */
    private final Integer maxVisible;
    /**
     * Minimum font-size while zooming out a view. Default value is 5.
     */
    private final Integer drawThreshold;


    @JsonCreator
    protected ScalingLabel(@JsonProperty("enabled") final Boolean enabled, @JsonProperty("min") final Integer min,
                    @JsonProperty("max") final Integer max, @JsonProperty("maxVisible") final Integer maxVisible,
                    @JsonProperty("drawThreshold") final Integer drawThreshold)
    {
        this.enabled = enabled;
        this.min = min;
        this.max = max;
        this.maxVisible = maxVisible;
        this.drawThreshold = drawThreshold;
    }


    public Boolean getEnabled()
    {
        return enabled;
    }


    public Integer getMin()
    {
        return min;
    }


    public Integer getMax()
    {
        return max;
    }


    public Integer getMaxVisible()
    {
        return maxVisible;
    }


    public Integer getDrawThreshold()
    {
        return drawThreshold;
    }


    public static class Builder
    {
        private Boolean enabled;
        private Integer min;
        private Integer max;
        private Integer maxVisible;
        private Integer drawThreshold;


        /**
         * Enables scaling of label. Default value is false.
         */
        public Builder withEnabled(final Boolean enabled)
        {
            this.enabled = enabled;
            return this;
        }


        /**
         * Minimum font-size of label. Default value is 14.
         */
        public Builder withMin(final Integer min)
        {
            this.min = min;
            return this;
        }


        /**
         * Maximum font-size of label. Default value is 30.
         */
        public Builder withMax(final Integer max)
        {
            this.max = max;
            return this;
        }


        /**
         * Maximum font-size while zooming a view. Default value is 30.
         */
        public Builder withMaxVisible(final Integer maxVisible)
        {
            this.maxVisible = maxVisible;
            return this;
        }


        /**
         * Minimum font-size while zooming out a view. Default value is 5.
         */
        public Builder withDrawThreshold(final Integer drawThreshold)
        {
            this.drawThreshold = drawThreshold;
            return this;
        }


        public ScalingLabel build()
        {
            return new ScalingLabel(enabled, min, max, maxVisible, drawThreshold);
        }
    }
}
