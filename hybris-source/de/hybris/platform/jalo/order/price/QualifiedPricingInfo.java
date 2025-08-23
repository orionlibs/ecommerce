package de.hybris.platform.jalo.order.price;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class QualifiedPricingInfo
{
    private final Map qualifiers;


    protected QualifiedPricingInfo()
    {
        this(Collections.EMPTY_MAP);
    }


    protected QualifiedPricingInfo(Map<?, ?> qualifiers)
    {
        this.qualifiers = new HashMap<>(qualifiers);
    }


    public int getQualifierCount()
    {
        return this.qualifiers.size();
    }


    public Set getQualifierKeys()
    {
        return Collections.unmodifiableSet(this.qualifiers.keySet());
    }


    public Object getQualifierValue(Object key)
    {
        return this.qualifiers.get(key);
    }


    public Map getQualifiers()
    {
        return Collections.unmodifiableMap(this.qualifiers);
    }


    public String toString()
    {
        return getQualifiers().toString();
    }
}
