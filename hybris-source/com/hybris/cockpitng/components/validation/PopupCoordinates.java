/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.validation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class PopupCoordinates implements Serializable
{
    private final Dimension horizontal;
    private final Dimension vertical;


    @JsonCreator
    public PopupCoordinates(@JsonProperty("horizontal") final Dimension horizontal,
                    @JsonProperty("vertical") final Dimension vertical)
    {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }


    public Dimension getHorizontal()
    {
        return horizontal;
    }


    public Dimension getVertical()
    {
        return vertical;
    }


    public static class Dimension implements Serializable
    {
        private boolean inverted;
        private int transition;


        @JsonCreator
        public Dimension(@JsonProperty("inverted") final boolean inverted, @JsonProperty("transition") final int transition)
        {
            this.inverted = inverted;
            this.transition = transition;
        }


        public boolean isInverted()
        {
            return inverted;
        }


        public void setInverted(final boolean inverted)
        {
            this.inverted = inverted;
        }


        public int getTransition()
        {
            return transition;
        }


        public void setTransition(final int transition)
        {
            this.transition = transition;
        }
    }
}
