package de.hybris.bootstrap.loader.metrics.internal;

import de.hybris.bootstrap.loader.metrics.EventType;
import java.util.Objects;

public class EventCounterKey
{
    private final String name;
    private final EventType type;


    public EventCounterKey(String name, EventType type)
    {
        this.name = name;
        this.type = type;
    }


    public String getName()
    {
        return this.name;
    }


    public EventType getType()
    {
        return this.type;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        EventCounterKey that = (EventCounterKey)o;
        return (Objects.equals(this.name, that.name) && this.type == that.type);
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.name, this.type});
    }


    public String toString()
    {
        return "EventCounterKey{name='" + this.name + "', type=" + this.type + "}";
    }
}
