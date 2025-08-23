/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import org.zkoss.zk.ui.event.Event;

/**
 * Listener for action/decision/and connection creation
 */
public interface WorkflowDesignerOnAddNodeActionsListener
{
    default void onAddActionNodeButtonClick(final Event ev, final NetworkChartContext context)
    {
    }


    default void onAddDecisionNodeButtonClick(final Event ev, final NetworkChartContext context)
    {
    }


    default void onAddAndNodeButtonClick(final Event ev, final NetworkChartContext context)
    {
    }
}
