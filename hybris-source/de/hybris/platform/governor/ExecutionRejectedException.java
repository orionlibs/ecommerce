package de.hybris.platform.governor;

import com.google.common.annotations.Beta;
import java.util.Objects;

@Beta
public class ExecutionRejectedException extends Exception
{
    @Beta
    public ExecutionRejectedException(String message)
    {
        super(Objects.<String>requireNonNull(message));
    }
}
