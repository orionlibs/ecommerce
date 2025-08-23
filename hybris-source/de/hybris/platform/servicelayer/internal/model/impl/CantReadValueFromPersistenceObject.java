package de.hybris.platform.servicelayer.internal.model.impl;

import java.util.Objects;

class CantReadValueFromPersistenceObject extends RuntimeException
{
    public CantReadValueFromPersistenceObject(String message)
    {
        super(Objects.<String>requireNonNull(message));
    }


    public CantReadValueFromPersistenceObject(String message, Throwable cause)
    {
        super(Objects.<String>requireNonNull(message), Objects.<Throwable>requireNonNull(cause));
    }


    public CantReadValueFromPersistenceObject(Throwable cause)
    {
        super(Objects.<Throwable>requireNonNull(cause));
    }
}
