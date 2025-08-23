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
 * Options for hierarchical layout.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Hierarchical implements Serializable
{
    /**
     * Enables hierarchical layout. Default value is false.
     */
    private final Boolean enabled;
    /**
     * Indicates distance between levels. Default value is 150.
     */
    private final Integer levelSeparation;
    /**
     * Minimum distance between nodes. Default value is 100.
     */
    private final Integer nodeSpacing;
    /**
     * Minimum distance between different subtrees. Default value is 200.
     */
    private final Integer treeSpacing;
    /**
     * Indicates whether whitespaces should be reduced. Default value is true.
     */
    private final Boolean blockShifting;
    /**
     * Indicates whether whitespaces should be reduced. Default value is true.
     */
    private final Boolean edgeMinimization;
    /**
     * Indicates whether parents nodes should be centered after simulation is finished. Default value is true.
     */
    private final Boolean parentCentralization;
    /**
     * Direction of hierarchical layout. Possible values are UD, DU, LR and RL (up-down, down-up, left-right,
     * right-left). Default value is UD.
     */
    private final String direction;
    /**
     * Indicates algorithm which should be used. Possible values are hubsize and directed. Default value is hubsize.
     */
    private final String sortMethod;


    @JsonCreator
    protected Hierarchical(@JsonProperty("enabled") final Boolean enabled,
                    @JsonProperty("levelSeparation") final Integer levelSeparation, @JsonProperty("nodeSpacing") final Integer nodeSpacing,
                    @JsonProperty("treeSpacing") final Integer treeSpacing, @JsonProperty("blockShifting") final Boolean blockShifting,
                    @JsonProperty("edgeMinimization") final Boolean edgeMinimization,
                    @JsonProperty("parentCentralization") final Boolean parentCentralization,
                    @JsonProperty("direction") final String direction, @JsonProperty("sortMethod") final String sortMethod)
    {
        this.enabled = enabled;
        this.levelSeparation = levelSeparation;
        this.nodeSpacing = nodeSpacing;
        this.treeSpacing = treeSpacing;
        this.blockShifting = blockShifting;
        this.edgeMinimization = edgeMinimization;
        this.parentCentralization = parentCentralization;
        this.direction = direction;
        this.sortMethod = sortMethod;
    }


    public Boolean getEnabled()
    {
        return enabled;
    }


    public Integer getLevelSeparation()
    {
        return levelSeparation;
    }


    public Integer getNodeSpacing()
    {
        return nodeSpacing;
    }


    public Integer getTreeSpacing()
    {
        return treeSpacing;
    }


    public Boolean getBlockShifting()
    {
        return blockShifting;
    }


    public Boolean getEdgeMinimization()
    {
        return edgeMinimization;
    }


    public Boolean getParentCentralization()
    {
        return parentCentralization;
    }


    public String getDirection()
    {
        return direction;
    }


    public String getSortMethod()
    {
        return sortMethod;
    }


    public static class Builder
    {
        private Boolean enabled;
        private Integer levelSeparation;
        private Integer nodeSpacing;
        private Integer treeSpacing;
        private Boolean blockShifting;
        private Boolean edgeMinimization;
        private Boolean parentCentralization;
        private String direction;
        private String sortMethod;


        /**
         * Enables hierarchical layout. Default value is false.
         */
        public Builder withEnabled(final Boolean enabled)
        {
            this.enabled = enabled;
            return this;
        }


        /**
         * Indicates distance between levels. Default value is 150.
         */
        public Builder withLevelSeparation(final Integer levelSeparation)
        {
            this.levelSeparation = levelSeparation;
            return this;
        }


        /**
         * Minimum distance between nodes. Default value is 100.
         */
        public Builder withNodeSpacing(final Integer nodeSpacing)
        {
            this.nodeSpacing = nodeSpacing;
            return this;
        }


        /**
         * Minimum distance between different subtrees. Default value is 200.
         */
        public Builder withTreeSpacing(final Integer treeSpacing)
        {
            this.treeSpacing = treeSpacing;
            return this;
        }


        /**
         * Indicates whether whitespaces should be reduced. Default value is true.
         */
        public Builder withBlockShifting(final Boolean blockShifting)
        {
            this.blockShifting = blockShifting;
            return this;
        }


        /**
         * Indicates whether whitespaces should be reduced. Default value is true.
         */
        public Builder withEdgeMinimization(final Boolean edgeMinimization)
        {
            this.edgeMinimization = edgeMinimization;
            return this;
        }


        /**
         * Indicates whether parents nodes should be centered after simulation is finished. Default value is true.
         */
        public Builder withParentCentralization(final Boolean parentCentralization)
        {
            this.parentCentralization = parentCentralization;
            return this;
        }


        /**
         * Direction of hierarchical layout. Possible values are UD, DU, LR and RL (up-down, down-up, left-right,
         * right-left). Default value is UD.
         */
        public Builder withDirection(final String direction)
        {
            this.direction = direction;
            return this;
        }


        /**
         * Indicates algorithm which should be used. Possible values are hubsize and directed. Default value is hubsize.
         */
        public Builder withSortMethod(final String sortMethod)
        {
            this.sortMethod = sortMethod;
            return this;
        }


        public Hierarchical build()
        {
            return new Hierarchical(enabled, levelSeparation, nodeSpacing, treeSpacing, blockShifting, edgeMinimization,
                            parentCentralization, direction, sortMethod);
        }
    }
}
