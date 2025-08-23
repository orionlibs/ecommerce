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
 * Represents options for casting a shadow of node and edge.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Shadow implements Serializable
{
    /**
     * Enables casting a shadow. Default value is false.
     */
    private final Boolean enabled;
    /**
     * Color of a shadow. Possible formats are: "rgb(255,255,255)", "rgba(255,255,255,1)" and "#FFFFFF". Default value is
     * "rgba(0,0,0,0.5)".
     */
    private final String color;
    /**
     * Blur size of a shadow. Default value is 10.
     */
    private final Integer size;
    /**
     * X offset. Default value is 5.
     */
    private final Integer x;
    /**
     * Y offset. Default value is 5.
     */
    private final Integer y;


    @JsonCreator
    protected Shadow(@JsonProperty("enabled") final Boolean enabled, @JsonProperty("color") final String color,
                    @JsonProperty("size") final Integer size, @JsonProperty("x") final Integer x, @JsonProperty("y") final Integer y)
    {
        this.enabled = enabled;
        this.color = color;
        this.size = size;
        this.x = x;
        this.y = y;
    }


    public Boolean getEnabled()
    {
        return enabled;
    }


    public String getColor()
    {
        return color;
    }


    public Integer getSize()
    {
        return size;
    }


    public Integer getX()
    {
        return x;
    }


    public Integer getY()
    {
        return y;
    }


    public static class Builder
    {
        private Boolean enabled;
        private String color;
        private Integer size;
        private Integer x;
        private Integer y;


        /**
         * Enables casting a shadow. Default value is false.
         */
        public Builder withEnabled(final Boolean enabled)
        {
            this.enabled = enabled;
            return this;
        }


        /**
         * Color of a shadow. Possible formats are: "rgb(255,255,255)", "rgba(255,255,255,1)" and "#FFFFFF". Default value
         * is "rgba(0,0,0,0.5)".
         */
        public Builder withColor(final String color)
        {
            this.color = color;
            return this;
        }


        /**
         * Blur size of a shadow. Default value is 10.
         */
        public Builder withSize(final Integer size)
        {
            this.size = size;
            return this;
        }


        /**
         * X offset. Default value is 5.
         */
        public Builder withX(final Integer x)
        {
            this.x = x;
            return this;
        }


        /**
         * Y offset. Default value is 5.
         */
        public Builder withY(final Integer y)
        {
            this.y = y;
            return this;
        }


        public Shadow build()
        {
            return new Shadow(enabled, color, size, x, y);
        }
    }
}
