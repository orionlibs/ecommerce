package de.hybris.platform.persistence.polyglot.model;

import de.hybris.platform.persistence.polyglot.PolyglotFeature;
import java.util.Objects;

public class HandlingRelatedItemsFeature implements PolyglotFeature
{
    private final Key attributeKey;
    private final Reference typeReference;
    private final boolean writable;


    private HandlingRelatedItemsFeature(Reference typeReference, Key attributeKey, boolean writable)
    {
        this.typeReference = Objects.<Reference>requireNonNull(typeReference);
        this.attributeKey = Objects.<Key>requireNonNull(attributeKey);
        this.writable = writable;
    }


    static PolyglotFeature create(Reference typeReference, Key attributeKey, boolean writable)
    {
        return new HandlingRelatedItemsFeature(typeReference, attributeKey, writable);
    }


    static PolyglotFeature create(Reference typeReference, Key attributeKey)
    {
        return new HandlingRelatedItemsFeature(typeReference, attributeKey, false);
    }


    public boolean isWritable()
    {
        return this.writable;
    }


    public Key getAttributeKey()
    {
        return this.attributeKey;
    }


    public Reference getTypeReference()
    {
        return this.typeReference;
    }


    public boolean equals(Object obj)
    {
        if(obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        HandlingRelatedItemsFeature other = (HandlingRelatedItemsFeature)obj;
        return (this == other || (this.attributeKey.equals(other.attributeKey) && this.typeReference.equals(other.typeReference)));
    }


    public int hashCode()
    {
        return this.attributeKey.hashCode();
    }
}
