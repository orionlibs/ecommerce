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
 * Default configuration for nodes.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Node extends NetworkEntity
{
    /**
     * Indicates width of a node. Default value is 1.
     */
    private final Integer borderWidth;
    /**
     * Indicates width of selected node. Default value is 2.
     */
    private final Integer borderWidthSelected;
    /**
     * Indicates URL to image when url provided for 'image' or 'circularImage' is broken.
     */
    private final String brokenImage;
    /**
     * Indicates whether selecting or hovering on a node should change the node and/or it's label. If the option is not
     * provided, no change will be applied.
     */
    private final ChosenNode chosen;
    /**
     * Represents configuration for color of node.
     */
    private final NodeColor color;
    /**
     * Options for fixing node position while physics simulation.
     */
    private final Fixed fixed;
    /**
     * Represents options for label's font.
     */
    private final Font font;
    /**
     * Indicates node's group. Default value is null.
     */
    private final String group;
    /**
     * Represents options for height of a node.
     */
    private final HeightConstraint heightConstraint;
    /**
     * Indicates whether node is hidden. Default value is false.
     */
    private final Boolean hidden;
    /**
     * Represents options for icon. These options are taken into account when {@link Node#shape} is set to 'icon'.
     */
    private final Icon icon;
    /**
     * Represents image and circular image when These options are taken into account when {@link Node#shape} is set to
     * 'image' or "circularImage".
     */
    private final Image image;
    /**
     * Node's label. Default value is null.
     */
    private final String label;
    /**
     * Indicates whether label should become bold when node is selected. Default value is true.
     */
    private final Boolean labelHighlightBold;
    /**
     * Indicates level of node. The value is taking into account only for hierarchical layout. Default value is null.
     */
    private final Integer level;
    /**
     * Represents margins of labels. These options are only taken into account when {@link Node#shape} is set to box,
     * circle, database, icon or text.
     */
    private final Margin margin;
    /**
     * Indicates mass of node. Used in BarnesHut algorithm. Increasing the mass will increase repulsion of node. Default
     * value is 1.
     */
    private final Integer mass;
    /**
     * Indicates whether node should be member of physics simulation. Default value is true.
     */
    private final Boolean physics;
    /**
     * Represents option for scaling edges and nodes according to their properties.
     */
    private final Scaling scaling;
    /**
     * Represents options for casting a shadow of node and edge.
     */
    private final Shadow shadow;
    /**
     * Indicates shape of node. Possible values are: ellipse, circle, database, box, text, image, circularImage, diamond,
     * dot, star, triangle, triangleDown, square, icon. Default value is ellipse.
     */
    private final String shape;
    /**
     * Represents configuration for specific shapes.
     */
    private final ShapeProperties shapeProperties;
    /**
     * Indicates size of node. Default value is 25.
     */
    private final Integer size;
    /**
     * Indicates title of node. The value can be plain text or HTML.
     */
    private final String title;
    /**
     * Indicates value which is used for scaling. Default value is null.
     */
    private final Integer value;
    /**
     * Represents width of a node.
     */
    private final WidthConstraint widthConstraint;
    /**
     * Indicates initial x position of node. Default value is null.
     */
    private final Integer x;
    /**
     * Indicates initial y position of node. Default value is null.
     */
    private final Integer y;


    @JsonCreator
    protected Node(@JsonProperty("borderWidth") final Integer borderWidth,
                    @JsonProperty("borderWidthSelected") final Integer borderWidthSelected,
                    @JsonProperty("brokenImage") final String brokenImage, @JsonProperty("chosen") final ChosenNode chosen,
                    @JsonProperty("color") final NodeColor color, @JsonProperty("fixed") final Fixed fixed,
                    @JsonProperty("font") final Font font, @JsonProperty("group") final String group,
                    @JsonProperty("heightConstraint") final HeightConstraint heightConstraint, @JsonProperty("hidden") final Boolean hidden,
                    @JsonProperty("icon") final Icon icon, @JsonProperty("id") final String id, @JsonProperty("image") final Image image,
                    @JsonProperty("label") final String label, @JsonProperty("labelHighlightBold") final Boolean labelHighlightBold,
                    @JsonProperty("level") final Integer level, @JsonProperty("margin") final Margin margin,
                    @JsonProperty("mass") final Integer mass, @JsonProperty("physics") final Boolean physics,
                    @JsonProperty("scaling") final Scaling scaling, @JsonProperty("shadow") final Shadow shadow,
                    @JsonProperty("shape") final String shape, @JsonProperty("shapeProperties") final ShapeProperties shapeProperties,
                    @JsonProperty("size") final Integer size, @JsonProperty("title") final String title,
                    @JsonProperty("value") final Integer value, @JsonProperty("widthConstraint") final WidthConstraint widthConstraint,
                    @JsonProperty("x") final Integer x, @JsonProperty("y") final Integer y, @JsonProperty("data") final Serializable data,
                    @JsonProperty("type") final String type)
    {
        super(id, data, type);
        this.borderWidth = borderWidth;
        this.borderWidthSelected = borderWidthSelected;
        this.brokenImage = brokenImage;
        this.chosen = chosen;
        this.color = color;
        this.fixed = fixed;
        this.font = font;
        this.group = group;
        this.heightConstraint = heightConstraint;
        this.hidden = hidden;
        this.icon = icon;
        this.image = image;
        this.label = label;
        this.labelHighlightBold = labelHighlightBold;
        this.level = level;
        this.margin = margin;
        this.mass = mass;
        this.physics = physics;
        this.scaling = scaling;
        this.shadow = shadow;
        this.shape = shape;
        this.shapeProperties = shapeProperties;
        this.size = size;
        this.title = title;
        this.value = value;
        this.widthConstraint = widthConstraint;
        this.x = x;
        this.y = y;
    }


    public Integer getBorderWidth()
    {
        return borderWidth;
    }


    public Integer getBorderWidthSelected()
    {
        return borderWidthSelected;
    }


    public String getBrokenImage()
    {
        return brokenImage;
    }


    public ChosenNode getChosen()
    {
        return chosen;
    }


    public NodeColor getColor()
    {
        return color;
    }


    public Fixed getFixed()
    {
        return fixed;
    }


    public Font getFont()
    {
        return font;
    }


    public String getGroup()
    {
        return group;
    }


    public HeightConstraint getHeightConstraint()
    {
        return heightConstraint;
    }


    public Boolean getHidden()
    {
        return hidden;
    }


    public Icon getIcon()
    {
        return icon;
    }


    public Image getImage()
    {
        return image;
    }


    public String getLabel()
    {
        return label;
    }


    public Boolean getLabelHighlightBold()
    {
        return labelHighlightBold;
    }


    public Integer getLevel()
    {
        return level;
    }


    public Margin getMargin()
    {
        return margin;
    }


    public Integer getMass()
    {
        return mass;
    }


    public Boolean getPhysics()
    {
        return physics;
    }


    public Scaling getScaling()
    {
        return scaling;
    }


    public Shadow getShadow()
    {
        return shadow;
    }


    public String getShape()
    {
        return shape;
    }


    public ShapeProperties getShapeProperties()
    {
        return shapeProperties;
    }


    public Integer getSize()
    {
        return size;
    }


    public String getTitle()
    {
        return title;
    }


    public Integer getValue()
    {
        return value;
    }


    public WidthConstraint getWidthConstraint()
    {
        return widthConstraint;
    }


    public Integer getX()
    {
        return x;
    }


    public Integer getY()
    {
        return y;
    }


    public static class Builder
    {
        private Integer borderWidth;
        private Integer borderWidthSelected;
        private String brokenImage;
        private ChosenNode chosen;
        private NodeColor color;
        private Fixed fixed;
        private Font font;
        private String group;
        private HeightConstraint heightConstraint;
        private Boolean hidden;
        private Icon icon;
        private String id;
        private Image image;
        private String label;
        private Boolean labelHighlightBold;
        private Integer level;
        private Margin margin;
        private Integer mass;
        private Boolean physics;
        private Scaling scaling;
        private Shadow shadow;
        private String shape;
        private ShapeProperties shapeProperties;
        private Integer size;
        private String title;
        private Integer value;
        private WidthConstraint widthConstraint;
        private Integer x;
        private Integer y;
        private Serializable data;


        public Builder()
        {
        }


        public Builder(final Node origin)
        {
            if(origin != null)
            {
                this.borderWidth = origin.borderWidth;
                this.borderWidthSelected = origin.borderWidthSelected;
                this.brokenImage = origin.brokenImage;
                this.chosen = origin.chosen;
                this.color = origin.color;
                this.fixed = origin.fixed;
                this.font = origin.font;
                this.group = origin.group;
                this.heightConstraint = origin.heightConstraint;
                this.hidden = origin.hidden;
                this.icon = origin.icon;
                this.id = origin.id;
                this.image = origin.image;
                this.label = origin.label;
                this.labelHighlightBold = origin.labelHighlightBold;
                this.level = origin.level;
                this.margin = origin.margin;
                this.mass = origin.mass;
                this.physics = origin.physics;
                this.scaling = origin.scaling;
                this.shadow = origin.shadow;
                this.shape = origin.shape;
                this.shapeProperties = origin.shapeProperties;
                this.size = origin.size;
                this.title = origin.title;
                this.value = origin.value;
                this.widthConstraint = origin.widthConstraint;
                this.x = origin.x;
                this.y = origin.y;
                this.data = origin.data;
            }
        }


        /**
         * Indicates width of a node. Default value is 1.
         */
        public Builder withBorderWidth(final Integer borderWidth)
        {
            this.borderWidth = borderWidth;
            return this;
        }


        /**
         * Indicates width of selected node. Default value is 2.
         */
        public Builder withBorderWidthSelected(final Integer borderWidthSelected)
        {
            this.borderWidthSelected = borderWidthSelected;
            return this;
        }


        /**
         * Indicates URL to image when url provided for 'image' or 'circularImage' is broken.
         */
        public Builder withBrokenImage(final String brokenImage)
        {
            this.brokenImage = brokenImage;
            return this;
        }


        /**
         * Indicates whether selecting or hovering on a node should change the node and/or it's label. If the option is
         * not provided, no change will be applied.
         */
        public Builder withChosen(final ChosenNode chosen)
        {
            this.chosen = chosen;
            return this;
        }


        /**
         * Represents configuration for color of node.
         */
        public Builder withColor(final NodeColor color)
        {
            this.color = color;
            return this;
        }


        /**
         * Options for fixing node position while physics simulation.
         */
        public Builder withFixed(final Fixed fixed)
        {
            this.fixed = fixed;
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
         * Indicates node's group. Default value is null.
         */
        public Builder withGroup(final String group)
        {
            this.group = group;
            return this;
        }


        /**
         * Represents options for height of a node.
         */
        public Builder withHeightConstraint(final HeightConstraint heightConstraint)
        {
            this.heightConstraint = heightConstraint;
            return this;
        }


        /**
         * Indicates whether node is hidden. Default value is false.
         */
        public Builder withHidden(final Boolean hidden)
        {
            this.hidden = hidden;
            return this;
        }


        /**
         * Represents options for icon. These options are taken into account when {@link Node#shape} is set to 'icon'.
         */
        public Builder withIcon(final Icon icon)
        {
            this.icon = icon;
            return this;
        }


        /**
         * Indicates id of nodes. Id should be unique.
         */
        public Builder withId(final String id)
        {
            this.id = id;
            return this;
        }


        /**
         * Represents image and circular image when These options are taken into account when {@link Node#shape} is set to
         * 'image' or "circularImage".
         */
        public Builder withImage(final Image image)
        {
            this.image = image;
            return this;
        }


        /**
         * Node's label. Default value is null.
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
         * Indicates level of node. The value is taking into account only for hierarchical layout. Default value is null.
         */
        public Builder withLevel(final Integer level)
        {
            this.level = level;
            return this;
        }


        /**
         * Represents margins of labels. These options are only taken into account when {@link Node#shape} is set to box,
         * circle, database, icon or text.
         */
        public Builder withMargin(final Margin margin)
        {
            this.margin = margin;
            return this;
        }


        /**
         * Indicates mass of node. Used in BarnesHut algorithm. Increasing the mass will increase repulsion of node.
         * Default value is 1.
         */
        public Builder withMass(final Integer mass)
        {
            this.mass = mass;
            return this;
        }


        /**
         * Indicates whether node should be member of physics simulation. Default value is true.
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
         * Represents options for casting a shadow of node and edge.
         */
        public Builder withShadow(final Shadow shadow)
        {
            this.shadow = shadow;
            return this;
        }


        /**
         * Indicates shape of node. Possible values are: ellipse, circle, database, box, text, image, circularImage,
         * diamond, dot, star, triangle, triangleDown, square, icon. Default value is ellipse.
         */
        public Builder withShape(final String shape)
        {
            this.shape = shape;
            return this;
        }


        /**
         * Represents configuration for specific shapes.
         */
        public Builder withShapeProperties(final ShapeProperties shapeProperties)
        {
            this.shapeProperties = shapeProperties;
            return this;
        }


        /**
         * Indicates size of node. Default value is 25.
         */
        public Builder withSize(final Integer size)
        {
            this.size = size;
            return this;
        }


        /**
         * Indicates title of node. The value can be plain text or HTML.
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
         * Represents width of a node.
         */
        public Builder withWidthConstraint(final WidthConstraint widthConstraint)
        {
            this.widthConstraint = widthConstraint;
            return this;
        }


        /**
         * Indicates initial x position of node. Default value is null.
         */
        public Builder withX(final Integer x)
        {
            this.x = x;
            return this;
        }


        /**
         * Indicates initial y position of node. Default value is null.
         */
        public Builder withY(final Integer y)
        {
            this.y = y;
            return this;
        }


        public Builder withData(final Serializable data)
        {
            this.data = data;
            return this;
        }


        public Node build()
        {
            return new Node(borderWidth, borderWidthSelected, brokenImage, chosen, color, fixed, font, group, heightConstraint,
                            hidden, icon, id, image, label, labelHighlightBold, level, margin, mass, physics, scaling, shadow, shape,
                            shapeProperties, size, title, value, widthConstraint, x, y, data, "node");
        }
    }
}
