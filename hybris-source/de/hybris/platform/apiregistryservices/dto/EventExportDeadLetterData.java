package de.hybris.platform.apiregistryservices.dto;

import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import java.io.Serializable;
import java.util.Date;

public class EventExportDeadLetterData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String eventType;
    private String id;
    private Date timestamp;
    private String payload;
    private String error;
    private DestinationTargetModel destinationTarget;


    public void setEventType(String eventType)
    {
        this.eventType = eventType;
    }


    public String getEventType()
    {
        return this.eventType;
    }


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }


    public Date getTimestamp()
    {
        return this.timestamp;
    }


    public void setPayload(String payload)
    {
        this.payload = payload;
    }


    public String getPayload()
    {
        return this.payload;
    }


    public void setError(String error)
    {
        this.error = error;
    }


    public String getError()
    {
        return this.error;
    }


    public void setDestinationTarget(DestinationTargetModel destinationTarget)
    {
        this.destinationTarget = destinationTarget;
    }


    public DestinationTargetModel getDestinationTarget()
    {
        return this.destinationTarget;
    }
}
