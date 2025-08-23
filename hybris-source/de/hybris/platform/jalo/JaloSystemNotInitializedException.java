package de.hybris.platform.jalo;

public class JaloSystemNotInitializedException extends JaloSystemException
{
    public JaloSystemNotInitializedException(Throwable exception, String message, int vendorCode)
    {
        super(exception, message, vendorCode);
    }
}
