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
 * Enables and configures keyboard shortcuts for network
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Keyboard implements Serializable
{
    /**
     * Enables a usage of the keyboard shortcuts. Default value is false.
     */
    private final Boolean enabled;
    /**
     * A speed of view on pressing a key or using navigation buttons.
     */
    private final Speed speed;
    /**
     * If set to true, the keyboard interaction will work regardless of which DOM object has the focus.
     */
    private final Boolean bindToWindow;


    @JsonCreator
    protected Keyboard(@JsonProperty("enabled") final Boolean enabled, @JsonProperty("speed") final Speed speed,
                    @JsonProperty("bindToWindow") final Boolean bindToWindow)
    {
        this.enabled = enabled;
        this.speed = speed;
        this.bindToWindow = bindToWindow;
    }


    public Boolean getEnabled()
    {
        return enabled;
    }


    public Speed getSpeed()
    {
        return speed;
    }


    public Boolean getBindToWindow()
    {
        return bindToWindow;
    }


    public static class Builder
    {
        private Boolean enabled;
        private Speed speed;
        private Boolean bindToWindow;


        /**
         * Enables a usage of the keyboard shortcuts. Default value is false
         */
        public Builder withEnabled(final Boolean enabled)
        {
            this.enabled = enabled;
            return this;
        }


        /**
         * A speed of view on pressing a key or using navigation buttons
         */
        public Builder withSpeed(final Speed speed)
        {
            this.speed = speed;
            return this;
        }


        /**
         * If set to true, the keyboard interaction will work regardless of which DOM object has the focus.
         */
        public Builder withBindToWindow(final Boolean bindToWindow)
        {
            this.bindToWindow = bindToWindow;
            return this;
        }


        public Keyboard build()
        {
            return new Keyboard(enabled, speed, bindToWindow);
        }
    }
}
