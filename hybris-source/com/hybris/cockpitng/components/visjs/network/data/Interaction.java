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
 * Represents options for navigation buttons, mouse and touch events.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Interaction implements Serializable
{
    /**
     * Indicates whether nodes that are not fixed can be dragged. Default value is true.
     */
    private final Boolean dragNodes;
    /**
     * Indicates whether view can be dragged. Default value is true.
     */
    private final Boolean dragView;
    /**
     * Indicates whether edges should be hide while dragging. Default value is false.
     */
    private final Boolean hideEdgesOnDrag;
    /**
     * Indicates whether nodes should be hide while dragging. Default value is false.
     */
    private final Boolean hideNodesOnDrag;
    /**
     * Indicates whether nodes should use their hover colors on a moveover event. Default value is false.
     */
    private final Boolean hover;
    /**
     * Indicates whether on hovering over a node its edges should be highlighted. Default value is true.
     */
    private final Boolean hoverConnectedEdges;
    /**
     * Enables and configures keyboard shortcuts for network.
     */
    private final Keyboard keyboard;
    /**
     * Indicates whether multiselect should be possible. Default value is false.
     */
    private final Boolean multiselect;
    /**
     * Indicates whether navigation buttons should be visible on a canvas. Default value is false.
     */
    private final Boolean navigationButtons;
    /**
     * Indicates whether nodes and edges can be selected by user. Default value is true.
     */
    private final Boolean selectable;
    /**
     * Indicates whether selection on a node should highlight its edges. Default value is true.
     */
    private final Boolean selectConnectedEdges;
    /**
     * Indicates delay of showing tooltip for edges or nodes. Edges or nodes have to have defined 'title' property.
     * Default value is 300.
     */
    private final Integer tooltipDelay;
    /**
     * Indicates whether zooming is possible. Default value is true.
     */
    private final Boolean zoomView;


    @JsonCreator
    protected Interaction(@JsonProperty("dragNodes") final Boolean dragNodes, @JsonProperty("dragView") final Boolean dragView,
                    @JsonProperty("hideEdgesOnDrag") final Boolean hideEdgesOnDrag,
                    @JsonProperty("hideNodesOnDrag") final Boolean hideNodesOnDrag, @JsonProperty("hover") final Boolean hover,
                    @JsonProperty("hoverConnectedEdges") final Boolean hoverConnectedEdges,
                    @JsonProperty("keyboard") final Keyboard keyboard, @JsonProperty("multiselect") final Boolean multiselect,
                    @JsonProperty("navigationButtons") final Boolean navigationButtons, @JsonProperty("selectable") final Boolean selectable,
                    @JsonProperty("selectConnectedEdges") final Boolean selectConnectedEdges,
                    @JsonProperty("tooltipDelay") final Integer tooltipDelay, @JsonProperty("zoomView") final Boolean zoomView)
    {
        this.dragNodes = dragNodes;
        this.dragView = dragView;
        this.hideEdgesOnDrag = hideEdgesOnDrag;
        this.hideNodesOnDrag = hideNodesOnDrag;
        this.hover = hover;
        this.hoverConnectedEdges = hoverConnectedEdges;
        this.keyboard = keyboard;
        this.multiselect = multiselect;
        this.navigationButtons = navigationButtons;
        this.selectable = selectable;
        this.selectConnectedEdges = selectConnectedEdges;
        this.tooltipDelay = tooltipDelay;
        this.zoomView = zoomView;
    }


    public Boolean getDragNodes()
    {
        return dragNodes;
    }


    public Boolean getDragView()
    {
        return dragView;
    }


    public Boolean getHideEdgesOnDrag()
    {
        return hideEdgesOnDrag;
    }


    public Boolean getHideNodesOnDrag()
    {
        return hideNodesOnDrag;
    }


    public Boolean getHover()
    {
        return hover;
    }


    public Boolean getHoverConnectedEdges()
    {
        return hoverConnectedEdges;
    }


    public Keyboard getKeyboard()
    {
        return keyboard;
    }


    public Boolean getMultiselect()
    {
        return multiselect;
    }


    public Boolean getNavigationButtons()
    {
        return navigationButtons;
    }


    public Boolean getSelectable()
    {
        return selectable;
    }


    public Boolean getSelectConnectedEdges()
    {
        return selectConnectedEdges;
    }


    public Integer getTooltipDelay()
    {
        return tooltipDelay;
    }


    public Boolean getZoomView()
    {
        return zoomView;
    }


    public static class Builder
    {
        private Boolean dragNodes;
        private Boolean dragView;
        private Boolean hideEdgesOnDrag;
        private Boolean hideNodesOnDrag;
        private Boolean hover;
        private Boolean hoverConnectedEdges;
        private Keyboard keyboard;
        private Boolean multiselect;
        private Boolean navigationButtons;
        private Boolean selectable;
        private Boolean selectConnectedEdges;
        private Integer tooltipDelay;
        private Boolean zoomView;


        /**
         * Indicates whether nodes that are not fixed can be dragged. Default value is true.
         */
        public Builder withDragNodes(final Boolean dragNodes)
        {
            this.dragNodes = dragNodes;
            return this;
        }


        /**
         * Indicates whether view can be dragged. Default value is true.
         */
        public Builder withDragView(final Boolean dragView)
        {
            this.dragView = dragView;
            return this;
        }


        /**
         * Indicates whether edges should be hide while dragging. Default value is false.
         */
        public Builder withHideEdgesOnDrag(final Boolean hideEdgesOnDrag)
        {
            this.hideEdgesOnDrag = hideEdgesOnDrag;
            return this;
        }


        /**
         * Indicates whether nodes should be hide while dragging. Default value is false.
         */
        public Builder withHideNodesOnDrag(final Boolean hideNodesOnDrag)
        {
            this.hideNodesOnDrag = hideNodesOnDrag;
            return this;
        }


        /**
         * Indicates whether nodes should use their hover colors on a moveover event. Default value is false.
         */
        public Builder withHover(final Boolean hover)
        {
            this.hover = hover;
            return this;
        }


        /**
         * Indicates whether on hovering over a node its edges should be highlighted. Default value is true.
         */
        public Builder withHoverConnectedEdges(final Boolean hoverConnectedEdges)
        {
            this.hoverConnectedEdges = hoverConnectedEdges;
            return this;
        }


        /**
         * Enables and configures keyboard shortcuts for network
         */
        public Builder withKeyboard(final Keyboard keyboard)
        {
            this.keyboard = keyboard;
            return this;
        }


        /**
         * Indicates whether multiselect should be possible. Default value is false.
         */
        public Builder withMultiselect(final Boolean multiselect)
        {
            this.multiselect = multiselect;
            return this;
        }


        /**
         * Indicates whether navigation buttons should be visible on a canvas. Default value is false.
         */
        public Builder withNavigationButtons(final Boolean navigationButtons)
        {
            this.navigationButtons = navigationButtons;
            return this;
        }


        /**
         * Indicates whether nodes and edges can be selected by user. Default value is true.
         */
        public Builder withSelectable(final Boolean selectable)
        {
            this.selectable = selectable;
            return this;
        }


        /**
         * Indicates whether selection on a node should highlight its edges. Default value is true.
         */
        public Builder withSelectConnectedEdges(final Boolean selectConnectedEdges)
        {
            this.selectConnectedEdges = selectConnectedEdges;
            return this;
        }


        /**
         * Indicates delay of showing tooltip for edges or nodes. Edges or nodes have to have defined 'title' property.
         * Default value is 300.
         */
        public Builder withZoomView(final Boolean zoomView)
        {
            this.zoomView = zoomView;
            return this;
        }


        /**
         * Indicates whether zooming is possible. Default value is true.
         */
        public Builder withTooltipDelay(final Integer tooltipDelay)
        {
            this.tooltipDelay = tooltipDelay;
            return this;
        }


        public Interaction build()
        {
            return new Interaction(dragNodes, dragView, hideEdgesOnDrag, hideNodesOnDrag, hover, hoverConnectedEdges, keyboard,
                            multiselect, navigationButtons, selectable, selectConnectedEdges, tooltipDelay, zoomView);
        }
    }
}
