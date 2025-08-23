package de.hybris.platform.jalo;

public class JaloInternalException extends JaloSystemException
{
    public JaloInternalException(Throwable exception, String message, int vendorCode)
    {
        super(exception, message, vendorCode);
    }


    public JaloInternalException(Throwable exception)
    {
        super(exception);
    }
}
