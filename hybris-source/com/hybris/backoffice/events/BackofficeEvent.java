package com.hybris.backoffice.events;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import java.io.Serializable;

@Deprecated(since = "6.7", forRemoval = true)
public class BackofficeEvent extends AbstractEvent
{
    private final String name;
    private final Object data;


    public BackofficeEvent(String code, Serializable source)
    {
        super(source);
        this.name = code;
        this.data = null;
    }


    public BackofficeEvent(String code, Object data, Serializable source)
    {
        super(source);
        this.name = code;
        this.data = data;
    }


    public String getName()
    {
        return this.name;
    }


    public Object getData()
    {
        return this.data;
    }
}
