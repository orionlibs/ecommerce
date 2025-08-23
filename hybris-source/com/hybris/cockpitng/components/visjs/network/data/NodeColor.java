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
 * Represents configuration for color of node.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NodeColor implements Serializable
{
    /**
     * Represents border color when node is not selected. Default value is #2B7CE9.
     */
    private final String border;
    /**
     * Represents background color when node is not selected. Default value is #2B7CE9.
     */
    private final String background;
    /**
     * Represents color information when node is selected.
     */
    private final Color highlight;
    /**
     * Represents color information when mouse is over a node.
     */
    private final Color hover;


    @JsonCreator
    protected NodeColor(@JsonProperty("border") final String border, @JsonProperty("background") final String background,
                    @JsonProperty("highlight") final Color highlight, @JsonProperty("hover") final Color hover)
    {
        this.border = border;
        this.background = background;
        this.highlight = highlight;
        this.hover = hover;
    }


    public String getBorder()
    {
        return border;
    }


    public String getBackground()
    {
        return background;
    }


    public Color getHighlight()
    {
        return highlight;
    }


    public Color getHover()
    {
        return hover;
    }


    public static class Builder
    {
        private String border;
        private String background;
        private Color highlight;
        private Color hover;


        /**
         * Represents border color when node is not selected. Default value is #2B7CE9.
         */
        public Builder withBorder(final String border)
        {
            this.border = border;
            return this;
        }


        /**
         * Represents background color when node is not selected. Default value is #2B7CE9.
         */
        public Builder withBackground(final String background)
        {
            this.background = background;
            return this;
        }


        /**
         * Represents color information when node is selected.
         */
        public Builder withHighlight(final Color highlight)
        {
            this.highlight = highlight;
            return this;
        }


        /**
         * Represents color information when mouse is over a node.
         */
        public Builder withHover(final Color hover)
        {
            this.hover = hover;
            return this;
        }


        public NodeColor build()
        {
            return new NodeColor(border, background, highlight, hover);
        }
    }
}
