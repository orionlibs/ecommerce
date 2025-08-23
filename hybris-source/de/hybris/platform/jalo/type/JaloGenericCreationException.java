package de.hybris.platform.jalo.type;

public class JaloGenericCreationException extends JaloTypeException
{
    public JaloGenericCreationException(Throwable nested, int aVendorCode)
    {
        super(nested, aVendorCode);
    }


    public JaloGenericCreationException(String message, int code)
    {
        super(message, code);
    }
}
