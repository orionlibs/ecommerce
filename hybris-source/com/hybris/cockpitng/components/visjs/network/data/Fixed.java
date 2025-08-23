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
 * Options for fixing node position while physics simulation.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Fixed implements Serializable
{
    /**
     *  If set to true, a node will not be moved in X direction. Default value is false.
     */
    private final Boolean x;
    /**
     * If set to true, a node will not be moved in Y direction. Default value is false.
     */
    private final Boolean y;


    @JsonCreator
    protected Fixed(@JsonProperty("x") final Boolean x, @JsonProperty("y") final Boolean y)
    {
        this.x = x;
        this.y = y;
    }


    public Boolean getX()
    {
        return x;
    }


    public Boolean getY()
    {
        return y;
    }


    public static class Builder
    {
        private Boolean x;
        private Boolean y;


        public Builder withX(final Boolean x)
        {
            this.x = x;
            return this;
        }


        public Builder withY(final Boolean y)
        {
            this.y = y;
            return this;
        }


        public Fixed build()
        {
            return new Fixed(x, y);
        }
    }
}
