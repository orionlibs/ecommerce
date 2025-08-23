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
 * Represents width of a node.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WidthConstraint implements Serializable
{
    /**
     * If value is provided then the minimum width of a node is set to this value. Default value is null.
     */
    private final Integer minimum;
    /**
     * If value is provided then the maximum width of a node is set to this value. Default value is null.
     */
    private final Integer maximum;


    @JsonCreator
    protected WidthConstraint(@JsonProperty("minimum") final Integer minimum, @JsonProperty("maximum") final Integer maximum)
    {
        this.minimum = minimum;
        this.maximum = maximum;
    }


    public Integer getMinimum()
    {
        return minimum;
    }


    public Integer getMaximum()
    {
        return maximum;
    }


    public static class Builder
    {
        private Integer minimum;
        private Integer maximum;


        /**
         * If value is provided then the minimum width of a node is set to this value. Default value is null
         */
        public Builder withMinimum(final Integer minimum)
        {
            this.minimum = minimum;
            return this;
        }


        /**
         * If value is provided then the maximum width of a node is set to this value. Default value is null
         */
        public Builder withMaximum(final Integer maximum)
        {
            this.maximum = maximum;
            return this;
        }


        public WidthConstraint build()
        {
            return new WidthConstraint(minimum, maximum);
        }
    }
}
