/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorkflowDesignerValidationResult
{
    private final Set<Violation> violations = new HashSet<>();


    public WorkflowDesignerValidationResult()
    {
    }


    public WorkflowDesignerValidationResult(final List<Violation> violations)
    {
        this.violations.addAll(violations);
    }


    public void addViolation(final Violation violation)
    {
        violations.add(violation);
    }


    public Set<Violation> getViolations()
    {
        return violations;
    }


    public boolean hasViolations()
    {
        return !violations.isEmpty();
    }


    public boolean hasErrors()
    {
        return violations.stream().anyMatch(violation -> violation.getLevel().equals(Violation.Level.ERROR));
    }


    public static WorkflowDesignerValidationResult combine(final WorkflowDesignerValidationResult result1,
                    final WorkflowDesignerValidationResult result2)
    {
        final WorkflowDesignerValidationResult combinedResults = new WorkflowDesignerValidationResult();
        combinedResults.getViolations().addAll(result1.getViolations());
        combinedResults.getViolations().addAll(result2.getViolations());
        return combinedResults;
    }
}
