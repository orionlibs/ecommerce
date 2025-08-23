package de.hybris.platform.persistence.polyglot.model;

import java.util.Objects;

public class Reference
{
    private final Identity id;


    Reference(Identity id)
    {
        this.id = Objects.<Identity>requireNonNull(id);
    }


    public Identity getIdentity()
    {
        return this.id;
    }


    public int hashCode()
    {
        return this.id.hashCode();
    }


    public boolean equals(Object other)
    {
        if(other == null || getClass() != other.getClass())
        {
            return false;
        }
        return (this == other || this.id.equals(((Reference)other).id));
    }


    public String toString()
    {
        return "Reference(" + this.id + ")";
    }
}
