package de.hybris.platform.task;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.util.Date;
import java.util.Objects;

public class TaskEvent
{
    private final String id;
    private final Date expirationDate;
    private final String choice;
    private final boolean fulfilNotExistingCondition;
    private final boolean fulfillmentByRemoval;


    private TaskEvent(Builder builder)
    {
        if(builder.fulfillmentByRemoval)
        {
            Preconditions.checkArgument(!builder.fulfilNotExistingCondition);
            Preconditions.checkArgument((builder.choice == null));
            Preconditions.checkArgument((builder.expirationDate == null));
        }
        this.id = builder.id;
        this.expirationDate = builder.expirationDate;
        this.choice = builder.choice;
        this.fulfilNotExistingCondition = builder.fulfilNotExistingCondition;
        this.fulfillmentByRemoval = builder.fulfillmentByRemoval;
    }


    public String getId()
    {
        return this.id;
    }


    public Date getExpirationDate()
    {
        return this.expirationDate;
    }


    public String getChoice()
    {
        return this.choice;
    }


    public boolean hasChoice()
    {
        return (null != this.choice);
    }


    public boolean isFulfilNotExistingCondition()
    {
        return this.fulfilNotExistingCondition;
    }


    public boolean isFulfillmentByRemoval()
    {
        return this.fulfillmentByRemoval;
    }


    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                        .add("id", this.id)
                        .add("expirationDate", this.expirationDate)
                        .add("choice", this.choice)
                        .add("fulfilNotExistingCondition", this.fulfilNotExistingCondition)
                        .add("fulfillmentByRemoval", this.fulfillmentByRemoval)
                        .omitNullValues()
                        .toString();
    }


    public static TaskEvent newEvent(String id)
    {
        Objects.requireNonNull(id, "id can't be null");
        return builder(id).build();
    }


    public static TaskEvent fulfilByRemoval(String id)
    {
        Objects.requireNonNull(id, "id can't be null");
        return builder(id).disableFulfillingNotExistingCondition().withFulfillmentByRemoval().build();
    }


    public static Builder builder(String id)
    {
        Objects.requireNonNull(id, "id can't be null");
        return new Builder(id);
    }
}
