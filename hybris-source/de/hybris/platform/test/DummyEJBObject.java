package de.hybris.platform.test;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.framework.EntityProxy;

class DummyEJBObject implements EntityProxy
{
    private final String pk;


    public DummyEJBObject(String pk, String ident)
    {
        this.pk = pk;
    }


    public boolean equals(Object object)
    {
        if(!(object instanceof DummyEJBObject))
        {
            return false;
        }
        return this.pk.equals(((DummyEJBObject)object).pk);
    }


    public int hashCode()
    {
        return this.pk.hashCode();
    }


    public String getPrimaryKey()
    {
        return this.pk;
    }


    public PK getPK()
    {
        return PK.parse(this.pk);
    }


    public void remove()
    {
    }
}
