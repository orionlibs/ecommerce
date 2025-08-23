/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.flow;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.WorkflowDesignerModelKey;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Optional;

/**
 * Default implementation of {@link WorkflowVisualisationChecker}
 */
public class DefaultWorkflowVisualisationChecker implements WorkflowVisualisationChecker
{
    @Override
    public boolean isVisualisationSet(final NetworkChartContext context)
    {
        return Optional.ofNullable(context).map(NetworkChartContext::getWim).map(WidgetInstanceManager::getModel)
                        .map(model -> model.getValue(WorkflowDesignerModelKey.KEY_IS_VISUALISATION_SET, Boolean.class)).orElse(false);
    }
}
