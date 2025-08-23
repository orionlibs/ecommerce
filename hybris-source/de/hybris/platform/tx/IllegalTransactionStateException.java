package de.hybris.platform.tx;

public class IllegalTransactionStateException extends TransactionException
{
    public IllegalTransactionStateException(Throwable throwable, String message, int vendorCode)
    {
        super(throwable, message, vendorCode);
    }


    public IllegalTransactionStateException(Throwable cause)
    {
        super(cause);
    }


    public IllegalTransactionStateException(String msg)
    {
        super(msg);
    }
}
