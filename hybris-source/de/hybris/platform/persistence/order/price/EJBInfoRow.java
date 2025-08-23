package de.hybris.platform.persistence.order.price;

import java.io.Serializable;
import java.util.Map;

public abstract class EJBInfoRow implements Serializable
{
    protected Map qualifiers;


    protected EJBInfoRow(Map qualifiers)
    {
        this.qualifiers = qualifiers;
    }


    public Map getQualifiers()
    {
        return this.qualifiers;
    }
}
