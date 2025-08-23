/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import com.hybris.cockpitng.engine.WidgetInstanceManager;

/**
 * Handles the 'Save' button in workflow designer.
 */
public interface WorkflowDesignerSaveHandler
{
    /**
     * Performs save, displays any errors / warnings in notification.
     *
     * @param context
     *           contains the {@link WidgetInstanceManager} of the Workflow Designer
     * @return any network updates necessary
     */
    NetworkUpdates save(final NetworkChartContext context);
}
