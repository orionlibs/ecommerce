package de.hybris.platform.persistence.polyglot.model;

public abstract class Identity
{
    public abstract boolean isKnown();


    public abstract long toLongValue();


    public abstract boolean possiblyMatches(Identity paramIdentity);
}
