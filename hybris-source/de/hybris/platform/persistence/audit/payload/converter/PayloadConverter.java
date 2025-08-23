package de.hybris.platform.persistence.audit.payload.converter;

public interface PayloadConverter<T>
{
    String convertToString(T paramT);


    T convertFromString(String paramString);


    Class<T> forClass();
}
