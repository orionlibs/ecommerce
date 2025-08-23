/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.validation;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;

/**
 * Validates that the current workflow template can be persisted.
 */
public interface WorkflowDesignerValidator
{
    /**
     * Performs validation
     *
     * @param context
     *           contains workflow template to be validated
     */
    WorkflowDesignerValidationResult validate(final NetworkChartContext context);
}
