package de.hybris.platform.processing.distributed.simple.data;

import de.hybris.platform.processing.distributed.BatchCreationData;
import java.io.Serializable;

public class SimpleBatchCreationData<T extends Serializable> implements BatchCreationData
{
    protected final T context;


    public SimpleBatchCreationData(T context)
    {
        this.context = context;
    }


    public T getContext()
    {
        return this.context;
    }
}
