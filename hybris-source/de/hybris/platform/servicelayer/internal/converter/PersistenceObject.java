package de.hybris.platform.servicelayer.internal.converter;

import de.hybris.platform.core.PK;
import java.util.Map;

public interface PersistenceObject
{
    String getTypeCode();


    PK getPK();


    long getPersistenceVersion();


    PersistenceObject getLatest();


    default void refresh()
    {
    }


    Object readRawValue(ReadParams paramReadParams);


    Map<String, Object> readRawValues(ReadParams paramReadParams);


    boolean isEnumerationType();


    boolean isAlive();
}
