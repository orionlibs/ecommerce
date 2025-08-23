/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection.validator;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Validation results containing {@link Violation}s
 */
public class WorkflowConnectionValidationResult
{
    /**
     * Constant representing no validation errors, that is no {@link Violation}s
     */
    public static final WorkflowConnectionValidationResult EMPTY = new WorkflowConnectionValidationResult(Collections.emptySet());
    private final Collection<Violation> violations;


    private WorkflowConnectionValidationResult(final Collection<Violation> violations)
    {
        this.violations = violations;
    }


    /**
     * Creates new validation result based on the {@link Violation}s passed
     *
     * @param violations
     *           violation objects that are part of the created validation result
     * @return validation result containing passed violations
     */
    public static WorkflowConnectionValidationResult ofViolations(final Violation... violations)
    {
        final Set<Violation> unmodifiableSet = Set.of(violations);
        return new WorkflowConnectionValidationResult(unmodifiableSet);
    }


    /**
     * Creates new validation result based on the passed, existing results
     *
     * @param firstResult
     *           validation result of which violations are going to be merged into new result
     * @param secondResult
     *           validation result of which violations are going to be merged into new result
     * @return new validation result containing violations of two passed validation results
     */
    public static WorkflowConnectionValidationResult concat(final WorkflowConnectionValidationResult firstResult,
                    final WorkflowConnectionValidationResult secondResult)
    {
        final Set<Violation> violations = new HashSet<>();
        violations.addAll(firstResult.getViolations());
        violations.addAll(secondResult.getViolations());
        return new WorkflowConnectionValidationResult(Set.copyOf(violations));
    }


    /**
     * Checks if given result is successful, that is has no violations
     *
     * @return true if result is successful, false otherwise
     */
    public boolean isSuccessful()
    {
        return violations.isEmpty();
    }


    /**
     * Checks if given result is failed, that is has violations
     *
     * @return true if result is failed, false otherwise
     */
    public boolean isFailed()
    {
        return !isSuccessful();
    }


    public Collection<Violation> getViolations()
    {
        return violations;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final WorkflowConnectionValidationResult that = (WorkflowConnectionValidationResult)o;
        return Objects.equals(violations, that.violations);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(violations);
    }


    @Override
    public String toString()
    {
        return "WorkflowConnectionValidationResult{" + "violations=" + violations + '}';
    }
}
