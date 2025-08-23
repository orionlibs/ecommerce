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
 * Represents configuration for color of edge.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EdgeColor implements Serializable
{
    /**
     * Represents color information when edge is not selected. Default value is #848484.
     */
    private final String color;
    /**
     * Represents color information when edge is selected. Default value is #848484.
     */
    private final String highlight;
    /**
     * Represents color information when mouse is over an edge. Default value is #848484.
     */
    private final String hover;
    /**
     * When set to "from", then edge will inherit color from a border of node on a 'from' side. When set to 'to' then
     * edge will inherit color from a border of node on a 'to' side. Possible values are true, false, 'from','to','both'.
     * Default value is 'from'.
     */
    private final String inherit;
    /**
     * Opacity of an edge. The allowed range of opacity values are [0..1]. Default value is 1.
     */
    private final Integer opacity;


    @JsonCreator
    protected EdgeColor(@JsonProperty("color") final String color, @JsonProperty("highlight") final String highlight,
                    @JsonProperty("hover") final String hover, @JsonProperty("inherit") final String inherit,
                    @JsonProperty("opacity") final Integer opacity)
    {
        this.color = color;
        this.highlight = highlight;
        this.hover = hover;
        this.inherit = inherit;
        this.opacity = opacity;
    }


    public String getColor()
    {
        return color;
    }


    public String getHighlight()
    {
        return highlight;
    }


    public String getHover()
    {
        return hover;
    }


    public String getInherit()
    {
        return inherit;
    }


    public Integer getOpacity()
    {
        return opacity;
    }


    public static class Builder
    {
        private String color;
        private String highlight;
        private String hover;
        private String inherit;
        private Integer opacity;


        /**
         * Represents color information when edge is not selected. Default value is #848484.
         */
        public Builder withColor(final String color)
        {
            this.color = color;
            return this;
        }


        /**
         * Represents color information when edge is selected. Default value is #848484.
         */
        public Builder withHighlight(final String highlight)
        {
            this.highlight = highlight;
            return this;
        }


        /**
         * Represents color information when mouse is over an edge. Default value is #848484.
         */
        public Builder withHover(final String hover)
        {
            this.hover = hover;
            return this;
        }


        /**
         * When set to "from", then edge will inherit color from a border of node on a 'from' side. When set to 'to' then
         * edge will inherit color from a border of node on a 'to' side. Possible values are true, false,
         * 'from','to','both'. Default value is 'from'.
         */
        public Builder withInherit(final String inherit)
        {
            this.inherit = inherit;
            return this;
        }


        /**
         * Opacity of an edge. The allowed range of opacity values are [0..1]. Default value is 1.
         */
        public Builder withOpacity(final Integer opacity)
        {
            this.opacity = opacity;
            return this;
        }


        public EdgeColor build()
        {
            return new EdgeColor(color, highlight, hover, inherit, opacity);
        }
    }
}
