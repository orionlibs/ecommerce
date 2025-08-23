/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection.validator;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import java.util.Objects;

/**
 * Contains all necessary objects for the validators
 */
public class ValidationContext
{
    private final NetworkChartContext networkChartContext;
    private final Edge edge;


    private ValidationContext(final NetworkChartContext context, final Edge edge)
    {
        this.networkChartContext = Objects.requireNonNull(context, "Context cannot be null");
        this.edge = Objects.requireNonNull(edge, "Edge cannot be null");
    }


    /**
     * Creates new context based on the passed {@link NetworkChartContext} and {@link Edge}
     *
     * @param networkChartContext
     *           network chart context that contains all nodes for validation
     * @param edge
     *           edge that is not yet created but to be validated
     * @return new context
     */
    public static ValidationContext ofContextAndEdge(final NetworkChartContext networkChartContext, final Edge edge)
    {
        return new ValidationContext(networkChartContext, edge);
    }


    public NetworkChartContext getNetworkChartContext()
    {
        return networkChartContext;
    }


    public Edge getEdge()
    {
        return edge;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final ValidationContext that = (ValidationContext)o;
        return Objects.equals(networkChartContext, that.networkChartContext) && Objects.equals(edge, that.edge);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(networkChartContext, edge);
    }
}
