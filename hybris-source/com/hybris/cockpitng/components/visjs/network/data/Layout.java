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
 * Options for network layout.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Layout implements Serializable
{
    /**
     * If hierarchical layout is not used then nodes are randomly positioned on canvas. If the seed is set then nodes
     * will be drawn always in the same position. Default value is null.
     */
    private final Integer randomSeed;
    /**
     * Indicates whether Kamada Kawai's algorithm should be used. Default value is true.
     */
    private final Boolean improvedLayout;
    /**
     * Options for hierarchical layout.
     */
    private final Hierarchical hierarchical;


    @JsonCreator
    public Layout(@JsonProperty("randomSeed") final Integer randomSeed,
                    @JsonProperty("improvedLayout") final Boolean improvedLayout,
                    @JsonProperty("hierarchical") final Hierarchical hierarchical)
    {
        this.randomSeed = randomSeed;
        this.improvedLayout = improvedLayout;
        this.hierarchical = hierarchical;
    }


    public Integer getRandomSeed()
    {
        return randomSeed;
    }


    public Boolean getImprovedLayout()
    {
        return improvedLayout;
    }


    public Hierarchical getHierarchical()
    {
        return hierarchical;
    }


    public static class Builder
    {
        private Integer randomSeed;
        private Boolean improvedLayout;
        private Hierarchical hierarchical;


        /**
         * If hierarchical layout is not used then nodes are randomly positioned on canvas. If the seed is set then nodes
         * will be drawn always in the same position. Default value is null.
         */
        public Builder withRandomSeed(final Integer randomSeed)
        {
            this.randomSeed = randomSeed;
            return this;
        }


        /**
         * Indicates whether Kamada Kawai's algorithm should be used. Default value is true.
         */
        public Builder withImprovedLayout(final Boolean improvedLayout)
        {
            this.improvedLayout = improvedLayout;
            return this;
        }


        /**
         * Options for hierarchical layout.
         */
        public Builder withHierarchical(final Hierarchical hierarchical)
        {
            this.hierarchical = hierarchical;
            return this;
        }


        public Layout build()
        {
            return new Layout(randomSeed, improvedLayout, hierarchical);
        }
    }
}
