/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.validation;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import java.util.Collection;

/**
 * Validates each decision node. Each decision need to have at least one connection to another node, otherwise it cannot
 * be saved.
 */
public class DecisionValidator extends AbstractValidator
{
    public static final String DECISION_NEEDS_AT_LEAST_ONE_CONNECTION = "workflowdesigner.notification.decisionOrphaned.error";


    @Override
    public WorkflowDesignerValidationResult validate(final NetworkChartContext context)
    {
        final WorkflowDesignerValidationResult validationResult = new WorkflowDesignerValidationResult();
        getNetworkEntityFinder().findDecisionNodes(context).forEach(decision -> {
            final Collection<Edge> incomingEdges = getConnectionFinder().findEdgesToNode(context, decision);
            final Collection<Edge> outgoingEdges = getConnectionFinder().findEdgesFromNode(context, decision);
            if(incomingEdges.isEmpty() && outgoingEdges.isEmpty())
            {
                validationResult.addViolation(Violation.error(DECISION_NEEDS_AT_LEAST_ONE_CONNECTION, getDecisionName(decision)));
            }
        });
        return validationResult;
    }


    private String getDecisionName(final Node decision)
    {
        return getNodeTypeService().getNodeName(decision);
    }
}
