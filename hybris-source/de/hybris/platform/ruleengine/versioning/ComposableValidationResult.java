package de.hybris.platform.ruleengine.versioning;

import java.util.Objects;
import java.util.function.Supplier;

public class ComposableValidationResult extends AbstractValidationResult
{
    public static final ComposableValidationResult SUCCESS = new ComposableValidationResult((AbstractValidationResult)new AbstractValidationResult.Success());
    private final AbstractValidationResult target;


    public ComposableValidationResult(AbstractValidationResult target)
    {
        this.target = Objects.<AbstractValidationResult>requireNonNull(target);
    }


    public static final ComposableValidationResult makeError(String errorMessage)
    {
        return new ComposableValidationResult(error(Objects.<String>requireNonNull(errorMessage)));
    }


    public boolean succeeded()
    {
        return this.target.succeeded();
    }


    public String getErrorMessage()
    {
        return this.target.getErrorMessage();
    }


    public ComposableValidationResult and(Supplier<ComposableValidationResult> supplier)
    {
        if(!succeeded())
        {
            return this;
        }
        return Objects.<ComposableValidationResult>requireNonNull(((Supplier<ComposableValidationResult>)Objects.<Supplier<ComposableValidationResult>>requireNonNull(supplier)).get());
    }
}
