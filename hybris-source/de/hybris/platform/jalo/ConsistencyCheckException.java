package de.hybris.platform.jalo;

public class ConsistencyCheckException extends JaloBusinessException
{
    public ConsistencyCheckException(String message, int vendorCode)
    {
        super(message, vendorCode);
    }


    public ConsistencyCheckException(Throwable nested, int vendorCode)
    {
        super(nested, vendorCode);
    }


    public ConsistencyCheckException(Throwable nested, String message, int vendorCode)
    {
        super(nested, message, vendorCode);
    }
}
