/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.event;

import com.hybris.cockpitng.components.visjs.network.data.Node;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

/**
 * Represents an event that indicates a hovering a node.
 */
public class HoverNodeEvent extends Event
{
    public static final String NAME = "onHoverNode";


    public HoverNodeEvent(final Component target, final Node node)
    {
        super(NAME, target, node);
    }


    /**
     * @return {@link Node} node which has been hovered
     */
    public Node getNode()
    {
        return (Node)getData();
    }
}
