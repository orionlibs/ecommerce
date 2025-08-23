/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.pojo;

import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import java.util.Collection;

/**
 * Represents a Workflow's Action - Template Action or Action
 */
public interface WorkflowAction extends WorkflowEntity
{
    /**
     * @return model of {@link WorkflowAction}
     */
    AbstractWorkflowActionModel getModel();


    /**
     * @return returns {@link WorkflowDecision}s which belongs to {@link WorkflowAction}
     */
    Collection<WorkflowDecision> getDecisions();


    /**
     * @return returns incoming {@link WorkflowDecision}s which belongs to {@link WorkflowAction}
     */
    Collection<WorkflowDecision> getIncomingDecisions();


    /**
     * @return returns incoming {@link de.hybris.platform.core.model.link.LinkModel}s which belongs to
     *         {@link WorkflowAction}
     */
    Collection<WorkflowLink> getIncomingLinks();


    /**
     * @return {@link WorkflowActionType} of {@link WorkflowAction}
     */
    WorkflowActionType getActionType();
}
