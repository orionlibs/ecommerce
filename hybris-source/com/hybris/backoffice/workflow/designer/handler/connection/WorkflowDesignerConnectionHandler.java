/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import com.hybris.cockpitng.engine.WidgetInstanceManager;

/**
 * Handles the 'add edge' event in workflow designer.
 */
public interface WorkflowDesignerConnectionHandler
{
    /**
     * Maps added edge to network updates, validates invalid connections
     *
     * @param edge
     *           edge that was added
     * @param context
     *           contains the {@link WidgetInstanceManager} of the Workflow Designer
     * @return any network updates necessary
     */
    NetworkUpdates addEdge(final NetworkChartContext context, final Edge edge);
}
