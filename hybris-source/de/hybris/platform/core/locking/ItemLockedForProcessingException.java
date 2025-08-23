package de.hybris.platform.core.locking;

public class ItemLockedForProcessingException extends RuntimeException
{
    public ItemLockedForProcessingException(String message)
    {
        super(message);
    }
}
