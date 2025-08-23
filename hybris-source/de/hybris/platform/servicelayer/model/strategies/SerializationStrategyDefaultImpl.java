package de.hybris.platform.servicelayer.model.strategies;

import de.hybris.platform.servicelayer.model.AbstractItemModel;
import java.io.ObjectStreamException;

@Deprecated(since = "6.2.0", forRemoval = true)
public class SerializationStrategyDefaultImpl implements SerializationStrategy
{
    public Object readResolve(AbstractItemModel aim) throws ObjectStreamException
    {
        return aim;
    }


    public Object writeReplace(AbstractItemModel aim) throws ObjectStreamException
    {
        return aim;
    }
}
