/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection.validator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

/**
 * Workflow connection validator containing all partial validators in given order
 */
public class WorkflowConnectionValidator
{
    private List<WorkflowConnectionPartialValidator> partialValidators = Collections.emptyList();


    /**
     * Validates workflow connections based on the context passed. The result of all partialValidators will be merged into
     * one result. All partial validators will be executed in the order specified by the value returned by
     * {@link WorkflowConnectionPartialValidator#getOrder()} method.
     *
     * @param context
     *           context of the validation
     * @return result of the validation containing potential violations
     */
    public WorkflowConnectionValidationResult validate(final ValidationContext context)
    {
        return getPartialValidators().stream().map(validator -> validator.validate(context))
                        .reduce(WorkflowConnectionValidationResult::concat).orElse(WorkflowConnectionValidationResult.EMPTY);
    }


    public List<WorkflowConnectionPartialValidator> getPartialValidators()
    {
        return partialValidators;
    }


    @Required
    public void setPartialValidators(final List<WorkflowConnectionPartialValidator> partialValidators)
    {
        this.partialValidators = partialValidators;
        this.partialValidators.sort(Comparator.comparingInt(WorkflowConnectionPartialValidator::getOrder));
    }
}
