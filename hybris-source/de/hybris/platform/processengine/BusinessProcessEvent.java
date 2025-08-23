package de.hybris.platform.processengine;

import com.google.common.base.MoreObjects;
import java.util.Date;
import java.util.Objects;

public class BusinessProcessEvent
{
    private final String event;
    private final Date expirationDate;
    private final String choice;
    private final boolean triggeringInTheFutureEnabled;


    private BusinessProcessEvent(Builder builder)
    {
        this.event = builder.event;
        this.expirationDate = builder.expirationDate;
        this.choice = builder.choice;
        this.triggeringInTheFutureEnabled = builder.triggeringInTheFutureEnabled;
    }


    public String getEvent()
    {
        return this.event;
    }


    public Date getExpirationDate()
    {
        return this.expirationDate;
    }


    public String getChoice()
    {
        return this.choice;
    }


    public boolean isTriggeringInTheFutureEnabled()
    {
        return this.triggeringInTheFutureEnabled;
    }


    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("event", this.event).add("expirationDate", this.expirationDate).add("choice", this.choice)
                        .omitNullValues().toString();
    }


    public static BusinessProcessEvent newEvent(String event)
    {
        Objects.requireNonNull(event, "event can't be null");
        return builder(event).build();
    }


    public static Builder builder(String event)
    {
        Objects.requireNonNull(event, "event can't be null");
        return new Builder(event);
    }
}
