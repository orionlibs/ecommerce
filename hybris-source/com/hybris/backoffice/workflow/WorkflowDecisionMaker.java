/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow;

import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;

public interface WorkflowDecisionMaker
{
    /**
     * Sets selected decision for given workflow action
     *
     * @param workflowAction
     *           workflow action to decide on
     * @param selectedDecision
     *           decision to be made
     * @param widgetInstanceManager
     */
    void makeDecision(final WorkflowActionModel workflowAction, final WorkflowDecisionModel selectedDecision,
                    final WidgetInstanceManager widgetInstanceManager);
}
