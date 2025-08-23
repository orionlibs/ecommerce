/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

/**
 * Default configuration for edges.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Edge extends NetworkEntity
{
    /**
     * Represents options for arrowhead. By using this class you can indicate position of arrowhead on the edge
     */
    private final Arrows arrows;
    /**
     * Indicates whether edge should stop at an arrow. Default value is true.
     */
    private final Boolean arrowStrikethrough;
    /**
     * Indicates whether selecting or hovering on an edge should change the edge and/or it's label. If the option is not
     * provided, no change will be applied.
     */
    private final ChosenEdge chosen;
    /**
     * Represents configuration for color of edge.
     */
    private final EdgeColor color;
    /**
     * true, false or array with format [gap length, dash length ...]. Default value is false.
     */
    private final String dashes;
    /**
     * Represents options for label's font.
     */
    private final Font font;
    /**
     * Id of node of 'from' side.
     */
    private final String from;
    /**
     * Node of 'from' side.
     */
    private final Node fromNode;
    /**
     * Indicates whether edge should be hidden. Default value is false.
     */
    private final Boolean hidden;
    /**
     * Number of javaScript's function. Indicates edge width when mouse is over the edge. Function should have the
     * following signature: function(width). Default value is 0.5
     */
    private final String hoverWidth;
    /**
     * Label of an edge. Default value is null.
     */
    private final String label;
    /**
     * Indicates whether label should become bold when node is selected. Default value is true.
     */
    private final Boolean labelHighlightBold;
    /**
     * Indicates length of edge. Default value is null.
     */
    private final Integer length;
    /**
     * Indicates whether edge should be taken into account while physics simulation. Default value is true.
     */
    private final Boolean physics;
    /**
     * Represents option for scaling edges and nodes according to their properties.
     */
    private final Scaling scaling;
    /**
     * Integer or javaScript's function. Indicates width of edge when edge is selected. Function should have the
     * following signature: function(width). Default value is 1.
     */
    private final String selectionWidth;
    /**
     * Indicates radius of circle when 'from' and 'to' points to the same node.
     */
    private final Integer selfReferenceSize;
    /**
     * Represents options for casting a shadow of node and edge.
     */
    private final Shadow shadow;
    /**
     * Indicates whether edge should be drawn as a quadratic bezier curve. Performance of these edges are worse, but they
     * look better.
     */
    private final Smooth smooth;
    /**
     * Title of edge. Default value is null.
     */
    private final String title;
    /**
     * Id of node of 'to' side.
     */
    private final String to;
    /**
     * Node of "to" side.
     */
    private final Node toNode;
    /**
     * Indicates value which is used for scaling. Default value is null.
     */
    private final Integer value;
    /**
     * Indicates width of an edge. Default value is 1.
     */
    private final Integer width;
    /**
     * If value is provided then the maximum width of an edge is set to this value. Default value is null.
     */
    private final Integer widthConstraint;


    @JsonCreator
    protected Edge(@JsonProperty("arrows") final Arrows arrows,
                    @JsonProperty("arrowStrikethrough") final Boolean arrowStrikethrough, @JsonProperty("chosen") final ChosenEdge chosen,
                    @JsonProperty("color") final EdgeColor color, @JsonProperty("dashes") final String dashes,
                    @JsonProperty("font") final Font font, @JsonProperty("from") final String from,
                    @JsonProperty("fromNode") final Node fromNode, @JsonProperty("hidden") final Boolean hidden,
                    @JsonProperty("hoverWidth") final String hoverWidth, @JsonProperty("id") final String id,
                    @JsonProperty("label") final String label, @JsonProperty("labelHighlightBold") final Boolean labelHighlightBold,
                    @JsonProperty("length") final Integer length, @JsonProperty("physics") final Boolean physics,
                    @JsonProperty("scaling") final Scaling scaling, @JsonProperty("selectionWidth") final String selectionWidth,
                    @JsonProperty("selfReferenceSize") final Integer selfReferenceSize, @JsonProperty("shadow") final Shadow shadow,
                    @JsonProperty("smooth") final Smooth smooth, @JsonProperty("title") final String title,
                    @JsonProperty("to") final String to, @JsonProperty("toNode") final Node toNode,
                    @JsonProperty("value") final Integer value, @JsonProperty("width") final Integer width,
                    @JsonProperty("widthConstraint") final Integer widthConstraint, @JsonProperty("data") final Serializable data,
                    @JsonProperty("type") final String type)
    {
        super(id, data, type);
        this.arrows = arrows;
        this.arrowStrikethrough = arrowStrikethrough;
        this.chosen = chosen;
        this.color = color;
        this.dashes = dashes;
        this.font = font;
        this.from = from;
        this.fromNode = fromNode;
        this.hidden = hidden;
        this.hoverWidth = hoverWidth;
        this.label = label;
        this.labelHighlightBold = labelHighlightBold;
        this.length = length;
        this.physics = physics;
        this.scaling = scaling;
        this.selectionWidth = selectionWidth;
        this.selfReferenceSize = selfReferenceSize;
        this.shadow = shadow;
        this.smooth = smooth;
        this.title = title;
        this.to = to;
        this.toNode = toNode;
        this.value = value;
        this.width = width;
        this.widthConstraint = widthConstraint;
    }


    public Arrows getArrows()
    {
        return arrows;
    }


    public Boolean getArrowStrikethrough()
    {
        return arrowStrikethrough;
    }


    public ChosenEdge getChosen()
    {
        return chosen;
    }


    public EdgeColor getColor()
    {
        return color;
    }


    public String getDashes()
    {
        return dashes;
    }


    public Font getFont()
    {
        return font;
    }


    public String getFrom()
    {
        return from;
    }


    public Node getFromNode()
    {
        return fromNode;
    }


    public Boolean getHidden()
    {
        return hidden;
    }


    public String getHoverWidth()
    {
        return hoverWidth;
    }


    public String getLabel()
    {
        return label;
    }


    public Boolean getLabelHighlightBold()
    {
        return labelHighlightBold;
    }


    public Integer getLength()
    {
        return length;
    }


    public Boolean getPhysics()
    {
        return physics;
    }


    public Scaling getScaling()
    {
        return scaling;
    }


    public String getSelectionWidth()
    {
        return selectionWidth;
    }


    public Integer getSelfReferenceSize()
    {
        return selfReferenceSize;
    }


    public Shadow getShadow()
    {
        return shadow;
    }


    public Smooth getSmooth()
    {
        return smooth;
    }


    public String getTitle()
    {
        return title;
    }


    public String getTo()
    {
        return to;
    }


    public Node getToNode()
    {
        return toNode;
    }


    public Integer getValue()
    {
        return value;
    }


    public Integer getWidth()
    {
        return width;
    }


    public Integer getWidthConstraint()
    {
        return widthConstraint;
    }


    public static class Builder
    {
        private Arrows arrows;
        private Boolean arrowStrikethrough;
        private ChosenEdge chosen;
        private EdgeColor color;
        private String dashes;
        private Font font;
        private String from;
        private Node fromNode;
        private Boolean hidden;
        private String hoverWidth;
        private String id;
        private String label;
        private Boolean labelHighlightBold;
        private Integer length;
        private Boolean physics;
        private Scaling scaling;
        private String selectionWidth;
        private Integer selfReferenceSize;
        private Shadow shadow;
        private Smooth smooth;
        private String title;
        private String to;
        private Node toNode;
        private Integer value;
        private Integer width;
        private Integer widthConstraint;
        private Serializable data;


        public Builder(final Node fromNode, final Node toNode)
        {
            this.from = fromNode != null ? fromNode.getId() : null;
            this.to = toNode != null ? toNode.getId() : null;
            this.fromNode = fromNode;
            this.toNode = toNode;
        }


        public Builder(final Edge origin, final Node fromNode, final Node toNode)
        {
            this(origin);
            this.from = fromNode != null ? fromNode.getId() : null;
            this.to = toNode != null ? toNode.getId() : null;
            this.fromNode = fromNode;
            this.toNode = toNode;
        }


        public Builder(final Edge origin)
        {
            this.arrows = origin.arrows;
            this.arrowStrikethrough = origin.arrowStrikethrough;
            this.chosen = origin.chosen;
            this.color = origin.color;
            this.dashes = origin.dashes;
            this.font = origin.font;
            this.from = origin.from;
            this.fromNode = origin.fromNode;
            this.hidden = origin.hidden;
            this.hoverWidth = origin.hoverWidth;
            this.id = StringUtils.isNotBlank(origin.id) ? origin.id : String.format("%s_%s", origin.from, origin.to);
            this.label = origin.label;
            this.labelHighlightBold = origin.labelHighlightBold;
            this.length = origin.length;
            this.physics = origin.physics;
            this.scaling = origin.scaling;
            this.selectionWidth = origin.selectionWidth;
            this.selfReferenceSize = origin.selfReferenceSize;
            this.shadow = origin.shadow;
            this.smooth = origin.smooth;
            this.title = origin.title;
            this.to = origin.to;
            this.toNode = origin.toNode;
            this.value = origin.value;
            this.width = origin.width;
            this.widthConstraint = origin.widthConstraint;
            this.data = origin.data;
        }


        /**
         * Represents options for arrowhead. By using this class you can indicate position of arrowhead on the edge
         */
        public Builder withArrows(final Arrows arrows)
        {
            this.arrows = arrows;
            return this;
        }


        /**
         * Indicates whether edge should stop at an arrow. Default value is true.
         */
        public Builder withArrowStrikethrough(final Boolean arrowStrikethrough)
        {
            this.arrowStrikethrough = arrowStrikethrough;
            return this;
        }


        /**
         * Indicates whether selecting or hovering on an edge should change the edge and/or it's label. If the option is
         * not provided, no change will be applied.
         */
        public Builder withChosen(final ChosenEdge chosen)
        {
            this.chosen = chosen;
            return this;
        }


        /**
         * Represents configuration for color of edge.
         */
        public Builder withColor(final EdgeColor color)
        {
            this.color = color;
            return this;
        }


        /**
         * true, false or array with format [gap length, dash length ...]. Default value is false.
         */
        public Builder withDashes(final String dashes)
        {
            this.dashes = dashes;
            return this;
        }


        /**
         * Represents options for label's font.
         */
        public Builder withFont(final Font font)
        {
            this.font = font;
            return this;
        }


        /**
         * Indicates whether edge should be hidden. Default value is false.
         */
        public Builder withHidden(final Boolean hidden)
        {
            this.hidden = hidden;
            return this;
        }


        /**
         * Number of javaScript's function. Indicates edge width when mouse is over the edge. Function should have the
         * following signature: function(width). Default value is 0.5
         */
        public Builder withHoverWidth(final String hoverWidth)
        {
            this.hoverWidth = hoverWidth;
            return this;
        }


        /**
         * Id of edge. The id should be unique.
         */
        public Builder withId(final String id)
        {
            this.id = id;
            return this;
        }


        /**
         * Label of an edge. Default value is null.
         */
        public Builder withLabel(final String label)
        {
            this.label = label;
            return this;
        }


        /**
         * Indicates whether label should become bold when node is selected. Default value is true.
         */
        public Builder withLabelHighlightBold(final Boolean labelHighlightBold)
        {
            this.labelHighlightBold = labelHighlightBold;
            return this;
        }


        /**
         * Indicates length of edge. Default value is null.
         */
        public Builder withLength(final Integer length)
        {
            this.length = length;
            return this;
        }


        /**
         * Indicates whether edge should be taken into account while physics simulation. Default value is true.
         */
        public Builder withPhysics(final Boolean physics)
        {
            this.physics = physics;
            return this;
        }


        /**
         * Represents option for scaling edges and nodes according to their properties.
         */
        public Builder withScaling(final Scaling scaling)
        {
            this.scaling = scaling;
            return this;
        }


        /**
         * Integer or javaScript's function. Indicates width of edge when edge is selected. Function should have the
         * following signature: function(width). Default value is 1.
         */
        public Builder withSelectionWidth(final String selectionWidth)
        {
            this.selectionWidth = selectionWidth;
            return this;
        }


        /**
         * Indicates radius of circle when 'from' and 'to' points to the same node.
         */
        public Builder withSelfReferenceSize(final Integer selfReferenceSize)
        {
            this.selfReferenceSize = selfReferenceSize;
            return this;
        }


        /**
         * Represents options for casting a shadow of node and edge.
         */
        public Builder withShadow(final Shadow shadow)
        {
            this.shadow = shadow;
            return this;
        }


        /**
         * Indicates whether edge should be drawn as a quadratic bezier curve. Performance of these edges are worse, but
         * they look better.
         */
        public Builder withSmooth(final Smooth smooth)
        {
            this.smooth = smooth;
            return this;
        }


        /**
         * Title of edge. Default value is null.
         */
        public Builder withTitle(final String title)
        {
            this.title = title;
            return this;
        }


        /**
         * Indicates value which is used for scaling. Default value is null.
         */
        public Builder withValue(final Integer value)
        {
            this.value = value;
            return this;
        }


        /**
         * Indicates width of an edge. Default value is 1.
         */
        public Builder withWidth(final Integer width)
        {
            this.width = width;
            return this;
        }


        /**
         * If value is provided then the maximum width of an edge is set to this value. Default value is null.
         */
        public Builder withWidthConstraint(final Integer widthConstraint)
        {
            this.widthConstraint = widthConstraint;
            return this;
        }


        public Builder withData(final Serializable data)
        {
            this.data = data;
            return this;
        }


        public Edge build()
        {
            final String calculatedId = StringUtils.isNotBlank(id) ? id : String.format("%s_%s", from, to);
            return new Edge(arrows, arrowStrikethrough, chosen, color, dashes, font, from, fromNode, hidden, hoverWidth,
                            calculatedId, label, labelHighlightBold, length, physics, scaling, selectionWidth, selfReferenceSize, shadow,
                            smooth, title, to, toNode, value, width, widthConstraint, data, "edge");
        }
    }
}
