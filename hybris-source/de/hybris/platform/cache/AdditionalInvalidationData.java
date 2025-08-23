package de.hybris.platform.cache;

import de.hybris.platform.core.PK;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

public final class AdditionalInvalidationData
{
    private static final AdditionalInvalidationData EMPTY_INSTANCE = new AdditionalInvalidationData(Map.of());
    private final Map<String, Set<PK>> foreignKeys;


    private AdditionalInvalidationData(Map<String, Set<PK>> foreignKeys)
    {
        this.foreignKeys = Map.copyOf(foreignKeys);
    }


    public static AdditionalInvalidationData empty()
    {
        return EMPTY_INSTANCE;
    }


    public static Optional<AdditionalInvalidationData> fromString(String str)
    {
        return ForeignKeysParser.tryToParse(str);
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public static Builder builder(AdditionalInvalidationData fromExisting)
    {
        return new Builder(fromExisting.foreignKeys);
    }


    public boolean isEmpty()
    {
        return this.foreignKeys.isEmpty();
    }


    public String asString()
    {
        return ForeignKeysParser.asString(this.foreignKeys);
    }


    public Set<PK> getForeignKeyValues(String foreignKeyName)
    {
        Set<PK> values = this.foreignKeys.get(foreignKeyName);
        return (values == null) ? Set.<PK>of() : Set.<PK>copyOf(values);
    }


    public void forEachForeignKey(BiConsumer<String, PK> consumer)
    {
        Objects.requireNonNull(consumer);
        this.foreignKeys.forEach((fk, pkSet) -> pkSet.forEach(()));
    }


    public String toString()
    {
        return "AdditionalInvalidationData{" + asString() + "}";
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        AdditionalInvalidationData data = (AdditionalInvalidationData)o;
        return this.foreignKeys.equals(data.foreignKeys);
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.foreignKeys});
    }
}
