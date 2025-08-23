/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.event;

import com.hybris.cockpitng.components.visjs.network.data.Edge;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

/**
 * Represents an event that indicates a blurring an edge.
 */
public class BlurEdgeEvent extends Event
{
    public static final String NAME = "onBlurEdge";


    public BlurEdgeEvent(final Component target, final Edge edge)
    {
        super(NAME, target, edge);
    }


    /**
     * @return {@link Edge} edge which has been blurred
     */
    public Edge getEdge()
    {
        return (Edge)getData();
    }
}
