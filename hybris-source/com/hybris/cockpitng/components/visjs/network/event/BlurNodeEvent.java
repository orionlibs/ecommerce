/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.event;

import com.hybris.cockpitng.components.visjs.network.data.Node;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

/**
 * Represents an event that indicates a blurring a node.
 */
public class BlurNodeEvent extends Event
{
    public static final String NAME = "onBlurNode";


    public BlurNodeEvent(final Component target, final Node node)
    {
        super(NAME, target, node);
    }


    /**
     * @return {@link Node} node which has been blurred
     */
    public Node getNode()
    {
        return (Node)getData();
    }
}
