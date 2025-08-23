package de.hybris.platform.tx;

import de.hybris.platform.cache.impl.DefaultCache;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

abstract class LocalCacheKey
{
    protected final Object internalKey;
    private final int hashCode;


    public static LocalCacheKey create(Object[] key)
    {
        if(!DefaultCache.isMultiPathKey(key))
        {
            return (LocalCacheKey)new SinglePathKey(key);
        }
        List<NestedSinglePathKey> keys = new LinkedList<>();
        for(Object[] singleKey : DefaultCache.toSingleKey(key))
        {
            keys.add(new NestedSinglePathKey(singleKey));
        }
        return (LocalCacheKey)new MultiPathKey(keys.<SinglePathKey>toArray(new SinglePathKey[keys.size()]));
    }


    private LocalCacheKey(Object internalKey)
    {
        this.internalKey = Objects.requireNonNull(internalKey);
        if(internalKey.getClass().isArray())
        {
            this.hashCode = Arrays.deepHashCode((Object[])internalKey);
        }
        else
        {
            this.hashCode = internalKey.hashCode();
        }
    }


    public abstract Collection<LocalCacheKey> getDependantKeys();


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(!(obj instanceof LocalCacheKey) && getClass() != obj.getClass())
        {
            return false;
        }
        LocalCacheKey other = (LocalCacheKey)obj;
        return Objects.deepEquals(this.internalKey, other.internalKey);
    }


    public int hashCode()
    {
        return this.hashCode;
    }
}
