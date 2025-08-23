package com.hybris.datahub.validation;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValidationException extends RuntimeException
{
    private static final long serialVersionUID = -2389119576795449520L;
    private final List<ValidationFailure> failures;


    public ValidationException(List<ValidationFailure> failures)
    {
        Preconditions.checkArgument((failures != null && !failures.isEmpty()), "At least one failure must be present for ValidationException to be thrown");
        this.failures = failures;
    }


    public ValidationException(ValidationFailure failure)
    {
        this((failure != null) ? Arrays.<ValidationFailure>asList(new ValidationFailure[] {failure}) : new ArrayList<>());
    }


    public boolean hasFailureOfType(ValidationFailureType type)
    {
        return this.failures.stream().anyMatch(f -> f.getFailureType().equals(type));
    }


    public List<ValidationFailure> getFailures()
    {
        return this.failures;
    }


    public String getMessage()
    {
        return this.failures.toString();
    }


    public String toString()
    {
        return "ValidationException{failures=" + this.failures + "}";
    }
}
