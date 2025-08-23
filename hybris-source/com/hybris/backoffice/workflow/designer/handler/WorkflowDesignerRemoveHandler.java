/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.cockpitng.components.visjs.network.data.Edges;
import com.hybris.cockpitng.components.visjs.network.data.Nodes;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import com.hybris.cockpitng.engine.WidgetInstanceManager;

/**
 * Handles the 'Delete' action in workflow designer.
 */
public interface WorkflowDesignerRemoveHandler
{
    /**
     * Maps removed nodes to network updates, clears removed nodes from the model
     *
     * @param nodes
     *           nodes that were removed
     * @param context
     *           contains the {@link WidgetInstanceManager} of the Workflow Designer
     * @return any network updates necessary
     */
    NetworkUpdates remove(Nodes nodes, NetworkChartContext context);


    /**
     * Maps removed edges to network updates
     *
     * @param edges
     *           edges that were removed
     * @param context
     *           contains the {@link WidgetInstanceManager} of the Workflow Designer
     * @return any network updates necessary
     */
    NetworkUpdates remove(Edges edges, NetworkChartContext context);
}
