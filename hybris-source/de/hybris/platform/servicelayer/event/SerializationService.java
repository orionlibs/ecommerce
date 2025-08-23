package de.hybris.platform.servicelayer.event;

public interface SerializationService
{
    byte[] serialize(Object paramObject);


    Object deserialize(byte[] paramArrayOfbyte);
}
