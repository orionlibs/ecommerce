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
 * Contains color information for border and background
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Color implements Serializable
{
    /**
     * Color of a border. Default value is '#2B7CE9'.
     */
    private final String border;
    /**
     * Color of a background. Default value is '#D2E5FF'.
     */
    private final String background;


    @JsonCreator
    public Color(@JsonProperty("border") final String border, @JsonProperty("background") final String background)
    {
        this.border = border;
        this.background = background;
    }


    public String getBorder()
    {
        return border;
    }


    public String getBackground()
    {
        return background;
    }


    public static class Builder
    {
        private String border;
        private String background;


        /**
         * Color of a border. Default value is '#2B7CE9'
         */
        public Builder withBorder(final String border)
        {
            this.border = border;
            return this;
        }


        /**
         * Color of a background. Default value is '#D2E5FF'
         */
        public Builder withBackground(final String background)
        {
            this.background = background;
            return this;
        }


        public Color build()
        {
            return new Color(border, background);
        }
    }
}
