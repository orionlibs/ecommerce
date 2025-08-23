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
 * Represents a point. It uses the {@link Number} to be compatible with Javascript's Number class.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Point implements Serializable
{
    /**
     * X coordinate of the point.
     */
    private final Number x;
    /**
     * Y coordinate of the point.
     */
    private final Number y;


    @JsonCreator
    public Point(@JsonProperty("x") final Number x, @JsonProperty("y") final Number y)
    {
        this.x = x;
        this.y = y;
    }


    public Number getX()
    {
        return x;
    }


    public Number getY()
    {
        return y;
    }


    public static class Builder
    {
        private Number x;
        private Number y;


        /**
         * X coordinate
         */
        public Builder withX(final Number x)
        {
            this.x = x;
            return this;
        }


        /**
         * Y coordinate
         */
        public Builder withY(final Number y)
        {
            this.y = y;
            return this;
        }


        public Point build()
        {
            return new Point(x, y);
        }
    }
}
