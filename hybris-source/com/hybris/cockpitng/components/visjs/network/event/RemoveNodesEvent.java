/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.event;

import com.hybris.cockpitng.components.visjs.network.data.Nodes;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

/**
 * Represents an event that indicates a clicking on 'Remove nodes' button.
 */
public class RemoveNodesEvent extends Event
{
    public static final String NAME = "onRemoveNodes";


    public RemoveNodesEvent(final Component target, final Nodes nodes)
    {
        super(NAME, target, nodes);
    }


    /**
     * @return {@link Nodes} represents nodes which should be removed
     */
    public Nodes getNodes()
    {
        return (Nodes)getData();
    }
}
