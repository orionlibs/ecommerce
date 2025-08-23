package de.hybris.platform.jalo.type;

public class JaloDuplicateQualifierException extends JaloTypeException
{
    public JaloDuplicateQualifierException(Throwable nested, int vendorCode)
    {
        super(nested, vendorCode);
    }
}
