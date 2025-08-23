package de.hybris.platform.servicelayer.internal.model.impl;

import com.google.common.collect.ImmutableSet;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.util.Key;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

abstract class OriginalValueHolder<K> implements Serializable
{
    private static final int INITIAL_COLLECTION_VALUE_SIZE = 32;
    private final Map<K, Object> originalValues = new HashMap<>(32);
    private final Map<K, Object> originalValuesOnDemand = new HashMap<>(32);


    boolean hasKey(K qualifier)
    {
        return this.originalValues.containsKey(qualifier);
    }


    private void setOnDemand(K qualifier, Object value)
    {
        this.originalValuesOnDemand.put(qualifier, value);
    }


    abstract Object getOnDemand(K paramK, AttributeProvider paramAttributeProvider);


    abstract K cloneKeyIfNecessary(K paramK);


    void set(K qualifier, Object value)
    {
        this.originalValues.put(qualifier, value);
    }


    void remove(K qualifier)
    {
        this.originalValues.remove(qualifier);
        this.originalValuesOnDemand.remove(qualifier);
    }


    Object get(K qualifier, AttributeProvider attributeProvider)
    {
        if(this.originalValues.containsKey(qualifier))
        {
            return this.originalValues.get(qualifier);
        }
        if(this.originalValuesOnDemand.containsKey(qualifier))
        {
            return this.originalValuesOnDemand.get(qualifier);
        }
        if(attributeProvider != null)
        {
            try
            {
                K potentiallyClonedQualifier = cloneKeyIfNecessary(qualifier);
                Object result;
                setOnDemand(potentiallyClonedQualifier,
                                result = getOnDemand(qualifier, attributeProvider));
                return result;
            }
            catch(ModelLoadingException e)
            {
                throw new IllegalStateException("no original value could be loaded for " + qualifier + " due to, " + e
                                .getMessage(), e);
            }
        }
        throw new IllegalStateException("no original value loaded for " + qualifier);
    }


    Set<K> toKeys()
    {
        return (Set<K>)ImmutableSet.copyOf(this.originalValues.keySet());
    }


    static OriginalValueHolder<String> newUnlocalized()
    {
        return (OriginalValueHolder<String>)new UnlocalizedOriginalValueHolder();
    }


    static OriginalValueHolder<Key<Locale, String>> newLocalized()
    {
        return (OriginalValueHolder<Key<Locale, String>>)new LocalizedOriginalValueHolder();
    }
}
