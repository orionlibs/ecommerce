/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.navigation;

/**
 * An object used to select explorerTree's {@link NavigationNode} by id.
 */
public class TreeNodeSelector
{
    private final String nodeId;
    private final boolean triggerSelectionEvents;


    /**
     * @param nodeId id of explorer tree node to select.
     * @param triggerSelectionEvents if the flag is true selection event will be invoked on explorer's tree output
     *           sockets
     */
    public TreeNodeSelector(final String nodeId, final boolean triggerSelectionEvents)
    {
        this.triggerSelectionEvents = triggerSelectionEvents;
        this.nodeId = nodeId;
    }


    /**
     * @return id of a node to select
     */
    public String getNodeId()
    {
        return nodeId;
    }


    /**
     * @return true if selection event should be triggered
     */
    public boolean isTriggerSelectionEvents()
    {
        return triggerSelectionEvents;
    }
}
