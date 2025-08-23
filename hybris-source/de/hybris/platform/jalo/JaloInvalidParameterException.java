package de.hybris.platform.jalo;

public class JaloInvalidParameterException extends JaloSystemException
{
    public JaloInvalidParameterException(String message, int vendorCode)
    {
        super(null, message, vendorCode);
    }


    public JaloInvalidParameterException(Exception nested, int vendorCode)
    {
        super(nested, nested.getMessage(), vendorCode);
    }


    public JaloInvalidParameterException(Throwable nested, int vendorCode)
    {
        super(nested, nested.getMessage(), vendorCode);
    }
}
