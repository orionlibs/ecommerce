package de.hybris.platform.tx;

public class RollbackOnlyException extends TransactionException
{
    public RollbackOnlyException(String message)
    {
        super(message);
    }
}
