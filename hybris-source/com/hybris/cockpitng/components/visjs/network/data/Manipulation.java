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
 * Options for network manipulation.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Manipulation implements Serializable
{
    /**
     * Enables network manipulation. Default is false.
     */
    private final Boolean enabled;
    /**
     * Indicates whether toolbar is visible initially. Default value is true.
     */
    private final Boolean initiallyActive;
    /**
     * Enables 'addNode' button. When user clicks the button then
     * {@link com.hybris.cockpitng.components.visjs.network.event.AddNodeEvent} is sent. Default value is false.
     */
    private final Boolean addNode;
    /**
     * Enables 'addEdge' button. When user clicks the button then
     * {@link com.hybris.cockpitng.components.visjs.network.event.AddEdgeEvent} is sent. Default value is false.
     */
    private final Boolean addEdge;
    /**
     * Enables 'editNode' button. When user clicks the button then
     * {@link com.hybris.cockpitng.components.visjs.network.event.EditNodeEvent} is sent. Default value is false.
     */
    private final Boolean editNode;
    /**
     * Enables 'editEdge' button. When user clicks the button then
     * {@link com.hybris.cockpitng.components.visjs.network.event.EditEdgeEvent} is sent. Default value is false.
     */
    private final Boolean editEdge;
    /**
     * Enables 'deleteNode' button. When user clicks the button then
     * {@link com.hybris.cockpitng.components.visjs.network.event.RemoveNodesEvent} is sent. Default value is false.
     */
    private final Boolean deleteNode;
    /**
     * Enables 'deleteEdge' button. When user clicks the button then
     * {@link com.hybris.cockpitng.components.visjs.network.event.RemoveEdgesEvent} is sent. Default value is false.
     */
    private final Boolean deleteEdge;
    /**
     * Indicates styling information. All field described in {@link Node} are valid. Default value is null.
     */
    private final Node controlNodeStyle;


    @JsonCreator
    protected Manipulation(@JsonProperty("enabled") final Boolean enabled,
                    @JsonProperty("initiallyActive") final Boolean initiallyActive, @JsonProperty("addNode") final Boolean addNode,
                    @JsonProperty("addEdge") final Boolean addEdge, @JsonProperty("editNode") final Boolean editNode,
                    @JsonProperty("editEdge") final Boolean editEdge, @JsonProperty("deleteNode") final Boolean deleteNode,
                    @JsonProperty("deleteEdge") final Boolean deleteEdge, @JsonProperty("controlNodeStyle") final Node controlNodeStyle)
    {
        this.enabled = enabled;
        this.initiallyActive = initiallyActive;
        this.addNode = addNode;
        this.addEdge = addEdge;
        this.editNode = editNode;
        this.editEdge = editEdge;
        this.deleteNode = deleteNode;
        this.deleteEdge = deleteEdge;
        this.controlNodeStyle = controlNodeStyle;
    }


    public Boolean getEnabled()
    {
        return enabled;
    }


    public Boolean getInitiallyActive()
    {
        return initiallyActive;
    }


    public Boolean getAddNode()
    {
        return addNode;
    }


    public Boolean getAddEdge()
    {
        return addEdge;
    }


    public Boolean getEditNode()
    {
        return editNode;
    }


    public Boolean getEditEdge()
    {
        return editEdge;
    }


    public Boolean getDeleteNode()
    {
        return deleteNode;
    }


    public Boolean getDeleteEdge()
    {
        return deleteEdge;
    }


    public Node getControlNodeStyle()
    {
        return controlNodeStyle;
    }


    public static class Builder
    {
        private Boolean enabled;
        private Boolean initiallyActive;
        private Boolean addNode;
        private Boolean addEdge;
        private Boolean editNode;
        private Boolean editEdge;
        private Boolean deleteNode;
        private Boolean deleteEdge;
        private Node controlNodeStyle;


        /**
         * Enables network manipulation. Default is false.
         */
        public Builder withEnabled(final Boolean enabled)
        {
            this.enabled = enabled;
            return this;
        }


        /**
         * Indicates whether toolbar is visible initially. Default value is true.
         */
        public Builder withInitiallyActive(final Boolean initiallyActive)
        {
            this.initiallyActive = initiallyActive;
            return this;
        }


        /**
         * Enables 'addNode' button. When user clicks the button then
         * {@link com.hybris.cockpitng.components.visjs.network.event.AddNodeEvent} is sent. Default value is false.
         */
        public Builder withAddNode(final Boolean addNode)
        {
            this.addNode = addNode;
            return this;
        }


        /**
         * Enables 'editEdge' button. When user clicks the button then
         * {@link com.hybris.cockpitng.components.visjs.network.event.AddEdgeEvent} is sent. Default value is false.
         */
        public Builder withAddEdge(final Boolean addEdge)
        {
            this.addEdge = addEdge;
            return this;
        }


        /**
         * Enables 'editNode' button. When user clicks the button then
         * {@link com.hybris.cockpitng.components.visjs.network.event.EditNodeEvent} is sent. Default value is false.
         */
        public Builder withEditNode(final Boolean editNode)
        {
            this.editNode = editNode;
            return this;
        }


        /**
         * Enables 'editEdge' button. When user clicks the button then
         * {@link com.hybris.cockpitng.components.visjs.network.event.EditEdgeEvent} is sent. Default value is false.
         */
        public Builder withEditEdge(final Boolean editEdge)
        {
            this.editEdge = editEdge;
            return this;
        }


        /**
         * Enables 'deleteNodes' button. When user clicks the button then
         * {@link com.hybris.cockpitng.components.visjs.network.event.RemoveNodesEvent} is sent. Default value is false.
         */
        public Builder withDeleteNode(final Boolean deleteNode)
        {
            this.deleteNode = deleteNode;
            return this;
        }


        /**
         * Enables 'deleteEdges' button. When user clicks the button then
         * {@link com.hybris.cockpitng.components.visjs.network.event.RemoveEdgesEvent} is sent. Default value is false.
         */
        public Builder withDeleteEdge(final Boolean deleteEdge)
        {
            this.deleteEdge = deleteEdge;
            return this;
        }


        /**
         * Indicates styling information. All field described in {@link Node} are valid. Default value is null.
         */
        public Builder withControlNodeStyle(final Node controlNodeStyle)
        {
            this.controlNodeStyle = controlNodeStyle;
            return this;
        }


        public Manipulation build()
        {
            return new Manipulation(enabled, initiallyActive, addNode, addEdge, editNode, editEdge, deleteNode, deleteEdge,
                            controlNodeStyle);
        }
    }
}
