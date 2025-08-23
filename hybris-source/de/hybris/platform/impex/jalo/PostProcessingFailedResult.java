package de.hybris.platform.impex.jalo;

import de.hybris.platform.jalo.Item;

public class PostProcessingFailedResult extends AbstractProcessResult
{
    final Exception reason;


    public PostProcessingFailedResult(Item item, Exception reason)
    {
        super(item);
        this.reason = reason;
    }


    public Exception getReason()
    {
        return this.reason;
    }
}
