/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.flow;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import java.util.function.Predicate;

/**
 * Allows to check if visualisation is set for a model
 */
@FunctionalInterface
public interface WorkflowVisualisationChecker extends Predicate<NetworkChartContext>
{
    /**
     * Allows to check whether visualisation is set for a model or not
     *
     * @param context
     *           context with data about the network
     * @return
     */
    boolean isVisualisationSet(final NetworkChartContext context);


    /**
     * {@inheritDoc}
     */
    @Override
    default boolean test(final NetworkChartContext context)
    {
        return isVisualisationSet(context);
    }
}
