package de.hybris.platform.persistence.polyglot.model;

final class LongIdentity extends Identity implements Comparable<LongIdentity>
{
    private final long id;


    LongIdentity(long id)
    {
        this.id = id;
    }


    public boolean isKnown()
    {
        return true;
    }


    public long toLongValue()
    {
        return this.id;
    }


    public boolean possiblyMatches(Identity identity)
    {
        if(identity == null || !identity.isKnown())
        {
            return true;
        }
        return (toLongValue() == identity.toLongValue());
    }


    public boolean equals(Object obj)
    {
        if(obj == null || obj.getClass() == UnknownIdentity.class || !(obj instanceof Identity))
        {
            return false;
        }
        return (this.id == ((Identity)obj).toLongValue());
    }


    public int hashCode()
    {
        return Long.hashCode(this.id);
    }


    public String toString()
    {
        return String.format("%s(%s)", new Object[] {getClass().getSimpleName(), Long.valueOf(this.id)});
    }


    public int compareTo(LongIdentity obj)
    {
        return Long.compare(this.id, obj.id);
    }
}
