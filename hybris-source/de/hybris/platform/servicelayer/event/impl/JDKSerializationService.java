package de.hybris.platform.servicelayer.event.impl;

import de.hybris.platform.servicelayer.event.SerializationService;
import java.io.Serializable;
import org.apache.commons.lang.SerializationException;
import org.apache.commons.lang.SerializationUtils;

public class JDKSerializationService implements SerializationService
{
    public Object deserialize(byte[] bytes)
    {
        return SerializationUtils.deserialize(bytes);
    }


    public byte[] serialize(Object object)
    {
        if(!(object instanceof Serializable))
        {
            throw new SerializationException("Object must implement java.io.Serializable");
        }
        return SerializationUtils.serialize((Serializable)object);
    }
}
