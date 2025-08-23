/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents image and circular image when These options are taken into account when {@link Node#shape} is set to
 * 'image' or "circularImage".
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Image implements Serializable
{
    /**
     * Url to image when node is unselected. Default value is null.
     */
    private final String unselected;
    /**
     * Url to image when node is elected. Default value is null.
     */
    private final String selected;


    @JsonCreator
    public Image(@JsonProperty("unselected") final String unselected, @JsonProperty("selected") final String selected)
    {
        this.unselected = unselected;
        this.selected = selected;
    }


    public String getUnselected()
    {
        return unselected;
    }


    public String getSelected()
    {
        return selected;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final Image image = (Image)o;
        return Objects.equals(unselected, image.unselected) && Objects.equals(selected, image.selected);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(unselected, selected);
    }


    public static class Builder
    {
        private String unselected;
        private String selected;


        /**
         * Url to image when node is unselected. Default value is null.
         */
        public Builder withUnselected(final String unselected)
        {
            this.unselected = unselected;
            return this;
        }


        /**
         * Url to image when node is elected. Default value is null.
         */
        public Builder withSelected(final String selected)
        {
            this.selected = selected;
            return this;
        }


        public Image build()
        {
            return new Image(unselected, selected);
        }
    }
}
