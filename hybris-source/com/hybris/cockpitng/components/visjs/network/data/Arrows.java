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
 * Represents options for arrowhead. By using this class you can indicate position of arrowhead on the edge
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Arrows implements Serializable
{
    /**
     * Options for an arrowhead on the 'to' side of the edge. Default value is null.
     */
    private final Arrow to;
    /**
     * Options for an arrowhead on the 'center' of the edge. Default value is null.
     */
    private final Arrow middle;
    /**
     * Options for an arrowhead on the 'from' side of the edge. Default value is null.
     */
    private final Arrow from;


    @JsonCreator
    protected Arrows(@JsonProperty("to") final Arrow to, @JsonProperty("middle") final Arrow middle,
                    @JsonProperty("from") final Arrow from)
    {
        this.to = to;
        this.middle = middle;
        this.from = from;
    }


    public Arrow getTo()
    {
        return to;
    }


    public Arrow getMiddle()
    {
        return middle;
    }


    public Arrow getFrom()
    {
        return from;
    }


    public static class Builder
    {
        private Arrow to;
        private Arrow middle;
        private Arrow from;


        /**
         * Options for an arrowhead on the 'to' side of the edge. Default value is null
         */
        public Builder withTo(final Arrow to)
        {
            this.to = to;
            return this;
        }


        /**
         * Options for an arrowhead on the 'center' of the edge. Default value is null
         */
        public Builder withMiddle(final Arrow middle)
        {
            this.middle = middle;
            return this;
        }


        /**
         * Options for an arrowhead on the 'from' side of the edge. Default value is null
         */
        public Builder withFrom(final Arrow from)
        {
            this.from = from;
            return this;
        }


        public Arrows build()
        {
            return new Arrows(to, middle, from);
        }
    }
}
