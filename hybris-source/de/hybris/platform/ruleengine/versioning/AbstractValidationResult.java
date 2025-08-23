package de.hybris.platform.ruleengine.versioning;

import com.google.common.base.Preconditions;
import java.util.Objects;

public abstract class AbstractValidationResult
{
    public static final AbstractValidationResult error(String errorMessage)
    {
        Preconditions.checkArgument(Objects.nonNull(errorMessage), "message must not be empty");
        return (AbstractValidationResult)new Error(errorMessage);
    }


    public abstract boolean succeeded();


    public abstract String getErrorMessage();
}
