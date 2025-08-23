/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.persistence;

import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import java.util.Collection;

/**
 * Creates link between two Workflow items
 */
public class LinkCreator
{
    /**
     * Creates link between decision and action
     *
     * @param workflowDecision
     *           link source
     * @param workflowAction
     *           link target
     */
    public void createLinkFromDecisionToAction(final WorkflowDecisionTemplateModel workflowDecision,
                    final WorkflowActionTemplateModel workflowAction)
    {
        if(!workflowAction.getIncomingTemplateDecisions().contains(workflowDecision))
        {
            final Collection<WorkflowActionTemplateModel> toTemplateActions = MutableListUtil
                            .toMutableList(workflowDecision.getToTemplateActions());
            toTemplateActions.add(workflowAction);
            workflowDecision.setToTemplateActions(toTemplateActions);
            final Collection<WorkflowDecisionTemplateModel> incomingTemplateDecisions = MutableListUtil
                            .toMutableList(workflowAction.getIncomingTemplateDecisions());
            incomingTemplateDecisions.add(workflowDecision);
            workflowAction.setIncomingTemplateDecisions(incomingTemplateDecisions);
        }
    }
}
