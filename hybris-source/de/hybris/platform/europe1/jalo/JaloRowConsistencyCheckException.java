package de.hybris.platform.europe1.jalo;

import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import java.util.Collection;

public class JaloRowConsistencyCheckException extends JaloPriceFactoryException
{
    private final Collection groups;


    public JaloRowConsistencyCheckException(Collection groups, String message, int vendorCode)
    {
        super(message, vendorCode);
        this.groups = groups;
    }


    public Collection getRowGroups()
    {
        return this.groups;
    }


    public String toString()
    {
        return super.toString() + " row groups : " + super.toString();
    }
}
