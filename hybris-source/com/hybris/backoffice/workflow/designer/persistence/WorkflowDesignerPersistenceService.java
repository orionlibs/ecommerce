/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.persistence;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

/**
 * Allows to persist the Workflow Template that is edited in Workflow Designer.
 */
public interface WorkflowDesignerPersistenceService
{
    /**
     * Persists the {@link WorkflowTemplateModel} given in the context.
     *
     * @param context
     *           contains the workflow template to persist
     * @throws WorkflowDesignerSavingException
     *            when the save was unsuccessful
     */
    void persist(final NetworkChartContext context);
}
