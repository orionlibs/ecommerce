/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection.validator;

import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowConnection;
import org.springframework.core.Ordered;

/**
 * Allows to validate whether given {@link WorkflowConnection} is valid or not
 */
public interface WorkflowConnectionPartialValidator extends Ordered
{
    int MEDIUM_PRECEDENCE = 0;


    /**
     * Validates workflow based on the {@link ValidationContext } passed
     *
     * @param context
     *           context of the validation
     * @return result containing potential violations
     */
    WorkflowConnectionValidationResult validate(ValidationContext context);


    @Override
    default int getOrder()
    {
        return MEDIUM_PRECEDENCE;
    }
}
