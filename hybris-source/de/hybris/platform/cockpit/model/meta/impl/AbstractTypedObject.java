package de.hybris.platform.cockpit.model.meta.impl;

import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.TypedObject;

public abstract class AbstractTypedObject implements TypedObject
{
    protected BaseType type;
    protected Object object;


    public String toString()
    {
        String ret = null;
        if(getType() == null)
        {
            ret = super.toString();
        }
        else
        {
            ret = "<" + getType().getCode() + "> " + getObject();
        }
        return ret;
    }


    public boolean equals(Object object)
    {
        boolean ret = false;
        if(object instanceof AbstractTypedObject)
        {
            ret = (this.object != null && this.object.equals(((AbstractTypedObject)object).object));
        }
        return ret;
    }


    public int hashCode()
    {
        return (this.object == null) ? super.hashCode() : this.object.hashCode();
    }
}
