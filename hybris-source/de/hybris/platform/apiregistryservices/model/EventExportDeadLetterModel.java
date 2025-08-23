package de.hybris.platform.apiregistryservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class EventExportDeadLetterModel extends ItemModel
{
    public static final String _TYPECODE = "EventExportDeadLetter";
    public static final String ID = "id";
    public static final String EVENTTYPE = "eventType";
    public static final String DESTINATIONTARGET = "destinationTarget";
    public static final String DESTINATIONCHANNEL = "destinationChannel";
    public static final String TIMESTAMP = "timestamp";
    public static final String PAYLOAD = "payload";
    public static final String ERROR = "error";


    public EventExportDeadLetterModel()
    {
    }


    public EventExportDeadLetterModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EventExportDeadLetterModel(DestinationChannel _destinationChannel, DestinationTargetModel _destinationTarget, String _error, String _eventType, String _id, String _payload, Date _timestamp)
    {
        setDestinationChannel(_destinationChannel);
        setDestinationTarget(_destinationTarget);
        setError(_error);
        setEventType(_eventType);
        setId(_id);
        setPayload(_payload);
        setTimestamp(_timestamp);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EventExportDeadLetterModel(DestinationChannel _destinationChannel, DestinationTargetModel _destinationTarget, String _error, String _eventType, String _id, ItemModel _owner, String _payload, Date _timestamp)
    {
        setDestinationChannel(_destinationChannel);
        setDestinationTarget(_destinationTarget);
        setError(_error);
        setEventType(_eventType);
        setId(_id);
        setOwner(_owner);
        setPayload(_payload);
        setTimestamp(_timestamp);
    }


    @Accessor(qualifier = "destinationChannel", type = Accessor.Type.GETTER)
    public DestinationChannel getDestinationChannel()
    {
        return (DestinationChannel)getPersistenceContext().getPropertyValue("destinationChannel");
    }


    @Accessor(qualifier = "destinationTarget", type = Accessor.Type.GETTER)
    public DestinationTargetModel getDestinationTarget()
    {
        return (DestinationTargetModel)getPersistenceContext().getPropertyValue("destinationTarget");
    }


    @Accessor(qualifier = "error", type = Accessor.Type.GETTER)
    public String getError()
    {
        return (String)getPersistenceContext().getPropertyValue("error");
    }


    @Accessor(qualifier = "eventType", type = Accessor.Type.GETTER)
    public String getEventType()
    {
        return (String)getPersistenceContext().getPropertyValue("eventType");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "payload", type = Accessor.Type.GETTER)
    public String getPayload()
    {
        return (String)getPersistenceContext().getPropertyValue("payload");
    }


    @Accessor(qualifier = "timestamp", type = Accessor.Type.GETTER)
    public Date getTimestamp()
    {
        return (Date)getPersistenceContext().getPropertyValue("timestamp");
    }


    @Accessor(qualifier = "destinationChannel", type = Accessor.Type.SETTER)
    public void setDestinationChannel(DestinationChannel value)
    {
        getPersistenceContext().setPropertyValue("destinationChannel", value);
    }


    @Accessor(qualifier = "destinationTarget", type = Accessor.Type.SETTER)
    public void setDestinationTarget(DestinationTargetModel value)
    {
        getPersistenceContext().setPropertyValue("destinationTarget", value);
    }


    @Accessor(qualifier = "error", type = Accessor.Type.SETTER)
    public void setError(String value)
    {
        getPersistenceContext().setPropertyValue("error", value);
    }


    @Accessor(qualifier = "eventType", type = Accessor.Type.SETTER)
    public void setEventType(String value)
    {
        getPersistenceContext().setPropertyValue("eventType", value);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }


    @Accessor(qualifier = "payload", type = Accessor.Type.SETTER)
    public void setPayload(String value)
    {
        getPersistenceContext().setPropertyValue("payload", value);
    }


    @Accessor(qualifier = "timestamp", type = Accessor.Type.SETTER)
    public void setTimestamp(Date value)
    {
        getPersistenceContext().setPropertyValue("timestamp", value);
    }
}
