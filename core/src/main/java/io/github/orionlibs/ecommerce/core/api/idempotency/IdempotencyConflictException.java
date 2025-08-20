package io.github.orionlibs.ecommerce.core.api.idempotency;

import io.github.orionlibs.ecommerce.core.asserts.UncheckedException;

public class IdempotencyConflictException extends UncheckedException
{
    private static final String DefaultErrorMessage = "There was an error.";


    public IdempotencyConflictException(String errorMessage)
    {
        super(errorMessage);
    }


    public IdempotencyConflictException(String errorMessage, Object... arguments)
    {
        super(String.format(errorMessage, arguments));
    }


    public IdempotencyConflictException(Throwable cause, String errorMessage, Object... arguments)
    {
        super(String.format(errorMessage, arguments), cause);
    }


    public IdempotencyConflictException(Throwable cause)
    {
        super(DefaultErrorMessage, cause);
    }
}
