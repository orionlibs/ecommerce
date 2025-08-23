package de.hybris.platform.persistence.type;

public class EJBDuplicateQualifierException extends EJBTypeException
{
    public EJBDuplicateQualifierException(Exception e, String msg, int vendorCode)
    {
        super(e, msg, vendorCode);
    }
}
