/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.create;

import com.hybris.backoffice.widgets.networkchart.NetworkChartController;
import com.hybris.backoffice.workflow.designer.dto.ElementLocation;
import com.hybris.cockpitng.components.visjs.network.data.Point;
import com.hybris.cockpitng.engine.WidgetInstanceManager;

/**
 * Provides zero location of the itemModel placed in Workflow Designer
 */
public class DefaultInitialElementLocationProvider implements InitialElementLocationProvider
{
    @Override
    public ElementLocation provideLocation(final WidgetInstanceManager wim)
    {
        final Point canvasCenter = wim.getModel().getValue(NetworkChartController.MODEL_CANVAS_CENTER, Point.class);
        return canvasCenter != null ? toElementLocation(canvasCenter) : ElementLocation.zeroLocation();
    }


    private ElementLocation toElementLocation(final Point canvasCenter)
    {
        final Number x = canvasCenter.getX();
        final Number y = canvasCenter.getY();
        return ElementLocation.of(x != null ? x.intValue() : ElementLocation.ZERO_POSITION_X,
                        y != null ? y.intValue() : ElementLocation.ZERO_POSITION_Y);
    }
}
