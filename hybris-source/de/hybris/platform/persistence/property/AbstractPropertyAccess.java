package de.hybris.platform.persistence.property;

public abstract class AbstractPropertyAccess implements PropertyAccess
{
    private PropertyOwner owner = null;
    private long version = -1L;


    protected AbstractPropertyAccess(long version)
    {
        this.version = version;
    }


    public final void registerOwner(PropertyOwner owner)
    {
        this.owner = owner;
    }


    public final void dispose()
    {
        this.owner = null;
    }


    public final PropertyOwner getOwner()
    {
        return this.owner;
    }


    public final long getVersion()
    {
        return this.version;
    }


    public final void setVersion(long vers)
    {
        this.version = vers;
    }


    protected synchronized void firePropertyDataChanged()
    {
        this.version++;
        if(this.owner != null)
        {
            this.owner.notifyPropertyDataChanged(this.version);
        }
    }
}
