/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.validation;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowDesignerGroup;

/**
 * Validates each 'start' action node. Workflow template should have at least one "start" node.
 */
public class StartActionValidator extends AbstractValidator
{
    public static final String VALIDATION_WARNING_MESSAGE = "workflowdesigner.notification.lackOfStartOrEndAction.warning";


    @Override
    public WorkflowDesignerValidationResult validate(final NetworkChartContext context)
    {
        final WorkflowDesignerValidationResult validationResult = new WorkflowDesignerValidationResult();
        if(!hasStartAction(context))
        {
            final Violation warn = Violation.warn(VALIDATION_WARNING_MESSAGE);
            validationResult.addViolation(warn);
        }
        return validationResult;
    }


    private boolean hasStartAction(final NetworkChartContext context)
    {
        return getNetworkEntityFinder().findActionNodes(context).stream()
                        .anyMatch(node -> WorkflowDesignerGroup.START_ACTION.getValue().equals(node.getGroup()));
    }
}
