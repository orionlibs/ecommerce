package com.hybris.datahub.validation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ValidationContext
{
    private List<ValidationFailure> failures = new LinkedList<>();


    public void addFailure(ValidationFailure failure)
    {
        if(failure != null)
        {
            this.failures.add(failure);
        }
    }


    public void addFailures(Collection<ValidationFailure> errors)
    {
        if(errors != null)
        {
            this.failures.addAll(errors);
        }
    }


    public List<ValidationFailure> getFailures()
    {
        return this.failures;
    }


    public boolean hasFailures()
    {
        return !this.failures.isEmpty();
    }
}
