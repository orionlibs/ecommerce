package de.hybris.platform.persistence.polyglot.model;

public class UnknownIdentity extends Identity
{
    public static Identity instance()
    {
        return new UnknownIdentity();
    }


    public boolean isKnown()
    {
        return false;
    }


    public long toLongValue()
    {
        return 0L;
    }


    public boolean possiblyMatches(Identity identity)
    {
        return true;
    }


    public int hashCode()
    {
        return 0;
    }


    public boolean equals(Object obj)
    {
        if(obj == null)
        {
            return false;
        }
        return (getClass() == obj.getClass());
    }
}
