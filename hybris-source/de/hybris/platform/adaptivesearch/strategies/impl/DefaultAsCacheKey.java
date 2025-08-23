package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.strategies.AsCacheKey;
import de.hybris.platform.adaptivesearch.strategies.AsCacheScope;
import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class DefaultAsCacheKey implements AsCacheKey
{
    private final AsCacheScope scope;
    private final Serializable[] keyFragments;


    public DefaultAsCacheKey(AsCacheScope scope, Serializable... keyFragments)
    {
        this.scope = scope;
        this.keyFragments = keyFragments;
    }


    public AsCacheScope getScope()
    {
        return this.scope;
    }


    public String toString()
    {
        return "DefaultAsCacheKey [scope=" + this.scope + ", keyFragments=" + this.keyFragments + "]";
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        DefaultAsCacheKey that = (DefaultAsCacheKey)obj;
        return (new EqualsBuilder()).append(this.scope, that.scope).append((Object[])this.keyFragments, (Object[])that.keyFragments).isEquals();
    }


    public int hashCode()
    {
        int keyFragmentsResult = (this.keyFragments != null) ? Arrays.hashCode((Object[])this.keyFragments) : 0;
        return 31 * ((this.scope != null) ? this.scope.ordinal() : 0) + keyFragmentsResult;
    }
}
