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
 * A speed of view on pressing a key or using navigation buttons
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Speed implements Serializable
{
    /**
     * A speed of view in the x direction on pressing a key or using navigation buttons. Default value is 1.
     */
    private final Integer x;
    /**
     * A speed of view in the y direction on pressing a key or using navigation buttons. Default value is 1.
     */
    private final Integer y;
    /**
     * A speed of zoom on pressing a key or using navigation buttons. Default value is 0.02.
     */
    private final Double zoom;


    @JsonCreator
    protected Speed(@JsonProperty("x") final Integer x, @JsonProperty("y") final Integer y, @JsonProperty("zoom") final Double zoom)
    {
        this.x = x;
        this.y = y;
        this.zoom = zoom;
    }


    public Integer getX()
    {
        return x;
    }


    public Integer getY()
    {
        return y;
    }


    public Double getZoom()
    {
        return zoom;
    }


    public static class Builder
    {
        private Integer x;
        private Integer y;
        private Double zoom;


        /**
         * A speed of view in the x direction on pressing a key or using navigation buttons. Default value is 1.
         */
        public Builder withX(final Integer x)
        {
            this.x = x;
            return this;
        }


        /**
         * A speed of view in the y direction on pressing a key or using navigation buttons. Default value is 1.
         */
        public Builder withY(final Integer y)
        {
            this.y = y;
            return this;
        }


        /**
         * A speed of zoom on pressing a key or using navigation buttons. Default value is 0.02
         */
        public Builder withZoom(final Double zoom)
        {
            this.zoom = zoom;
            return this;
        }


        public Speed build()
        {
            return new Speed(x, y, zoom);
        }
    }
}
