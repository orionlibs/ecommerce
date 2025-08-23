/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.retriever;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import de.hybris.platform.core.model.ItemModel;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Required;

/**
 * Composition of {@link WorkflowModelRetriever}s
 */
public class DefaultWorkflowModelRetriever implements WorkflowModelRetriever<ItemModel>
{
    private Collection<WorkflowModelRetriever<ItemModel>> workflowModelRetrievers;


    @Override
    public Optional<ItemModel> retrieve(final Node node, final NetworkChartContext networkChartContext)
    {
        return workflowModelRetrievers.stream().map(retriever -> retriever.retrieve(node, networkChartContext))
                        .filter(Optional::isPresent).findFirst().flatMap(Function.identity());
    }


    @Required
    public void setWorkflowModelRetrievers(final Collection<WorkflowModelRetriever<ItemModel>> workflowModelRetrievers)
    {
        this.workflowModelRetrievers = workflowModelRetrievers;
    }
}
