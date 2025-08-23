/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.event;

import com.hybris.cockpitng.components.visjs.network.data.Point;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

/**
 * Represents an event when the view position (canvas center) has changed.
 */
public class ViewPositionChangeEvent extends Event
{
    public static final String NAME = "onViewPositionChange";


    public ViewPositionChangeEvent(final Component target, final Point param)
    {
        super(NAME, target, param);
    }


    /**
     * @return new view position (canvas center point)
     */
    public Point getViewPosition()
    {
        return (Point)getData();
    }
}
