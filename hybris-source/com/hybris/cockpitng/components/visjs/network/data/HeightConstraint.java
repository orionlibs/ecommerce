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
 * Represents options for height of a node.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HeightConstraint implements Serializable
{
    /**
     * Minimum height of a node. Default value is null.
     */
    private final Integer minimum;
    /**
     * Offsets label vertically to the specified position. Possible values are "top", "middle" and "bottom". Default
     * value is "middle".
     */
    private final String valign;


    @JsonCreator
    protected HeightConstraint(@JsonProperty("minimum") final Integer minimum, @JsonProperty("valign") final String valign)
    {
        this.minimum = minimum;
        this.valign = valign;
    }


    public Integer getMinimum()
    {
        return minimum;
    }


    public String getValign()
    {
        return valign;
    }


    public static class Builder
    {
        private Integer minimum;
        private String valign;


        /**
         * Minimum height of a node. Default value is null.
         */
        public Builder withMinimum(final Integer minimum)
        {
            this.minimum = minimum;
            return this;
        }


        /**
         * Offsets label vertically to the specified position. Possible values are "top", "middle" and "bottom". Default
         * value is "middle".
         */
        public Builder withValign(final String valign)
        {
            this.valign = valign;
            return this;
        }


        public HeightConstraint build()
        {
            return new HeightConstraint(minimum, valign);
        }
    }
}
