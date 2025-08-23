package de.hybris.platform.persistence.type;

import java.io.Serializable;

public class Wrapper implements Serializable
{
    private Object wo_;


    protected Wrapper()
    {
        this(null);
    }


    public Wrapper(Object wo)
    {
        setWrappedObject(wo);
    }


    protected Object getWrappedObject()
    {
        return this.wo_;
    }


    protected void setWrappedObject(Object wo)
    {
        this.wo_ = wo;
    }


    public boolean equals(Object o)
    {
        if(o instanceof Wrapper)
        {
            return getWrappedObject().equals(((Wrapper)o).getWrappedObject());
        }
        return (o != null && o.equals(this));
    }


    public int hashCode()
    {
        return getWrappedObject().hashCode();
    }


    public String toString()
    {
        return getWrappedObject().toString();
    }
}
