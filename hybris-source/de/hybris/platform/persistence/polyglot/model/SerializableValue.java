package de.hybris.platform.persistence.polyglot.model;

import java.io.Serializable;
import java.util.Objects;

public class SerializableValue
{
    private final Serializable serializableObject;


    SerializableValue(Serializable serializableObject)
    {
        this.serializableObject = Objects.<Serializable>requireNonNull(serializableObject);
    }


    public Serializable getSerializableObject()
    {
        return this.serializableObject;
    }


    public int hashCode()
    {
        return this.serializableObject.hashCode();
    }


    public boolean equals(Object other)
    {
        if(other == null || getClass() != other.getClass())
        {
            return false;
        }
        return (this == other || this.serializableObject.equals(((SerializableValue)other).serializableObject));
    }
}
