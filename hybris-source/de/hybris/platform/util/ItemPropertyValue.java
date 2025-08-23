package de.hybris.platform.util;

import de.hybris.platform.core.PK;
import java.io.Serializable;

public class ItemPropertyValue implements Serializable, Comparable<ItemPropertyValue>
{
    private static final long serialVersionUID = -3108691849189051260L;
    private final PK pk;


    public ItemPropertyValue(PK pk)
    {
        if(pk == null)
        {
            throw new RuntimeException("you cannot create an ItemPropertyValue with a NULL pk.");
        }
        this.pk = pk;
    }


    public PK getPK()
    {
        return this.pk;
    }


    public String toString()
    {
        return "PropertyValue:" + this.pk.toString();
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        ItemPropertyValue other = (ItemPropertyValue)obj;
        if(this.pk == null)
        {
            if(other.pk != null)
            {
                return false;
            }
        }
        else if(!this.pk.equals(other.pk))
        {
            return false;
        }
        return true;
    }


    public int hashCode()
    {
        return (this.pk == null) ? 1 : this.pk.hashCode();
    }


    public int compareTo(ItemPropertyValue o)
    {
        return this.pk.compareTo(o.pk);
    }
}
