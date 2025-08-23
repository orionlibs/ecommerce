/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.flow;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.WorkflowItem;
import com.hybris.cockpitng.components.visjs.network.data.Network;
import java.util.Collection;
import java.util.Optional;

/**
 * Allows to find correct {@link WorkflowFlowNetworkFactory}
 */
public interface WorkflowNetworkFactoryStrategy
{
    /**
     * Finds {@link WorkflowFlowNetworkFactory}. Empty {@link Optional} is found if there is no
     * {@link WorkflowFlowNetworkFactory} which is able to handle the input.
     *
     * @param items
     *           the collection of {@link WorkflowItem} which can be used to build the {@link Network}
     * @param context
     *           context with data about the {@link Network}
     * @return {@link WorkflowFlowNetworkFactory} to create the {@link Network} with
     */
    Optional<WorkflowFlowNetworkFactory> find(final Collection<WorkflowItem> items, final NetworkChartContext context);
}
