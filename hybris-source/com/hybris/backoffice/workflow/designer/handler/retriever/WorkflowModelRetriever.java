/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.retriever;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import de.hybris.platform.core.model.ItemModel;
import java.util.Optional;

/**
 * Allows to retrieve model related to workflows from {@link com.hybris.cockpitng.core.model.WidgetModel}
 *
 * @param <MODEL>
 */
public interface WorkflowModelRetriever<MODEL extends ItemModel>
{
    /**
     * @param node
     *           currently edited node
     * @param networkChartContext
     *           context of network chart
     * @return retrieved model
     */
    Optional<MODEL> retrieve(final Node node, final NetworkChartContext networkChartContext);
}
