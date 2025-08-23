/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.pojo;

import de.hybris.platform.core.model.ItemModel;

/**
 * Represents a Workflow's element which can be visualized - Template Action, Template Decision, Decision, Action and
 * Link
 */
public interface WorkflowEntity
{
    /**
     * @return model of {@link WorkflowEntity}
     */
    ItemModel getModel();
}
