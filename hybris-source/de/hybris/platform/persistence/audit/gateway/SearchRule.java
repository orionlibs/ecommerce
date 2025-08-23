package de.hybris.platform.persistence.audit.gateway;

public interface SearchRule<T>
{
    String getFieldName();


    T getValue();


    boolean isForPayload();
}
