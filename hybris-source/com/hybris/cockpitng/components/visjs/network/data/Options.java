/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * Represents set of options for network chart.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Options implements Serializable
{
    public static final Options EMPTY = new Builder().build();
    /**
     * Indicates whether network chart should be redrawn when container will be resized. Default value is true.
     */
    private final Boolean autoResize;
    /**
     * Indicates height of canvas. Value can be set in percentage or pixels. Default value is 100%
     */
    private final String height;
    /**
     * Indicates width of canvas. Value can be set in percentage or pixels. Default value is 100%
     */
    private final String width;
    /**
     * Indicates current locale. Default value is current locale of logged user.
     */
    private String locale;
    /**
     * List of translations for particular languages. A key should indicates particular language, a value should be map of
     * translation for the following keys: "edit", "del", "back", "addNode", "addEdge", "editNode", "editEdge",
     * "addDescription", "edgeDescription", "editEdgeDescription", "createEdgeError", "deleteClusterError",
     * "editClusterError"
     */
    private Map<String, Map<String, String>> locales;
    /**
     * Indicates whether network should react on mouse and touch events when network is not active. Default value is false.
     */
    private final Boolean clickToUse;
    /**
     * Configuration class for a canvas
     */
    private final Configure configure;
    /**
     * Default configuration for edges.
     */
    private final Edge edges;
    /**
     * Default configuration for nodes.
     */
    private final Node nodes;
    /**
     * Configuration for groups of nodes. A key represents group's name, a value represents group configuration.
     */
    private final Map<String, Node> groups;
    /**
     * Options for network layout. Indicates whether layout should be hierarchical or not.
     */
    private final Layout layout;
    /**
     * Represents options for navigation buttons, mouse and touch events.
     */
    private final Interaction interaction;
    /**
     * Represents options for navigation buttons, mouse and touch events.
     */
    private final Manipulation manipulation;
    /**
     * Represents physics simulation.
     */
    private final Physics physics;


    @JsonCreator
    protected Options(@JsonProperty("autoResize") final Boolean autoResize, @JsonProperty("height") final String height,
                    @JsonProperty("width") final String width, @JsonProperty("locale") final String locale,
                    @JsonProperty("locales") final Map<String, Map<String, String>> locales,
                    @JsonProperty("clickToUse") final Boolean clickToUse, @JsonProperty("configure") final Configure configure,
                    @JsonProperty("edges") final Edge edges, @JsonProperty("nodes") final Node nodes,
                    @JsonProperty("groups") final Map<String, Node> groups, @JsonProperty("layout") final Layout layout,
                    @JsonProperty("interaction") final Interaction interaction,
                    @JsonProperty("manipulation") final Manipulation manipulation, @JsonProperty("physics") final Physics physics)
    {
        this.autoResize = autoResize;
        this.height = height;
        this.width = width;
        this.locale = locale;
        this.locales = locales;
        this.clickToUse = clickToUse;
        this.configure = configure;
        this.edges = edges;
        this.nodes = nodes;
        this.groups = groups;
        this.layout = layout;
        this.interaction = interaction;
        this.manipulation = manipulation;
        this.physics = physics;
    }


    public Boolean getAutoResize()
    {
        return autoResize;
    }


    public String getHeight()
    {
        return height;
    }


    public String getWidth()
    {
        return width;
    }


    public String getLocale()
    {
        return locale;
    }


    public void setLocale(final String locale)
    {
        this.locale = locale;
    }


    public Map<String, Map<String, String>> getLocales()
    {
        return locales;
    }


    public void setLocales(final Map<String, Map<String, String>> locales)
    {
        this.locales = locales;
    }


    public Boolean getClickToUse()
    {
        return clickToUse;
    }


    public Configure getConfigure()
    {
        return configure;
    }


    public Edge getEdges()
    {
        return edges;
    }


    public Node getNodes()
    {
        return nodes;
    }


    public Map<String, Node> getGroups()
    {
        return groups;
    }


    public Layout getLayout()
    {
        return layout;
    }


    public Interaction getInteraction()
    {
        return interaction;
    }


    public Manipulation getManipulation()
    {
        return manipulation;
    }


    public Physics getPhysics()
    {
        return physics;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final Options options = (Options)o;
        return Objects.equals(autoResize, options.autoResize) && Objects.equals(height, options.height)
                        && Objects.equals(width, options.width) && Objects.equals(locale, options.locale)
                        && Objects.equals(locales, options.locales) && Objects.equals(clickToUse, options.clickToUse)
                        && Objects.equals(configure, options.configure) && Objects.equals(edges, options.edges)
                        && Objects.equals(nodes, options.nodes) && Objects.equals(groups, options.groups)
                        && Objects.equals(layout, options.layout) && Objects.equals(interaction, options.interaction)
                        && Objects.equals(manipulation, options.manipulation) && Objects.equals(physics, options.physics);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(autoResize, height, width, locale, locales, clickToUse, configure, edges, nodes, groups, layout,
                        interaction, manipulation, physics);
    }


    public static class Builder
    {
        private Boolean autoResize;
        private String height;
        private String width;
        private String locale;
        private Map<String, Map<String, String>> locales;
        private Boolean clickToUse;
        private Configure configure;
        private Edge edges;
        private Node nodes;
        private Map<String, Node> groups;
        private Layout layout;
        private Interaction interaction;
        private Manipulation manipulation;
        private Physics physics;


        /**
         * Indicates whether network chart should be redrawn when container will be resized. Default value is true.
         */
        public Builder withAutoResize(final Boolean autoResize)
        {
            this.autoResize = autoResize;
            return this;
        }


        /**
         * Indicates height of canvas. Value can be set in percentage or pixels. Default value is 100%
         */
        public Builder withHeight(final String height)
        {
            this.height = height;
            return this;
        }


        /**
         * Indicates width of canvas. Value can be set in percentage or pixels. Default value is 100%
         */
        public Builder withWidth(final String width)
        {
            this.width = width;
            return this;
        }


        /**
         * Indicates current locale. Default value is current locale of logged user.
         */
        public Builder withLocale(final String locale)
        {
            this.locale = locale;
            return this;
        }


        /**
         * List of translations for particular languages. A key should indicates particular language, a value should be map of
         * translation for the following keys: "edit", "del", "back", "addNode", "addEdge", "editNode", "editEdge",
         * "addDescription", "edgeDescription", "editEdgeDescription", "createEdgeError", "deleteClusterError",
         * "editClusterError"
         */
        public Builder withLocales(final Map<String, Map<String, String>> locales)
        {
            this.locales = locales;
            return this;
        }


        /**
         * Indicates whether network should react on mouse and touch events when network is not active. Default value is false.
         */
        public Builder withClickToUse(final Boolean clickToUse)
        {
            this.clickToUse = clickToUse;
            return this;
        }


        /**
         * Configuration class for a canvas
         */
        public Builder withConfigure(final Configure configure)
        {
            this.configure = configure;
            return this;
        }


        /**
         * Default configuration for edges.
         */
        public Builder withEdges(final Edge edges)
        {
            this.edges = edges;
            return this;
        }


        /**
         * Default configuration for nodes.
         */
        public Builder withNodes(final Node nodes)
        {
            this.nodes = nodes;
            return this;
        }


        /**
         * Configuration for groups of nodes. A key represents group's name, a value represents group configuration.
         */
        public Builder withGroups(final Map<String, Node> groups)
        {
            this.groups = groups;
            return this;
        }


        /**
         * Options for network layout. Indicates whether layout should be hierarchical or not.
         */
        public Builder withLayout(final Layout layout)
        {
            this.layout = layout;
            return this;
        }


        /**
         * Represents options for navigation buttons, mouse and touch events.
         */
        public Builder withInteraction(final Interaction interaction)
        {
            this.interaction = interaction;
            return this;
        }


        /**
         * Represents options for navigation buttons, mouse and touch events.
         */
        public Builder withManipulation(final Manipulation manipulation)
        {
            this.manipulation = manipulation;
            return this;
        }


        /**
         * Represents physics simulation.
         */
        public Builder withPhysics(final Physics physics)
        {
            this.physics = physics;
            return this;
        }


        public Options build()
        {
            return new Options(autoResize, height, width, locale, locales, clickToUse, configure, edges, nodes, groups, layout,
                            interaction, manipulation, physics);
        }
    }
}
