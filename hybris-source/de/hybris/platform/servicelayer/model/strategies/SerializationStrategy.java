package de.hybris.platform.servicelayer.model.strategies;

import de.hybris.platform.servicelayer.model.AbstractItemModel;
import java.io.ObjectStreamException;
import java.io.Serializable;

public interface SerializationStrategy extends Serializable
{
    Object writeReplace(AbstractItemModel paramAbstractItemModel) throws ObjectStreamException;


    Object readResolve(AbstractItemModel paramAbstractItemModel) throws ObjectStreamException;
}
