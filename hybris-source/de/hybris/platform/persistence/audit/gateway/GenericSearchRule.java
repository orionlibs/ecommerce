package de.hybris.platform.persistence.audit.gateway;

import java.util.Objects;

public class GenericSearchRule<T> implements SearchRule<T>
{
    protected final String fieldName;
    protected final T value;
    private final boolean forPayload;


    public GenericSearchRule(String fieldName, T value, boolean forPayload)
    {
        this.fieldName = Objects.<String>requireNonNull(fieldName, "fieldName is required");
        this.value = Objects.requireNonNull(value, "value is required");
        this.forPayload = forPayload;
    }


    public String getFieldName()
    {
        return this.fieldName;
    }


    public T getValue()
    {
        return this.value;
    }


    public boolean isForPayload()
    {
        return this.forPayload;
    }
}
