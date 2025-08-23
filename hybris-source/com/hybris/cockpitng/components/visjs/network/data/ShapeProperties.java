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
 * Represents configuration for specific shapes.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ShapeProperties implements Serializable
{
    /**
     * Possible values are 'true', 'false' or array with format [dash length, gap length]. Default value is false. The
     * configuration applied to all shapes with border.
     */
    private final Serializable borderDashes;
    /**
     * Indicates a roundness of corners. Possible only for 'box' shape. Default value is 6.
     */
    private final Integer borderRadius;
    /**
     * Indicates whether image should be resampled when scalled down. Taking into account only for 'image' and
     * 'circularImage' shape. Default value is true.
     */
    private final Boolean interpolation;
    /**
     * When set to true then size of image is used. When false then size option is used. Taking into account only for
     * 'image' and 'circularImage' shape. Default value is false.
     */
    private final Boolean useImageSize;
    /**
     * When true then color object is used. aking into account only for 'image' shape. Default value is false.
     */
    private final Boolean useBorderWithImage;


    @JsonCreator
    protected ShapeProperties(@JsonProperty("borderDashes") final Serializable borderDashes,
                    @JsonProperty("borderRadius") final Integer borderRadius, @JsonProperty("interpolation") final Boolean interpolation,
                    @JsonProperty("useImageSize") final Boolean useImageSize,
                    @JsonProperty("useBorderWithImage") final Boolean useBorderWithImage)
    {
        this.borderDashes = borderDashes;
        this.borderRadius = borderRadius;
        this.interpolation = interpolation;
        this.useImageSize = useImageSize;
        this.useBorderWithImage = useBorderWithImage;
    }


    public String getBorderDashes()
    {
        return String.valueOf(borderDashes);
    }


    public Integer getBorderRadius()
    {
        return borderRadius;
    }


    public Boolean getInterpolation()
    {
        return interpolation;
    }


    public Boolean getUseImageSize()
    {
        return useImageSize;
    }


    public Boolean getUseBorderWithImage()
    {
        return useBorderWithImage;
    }


    public static class Builder
    {
        private Serializable borderDashes;
        private Integer borderRadius;
        private Boolean interpolation;
        private Boolean useImageSize;
        private Boolean useBorderWithImage;


        /**
         * Possible values are 'true', 'false' or array with format [dash length, gap length]. Default value is false. The
         * configuration applied to all shapes with border.
         */
        public Builder withBorderDashes(final String borderDashes)
        {
            this.borderDashes = borderDashes;
            return this;
        }


        /**
         * Indicates a roundness of corners. Possible only for 'box' shape. Default value is 6.
         */
        public Builder withBorderRadius(final Integer borderRadius)
        {
            this.borderRadius = borderRadius;
            return this;
        }


        /**
         * Indicates whether image should be resampled when scalled down. Taking into account only for 'image' and
         * 'circularImage' shape. Default value is true.
         */
        public Builder withInterpolation(final Boolean interpolation)
        {
            this.interpolation = interpolation;
            return this;
        }


        /**
         * When set to true then size of image is used. When false then size option is used. Taking into account only for
         * 'image' and 'circularImage' shape. Default value is false.
         */
        public Builder withUseImageSize(final Boolean useImageSize)
        {
            this.useImageSize = useImageSize;
            return this;
        }


        /**
         * When true then color object is used. aking into account only for 'image' shape. Default value is false.
         */
        public Builder withUseBorderWithImage(final Boolean useBorderWithImage)
        {
            this.useBorderWithImage = useBorderWithImage;
            return this;
        }


        public ShapeProperties build()
        {
            return new ShapeProperties(borderDashes, borderRadius, interpolation, useImageSize, useBorderWithImage);
        }
    }
}
