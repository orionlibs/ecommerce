/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.networkchart.handler;

import com.hybris.backoffice.widgets.networkchart.NetworkChartController;
import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.cockpitng.components.visjs.network.data.Network;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;

/**
 * {@link NetworkPopulator} is used by {@link NetworkChartController} for populating initial data of network chart.
 */
public interface NetworkPopulator
{
    /**
     * Prepares initial set of nodes and edges.
     *
     * @param context
     *           stores information about initData and {@link com.hybris.cockpitng.engine.WidgetInstanceManager}
     * @return {@link Network} - initial set of edges and nodes
     */
    Network populate(final NetworkChartContext context);


    /**
     * Updates given object in network chart.
     * @param updatedObject - object which should be updated
     * @param context stores information about initData and {@link com.hybris.cockpitng.engine.WidgetInstanceManager}
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    NetworkUpdates update(final Object updatedObject, final NetworkChartContext context);
}
