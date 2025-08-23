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
 * Represents margins of labels. These options are only taken into account when {@link Node#shape} is set to box,
 * circle, database, icon or text.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Margin implements Serializable
{
    /**
     * Top margin of label. Default value is 5.
     */
    private final Integer top;
    /**
     * Right margin of label. Default value is 5.
     */
    private final Integer right;
    /**
     * Bottom margin of label. Default value is 5.
     */
    private final Integer bottom;
    /**
     * Left margin of label. Default value is 5.
     */
    private final Integer left;


    @JsonCreator
    public Margin(@JsonProperty("top") final Integer top, @JsonProperty("right") final Integer right,
                    @JsonProperty("bottom") final Integer bottom, @JsonProperty("left") final Integer left)
    {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }


    public Integer getTop()
    {
        return top;
    }


    public Integer getRight()
    {
        return right;
    }


    public Integer getBottom()
    {
        return bottom;
    }


    public Integer getLeft()
    {
        return left;
    }


    public static class Builder
    {
        private Integer top;
        private Integer right;
        private Integer bottom;
        private Integer left;


        /**
         * Top margin of label. Default value is 5.
         */
        public Builder withTop(final Integer top)
        {
            this.top = top;
            return this;
        }


        /**
         * Right margin of label. Default value is 5.
         */
        public Builder withRight(final Integer right)
        {
            this.right = right;
            return this;
        }


        /**
         * Bottom margin of label. Default value is 5.
         */
        public Builder withBottom(final Integer bottom)
        {
            this.bottom = bottom;
            return this;
        }


        /**
         * Left margin of label. Default value is 5.
         */
        public Builder withLeft(final Integer left)
        {
            this.left = left;
            return this;
        }


        public Margin build()
        {
            return new Margin(top, right, bottom, left);
        }
    }
}
