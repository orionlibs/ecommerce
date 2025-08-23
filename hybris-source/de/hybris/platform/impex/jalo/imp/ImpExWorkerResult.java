package de.hybris.platform.impex.jalo.imp;

import de.hybris.platform.jalo.Item;

public class ImpExWorkerResult
{
    private final ImpExWorker worker;
    private final ValueLine line;
    private final Item result;
    private final Exception error;


    public ImpExWorkerResult(ImpExWorker worker, ValueLine valueLine, Item ret, Exception error)
    {
        this.worker = worker;
        this.line = valueLine;
        this.result = ret;
        this.error = error;
    }


    public Exception getError()
    {
        return this.error;
    }


    public ValueLine getLine()
    {
        return this.line;
    }


    public Item getResult()
    {
        return this.result;
    }


    public ImpExWorker getWorker()
    {
        return this.worker;
    }


    public String toString()
    {
        StringBuilder builder = new StringBuilder(1000);
        builder.append(getClass().getSimpleName());
        if(this.line != null)
        {
            builder.append("valueLine [ " + this.line + "] ");
        }
        if(this.result != null)
        {
            builder.append("result [ " + this.result + "]");
        }
        if(this.worker != null)
        {
            builder.append("worker [ " + this.worker + "]");
        }
        if(this.error != null)
        {
            builder.append("error [ " + this.error.getMessage() + "]");
        }
        return builder.toString();
    }
}
