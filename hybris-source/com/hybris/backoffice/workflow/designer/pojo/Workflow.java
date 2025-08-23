/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.pojo;

import de.hybris.platform.core.model.ItemModel;
import java.util.Collection;

/**
 * Represents POJO which encapsulates {@link de.hybris.platform.workflow.model.WorkflowModel} or
 * {@link de.hybris.platform.workflow.model.WorkflowTemplateModel}
 */
public interface Workflow
{
    /**
     * @return {@link de.hybris.platform.workflow.model.WorkflowModel} or
     *         {@link de.hybris.platform.workflow.model.WorkflowTemplateModel}
     */
    ItemModel getModel();


    /**
     * @return {@link WorkflowAction}s which belongs to given {@link #getModel()}
     */
    Collection<WorkflowAction> getActions();
}
