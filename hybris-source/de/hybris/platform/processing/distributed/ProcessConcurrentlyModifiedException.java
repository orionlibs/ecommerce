package de.hybris.platform.processing.distributed;

public class ProcessConcurrentlyModifiedException extends RuntimeException
{
    public ProcessConcurrentlyModifiedException(Throwable cause)
    {
        super(cause);
    }
}
