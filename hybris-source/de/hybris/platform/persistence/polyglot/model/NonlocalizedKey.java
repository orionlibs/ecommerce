package de.hybris.platform.persistence.polyglot.model;

public final class NonlocalizedKey extends SingleAttributeKey
{
    NonlocalizedKey(String qualifier)
    {
        super(qualifier);
    }


    public boolean equals(Object obj)
    {
        if(obj == null || obj == this || !(obj instanceof NonlocalizedKey))
        {
            return (obj == this);
        }
        NonlocalizedKey other = (NonlocalizedKey)obj;
        return other.qualifier.equals(this.qualifier);
    }


    public int hashCode()
    {
        return this.qualifier.hashCode();
    }


    public String toString()
    {
        return String.format("NonlocalizedKey(%s)", new Object[] {this.qualifier});
    }
}
