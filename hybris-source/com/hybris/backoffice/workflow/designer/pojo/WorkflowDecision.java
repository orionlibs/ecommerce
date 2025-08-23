/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.pojo;

import de.hybris.platform.workflow.model.AbstractWorkflowDecisionModel;

/**
 * Represents a Workflow Decision - which can be Template Decision or Decision
 */
public interface WorkflowDecision extends WorkflowEntity
{
    /**
     * @return model of {@link WorkflowDecision}
     */
    @Override
    AbstractWorkflowDecisionModel getModel();
}
