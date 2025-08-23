package de.hybris.platform.impex.jalo;

import de.hybris.platform.impex.jalo.imp.ImpExWorker;
import de.hybris.platform.impex.jalo.imp.ImpExWorkerResult;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.jalo.Item;

public abstract class AbstractProcessResult extends ImpExWorkerResult
{
    private Item item;


    private AbstractProcessResult(ImpExWorker worker, ValueLine valueLine, Item ret, Exception error)
    {
        super(worker, valueLine, ret, error);
    }


    public AbstractProcessResult(Item item)
    {
        this(null, null, null, null);
        this.item = item;
    }


    public Item getItem()
    {
        return this.item;
    }
}
