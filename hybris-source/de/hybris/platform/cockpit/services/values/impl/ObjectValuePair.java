package de.hybris.platform.cockpit.services.values.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;

public class ObjectValuePair
{
    private final TypedObject object;
    private final ObjectValueContainer.ObjectValueHolder valueHolder;


    public ObjectValuePair(TypedObject object, ObjectValueContainer.ObjectValueHolder valueHolder)
    {
        if(object == null)
        {
            throw new IllegalArgumentException("object must not be null.");
        }
        this.object = object;
        this.valueHolder = valueHolder;
    }


    public TypedObject getObject()
    {
        return this.object;
    }


    public ObjectValueContainer.ObjectValueHolder getValueHolder()
    {
        return this.valueHolder;
    }


    public int hashCode()
    {
        return this.object.hashCode() + ((this.valueHolder == null) ? 0 : this.valueHolder.hashCode());
    }


    public boolean equals(Object obj)
    {
        if(obj instanceof ObjectValuePair)
        {
            ObjectValuePair other = (ObjectValuePair)obj;
            return ((other.object == this.object || (other.object != null && other.object.equals(this.object))) && (other.valueHolder == this.valueHolder || (other.valueHolder != null && other.valueHolder
                            .equals(this.valueHolder))));
        }
        return false;
    }
}
