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
 * Represents options for Arrow object. The same options can be applied to {@link Arrows#to}, {@link Arrows#middle} and {@link Arrows#from}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Arrow implements Serializable
{
    /**
     * Toggle the arrow on or off. Default value is false.
     */
    private final Boolean enabled;
    /**
     * The scale factor allows you to change the size of the arrowhead. Default value is 1.
     */
    private final Integer scaleFactor;
    /**
     * The type of endpoint. Default is arrow. Also possible is circle.
     */
    private final String type;


    @JsonCreator
    protected Arrow(@JsonProperty("enabled") final Boolean enabled, @JsonProperty("scaleFactor") final Integer scaleFactor,
                    @JsonProperty("type") final String type)
    {
        this.enabled = enabled;
        this.scaleFactor = scaleFactor;
        this.type = type;
    }


    public Boolean getEnabled()
    {
        return enabled;
    }


    public Integer getScaleFactor()
    {
        return scaleFactor;
    }


    public String getType()
    {
        return type;
    }


    public static class Builder
    {
        private Boolean enabled;
        private Integer scaleFactor;
        private String type;


        /**
         * Toggle the arrow on or off. Default value is false
         */
        public Builder withEnabled(final Boolean enabled)
        {
            this.enabled = enabled;
            return this;
        }


        /**
         * The scale factor allows you to change the size of the arrowhead. Default value is 1
         */
        public Builder withScaleFactor(final Integer scaleFactor)
        {
            this.scaleFactor = scaleFactor;
            return this;
        }


        /**
         * The type of endpoint. Default is arrow. Also possible is circle.
         */
        public Builder withType(final String type)
        {
            this.type = type;
            return this;
        }


        public Arrow build()
        {
            return new Arrow(enabled, scaleFactor, type);
        }
    }
}
