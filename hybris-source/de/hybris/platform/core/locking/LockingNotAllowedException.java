package de.hybris.platform.core.locking;

public class LockingNotAllowedException extends RuntimeException
{
    public LockingNotAllowedException(String message)
    {
        super(message);
    }
}
