/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.validation;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

/**
 * Combines multiple validators and runs every one.
 */
public class CombinedWorkflowDesignerValidator implements WorkflowDesignerValidator
{
    private List<WorkflowDesignerValidator> validators;


    @Override
    public WorkflowDesignerValidationResult validate(final NetworkChartContext context)
    {
        return validators.stream().map(validator -> validator.validate(context)).reduce(WorkflowDesignerValidationResult::combine)
                        .orElse(new WorkflowDesignerValidationResult());
    }


    @Required
    public void setValidators(final List<WorkflowDesignerValidator> validators)
    {
        this.validators = validators;
    }
}
