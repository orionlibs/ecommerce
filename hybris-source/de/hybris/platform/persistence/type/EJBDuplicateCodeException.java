package de.hybris.platform.persistence.type;

public class EJBDuplicateCodeException extends EJBTypeException
{
    public EJBDuplicateCodeException(Exception e, String msg, int vendorCode)
    {
        super(e, msg, vendorCode);
    }
}
