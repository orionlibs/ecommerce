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
 * Represents options for label's font.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Font implements Serializable
{
    /**
     * Color of label text. Default value is #343434.
     */
    private final String color;
    /**
     * Size of label text. Default value is 14.
     */
    private final Integer size;
    /**
     * Font face of label text. Default value is arial.
     */
    private final String face;
    /**
     * When color's value is provided then background rectangle will be drawn behind a label in the given color. Default
     * value is null.
     */
    private final String background;
    /**
     * Alternative for background rectangle. If positive value is provided then a stroke will be drawn. Default value is
     * 0.
     */
    private final Integer strokeWidth;
    /**
     * A color of stroke, assuming that strokeWidth is positive. Default value is #ffffff.
     */
    private final String strokeColor;
    /**
     * Align of a label. Possible values are "left" and "center". Default value is center.
     */
    private final String align;
    /**
     * Font specific correction of the vertical positioning. Default value is 0
     */
    private final String vadjust;
    /**
     * If false then label is treated as a pure text. If true then label may have multi-font, with bold, italic, and it
     * will be interpreted as html. Default value is false.
     */
    private final Boolean multi;
    /**
     * Represents details for font decoration: bold.
     */
    private final FontMode bold;
    /**
     * Represents details for font decoration: ital.
     */
    private final FontMode ital;
    /**
     * Represents details for font decoration: boldital.
     */
    private final FontMode boldital;
    /**
     * Represents details for  monospaced font decoration.
     */
    private final FontMode mono;


    @JsonCreator
    protected Font(@JsonProperty("color") final String color, @JsonProperty("size") final Integer size,
                    @JsonProperty("face") final String face, @JsonProperty("background") final String background,
                    @JsonProperty("strokeWidth") final Integer strokeWidth, @JsonProperty("strokeColor") final String strokeColor,
                    @JsonProperty("align") final String align, @JsonProperty("vadjust") final String vadjust,
                    @JsonProperty("multi") final Boolean multi, @JsonProperty("bold") final FontMode bold,
                    @JsonProperty("ital") final FontMode ital, @JsonProperty("boldital") final FontMode boldital,
                    @JsonProperty("mono") final FontMode mono)
    {
        this.color = color;
        this.size = size;
        this.face = face;
        this.background = background;
        this.strokeWidth = strokeWidth;
        this.strokeColor = strokeColor;
        this.align = align;
        this.vadjust = vadjust;
        this.multi = multi;
        this.bold = bold;
        this.ital = ital;
        this.boldital = boldital;
        this.mono = mono;
    }


    public String getColor()
    {
        return color;
    }


    public Integer getSize()
    {
        return size;
    }


    public String getFace()
    {
        return face;
    }


    public String getBackground()
    {
        return background;
    }


    public Integer getStrokeWidth()
    {
        return strokeWidth;
    }


    public String getStrokeColor()
    {
        return strokeColor;
    }


    public String getAlign()
    {
        return align;
    }


    public String getVadjust()
    {
        return vadjust;
    }


    public Boolean getMulti()
    {
        return multi;
    }


    public FontMode getBold()
    {
        return bold;
    }


    public FontMode getItal()
    {
        return ital;
    }


    public FontMode getBoldital()
    {
        return boldital;
    }


    public FontMode getMono()
    {
        return mono;
    }


    public static class Builder
    {
        private String color;
        private Integer size;
        private String face;
        private String background;
        private Integer strokeWidth;
        private String strokeColor;
        private String align;
        private String vadjust;
        private Boolean multi;
        private FontMode bold;
        private FontMode ital;
        private FontMode boldital;
        private FontMode mono;


        /**
         * Color of label text. Default value is #343434.
         */
        public Builder withColor(final String color)
        {
            this.color = color;
            return this;
        }


        /**
         * Size of label text. Default value is 14.
         */
        public Builder withSize(final Integer size)
        {
            this.size = size;
            return this;
        }


        /**
         * Font face of label text. Default value is arial.
         */
        public Builder withFace(final String face)
        {
            this.face = face;
            return this;
        }


        /**
         * When color's value is provided then background rectangle will be drawn behind a label in the given color.
         * Default value is null.
         */
        public Builder withBackground(final String background)
        {
            this.background = background;
            return this;
        }


        /**
         * Alternative for background rectangle. If positive value is provided then a stroke will be drawn. Default value
         * is 0.
         */
        public Builder withStrokeWidth(final Integer strokeWidth)
        {
            this.strokeWidth = strokeWidth;
            return this;
        }


        /**
         * A color of stroke, assuming that strokeWidth is positive. Default value is #ffffff.
         */
        public Builder withStrokeColor(final String strokeColor)
        {
            this.strokeColor = strokeColor;
            return this;
        }


        /**
         * Align of a label. Possible values are "left" and "center". Default value is center.
         */
        public Builder withAlign(final String align)
        {
            this.align = align;
            return this;
        }


        /**
         * Font specific correction of the vertical positioning. Default value is 0
         */
        public Builder withVadjust(final String vadjust)
        {
            this.vadjust = vadjust;
            return this;
        }


        /**
         * If false then label is treated as a pure text. If true then label may have multi-font, with bold, italic, and
         * it will be interpreted as html. Default value is false.
         */
        public Builder withMulti(final Boolean multi)
        {
            this.multi = multi;
            return this;
        }


        /**
         * Represents details for font decoration: bold.
         */
        public Builder withBold(final FontMode bold)
        {
            this.bold = bold;
            return this;
        }


        /**
         * Represents details for font decoration: ital.
         */
        public Builder withItal(final FontMode ital)
        {
            this.ital = ital;
            return this;
        }


        /**
         * Represents details for font decoration: boldital.
         */
        public Builder withBoldital(final FontMode boldital)
        {
            this.boldital = boldital;
            return this;
        }


        /**
         * Represents details for  monospaced font decoration.
         */
        public Builder withMono(final FontMode mono)
        {
            this.mono = mono;
            return this;
        }


        public Font build()
        {
            return new Font(color, size, face, background, strokeWidth, strokeColor, align, vadjust, multi, bold, ital, boldital,
                            mono);
        }
    }
}
