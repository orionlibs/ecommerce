package de.hybris.platform.persistence.type;

public class EJBAbstractTypeException extends EJBTypeException
{
    public EJBAbstractTypeException(Exception e, String msg, int vendorCode)
    {
        super(e, msg, vendorCode);
    }
}
