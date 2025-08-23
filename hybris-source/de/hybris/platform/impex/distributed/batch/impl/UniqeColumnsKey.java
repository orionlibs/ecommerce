package de.hybris.platform.impex.distributed.batch.impl;

import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.SortedMap;

class UniqeColumnsKey
{
    private final Object[] uniqueValues;


    private UniqeColumnsKey(Object[] uniqueValues)
    {
        this.uniqueValues = uniqueValues;
    }


    public static UniqeColumnsKey from(SortedMap<StandardColumnDescriptor, Object> uniqueValues)
    {
        Objects.requireNonNull(uniqueValues, "uniqueValues mustn't be null.");
        Collection<Object> values = uniqueValues.values();
        return new UniqeColumnsKey(values.toArray(new Object[values.size()]));
    }


    public boolean equals(Object obj)
    {
        if(obj == this)
        {
            return true;
        }
        if(obj == null || !(obj instanceof UniqeColumnsKey))
        {
            return false;
        }
        UniqeColumnsKey other = (UniqeColumnsKey)obj;
        return Arrays.equals(this.uniqueValues, other.uniqueValues);
    }


    public int hashCode()
    {
        return Arrays.hashCode(this.uniqueValues);
    }


    public String toString()
    {
        return Arrays.toString(this.uniqueValues);
    }
}
