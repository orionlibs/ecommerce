package de.hybris.platform.jalo.type;

public class JaloDuplicateCodeException extends JaloTypeException
{
    public JaloDuplicateCodeException(Throwable nested, int vendorCode)
    {
        super(nested, vendorCode);
    }
}
