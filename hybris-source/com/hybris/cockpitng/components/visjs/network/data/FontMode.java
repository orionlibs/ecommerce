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
 * Represents details for font decoration (bold, ital, boldital, mono).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FontMode implements Serializable
{
    /**
     * Color of the font decoration of a label. Default value is color of base font declaration.
     */
    private final String color;
    /**
     * Size of the font decoration of a label. Default value is size of base font declaration.
     */
    private final Integer size;
    /**
     * Font face of the font decoration of a label. Default value is font face of base font declaration.
     */
    private final String face;
    /**
     * A string added to the face and size to determining a font decoration. Possible values are bold, italic.
     */
    private final String mod;
    /**
     * Font specific correction of the vertical positioning. Default value is 0.
     */
    private final String vadjust;


    @JsonCreator
    protected FontMode(@JsonProperty("color") final String color, @JsonProperty("size") final Integer size,
                    @JsonProperty("face") final String face, @JsonProperty("mod") final String mod,
                    @JsonProperty("vadjust") final String vadjust)
    {
        this.color = color;
        this.size = size;
        this.face = face;
        this.mod = mod;
        this.vadjust = vadjust;
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


    public String getMod()
    {
        return mod;
    }


    public String getVadjust()
    {
        return vadjust;
    }


    public static class Builder
    {
        private String color;
        private Integer size;
        private String face;
        private String mod;
        private String vadjust;


        /**
         * Color of the font decoration of label. Default value is color of base font declaration.
         */
        public Builder withColor(final String color)
        {
            this.color = color;
            return this;
        }


        /**
         * Size of the font decoration of a label. Default value is size of base font declaration.
         */
        public Builder withSize(final Integer size)
        {
            this.size = size;
            return this;
        }


        /**
         * Font face of the font decoration of a label. Default value is font face of base font declaration.
         */
        public Builder withFace(final String face)
        {
            this.face = face;
            return this;
        }


        /**
         * A string added to the face and size to determining a font decoration. Possible values are bold, italic.
         */
        public Builder withMod(final String mod)
        {
            this.mod = mod;
            return this;
        }


        /**
         * Font specific correction of the vertical positioning. Default value is 0.
         */
        public Builder withVadjust(final String vadjust)
        {
            this.vadjust = vadjust;
            return this;
        }


        public FontMode build()
        {
            return new FontMode(color, size, face, mod, vadjust);
        }
    }
}
