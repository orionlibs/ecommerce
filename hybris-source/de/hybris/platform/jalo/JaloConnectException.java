package de.hybris.platform.jalo;

public class JaloConnectException extends JaloBusinessException
{
    public JaloConnectException(String message, int vendorCode)
    {
        super(message, vendorCode);
    }
}
