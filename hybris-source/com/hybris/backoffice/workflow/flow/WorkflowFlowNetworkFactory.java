/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.flow;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.WorkflowItem;
import com.hybris.cockpitng.components.visjs.network.data.Network;
import java.util.Collection;
import org.springframework.core.Ordered;

/**
 * Factory which allows to create instance of {@link Network}
 */
public interface WorkflowFlowNetworkFactory extends Ordered
{
    /**
     * Checks if it is possible to create a {@link Network} for given input
     *
     * @param items
     *           the collection of {@link WorkflowItem} which can be used to build the {@link Network}
     * @param context
     *           context with data about the {@link Network}
     * @return whether {@link Network} can be created or not
     */
    boolean canHandle(final Collection<WorkflowItem> items, final NetworkChartContext context);


    /**
     * Creates instance of {@link Network}
     *
     * @param items
     *           the collection of {@link WorkflowItem} which can be used to build the {@link Network}
     * @return newly created {@link Network}
     */
    default Network create(final Collection<WorkflowItem> items)
    {
        return create(items, null);
    }


    /**
     * Creates instance of {@link Network}
     *
     * @param items
     *           the collection of {@link WorkflowItem} which can be used to build the {@link Network}
     * @param context
     *           context with data about the {@link Network}
     * @return newly created {@link Network}
     */
    Network create(final Collection<WorkflowItem> items, final NetworkChartContext context);
}
