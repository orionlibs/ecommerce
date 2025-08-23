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
 * Represents options for icon. These options are taken into account when {@link Node#shape} is set to 'icon'.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Icon implements Serializable
{
    /**
     * Represents font face. Possible values are "FontAwesome" and "Ionicons". Default value is "FontAwesome".
     */
    private final String face;
    /**
     * Code which represent an icon for given font face. Default value is null.
     */
    private final String code;
    /**
     * Size of an icon. Default value is 50.
     */
    private final Integer size;
    /**
     * Color of an icon. Default value is #2B7CE9.
     */
    private final String color;


    @JsonCreator
    protected Icon(@JsonProperty("face") final String face, @JsonProperty("code") final String code,
                    @JsonProperty("size") final Integer size, @JsonProperty("color") final String color)
    {
        this.face = face;
        this.code = code;
        this.size = size;
        this.color = color;
    }


    public String getFace()
    {
        return face;
    }


    public String getCode()
    {
        return code;
    }


    public Integer getSize()
    {
        return size;
    }


    public String getColor()
    {
        return color;
    }


    public static class Builder
    {
        private String face;
        private String code;
        private Integer size;
        private String color;


        /**
         * Represents font face. Possible values are "FontAwesome" and "Ionicons". Default value is "FontAwesome".
         */
        public Builder withFace(final String face)
        {
            this.face = face;
            return this;
        }


        /**
         * Code which represent an icon for given font face. Default value is null.
         */
        public Builder withCode(final String code)
        {
            this.code = code;
            return this;
        }


        /**
         * Size of an icon. Default value is 50.
         */
        public Builder withSize(final Integer size)
        {
            this.size = size;
            return this;
        }


        /**
         * Color of an icon. Default value is #2B7CE9.
         */
        public Builder withColor(final String color)
        {
            this.color = color;
            return this;
        }


        public Icon build()
        {
            return new Icon(face, code, size, color);
        }
    }
}
