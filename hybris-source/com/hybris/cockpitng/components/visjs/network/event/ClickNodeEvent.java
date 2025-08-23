/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.event;

import com.hybris.cockpitng.components.visjs.network.data.Node;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

/**
 * Represents an event that indicates a clicking on a node.
 */
public class ClickNodeEvent extends Event
{
    public static final String NAME = "onClickNode";


    public ClickNodeEvent(final Component target, final Node node)
    {
        super(NAME, target, node);
    }


    /**
     * @return {@link Node} node which has been clicked
     */
    public Node getNode()
    {
        return (Node)getData();
    }
}
