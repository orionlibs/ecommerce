/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.renderer.predicates;

import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import java.util.function.Predicate;

public class DecisionWorkflowActionPredicate implements Predicate<WorkflowActionModel>
{
    @Override
    public boolean test(final WorkflowActionModel workflowActionModel)
    {
        return WorkflowActionStatus.IN_PROGRESS.equals(workflowActionModel.getStatus());
    }
}
