package de.hybris.platform.audit.view.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import java.util.Date;
import java.util.Objects;

public class DefaultAuditEvent implements AuditEvent
{
    private final Date timestamp;
    private final EventType eventType;
    private final AuditRecord record;


    public DefaultAuditEvent(AuditRecord record, Date timestamp, EventType eventType)
    {
        this.timestamp = Objects.<Date>requireNonNull(timestamp, "record is required");
        this.eventType = Objects.<EventType>requireNonNull(eventType, "eventType is required");
        this.record = Objects.<AuditRecord>requireNonNull(record, "record, is required");
    }


    public Date getTimestamp()
    {
        return this.timestamp;
    }


    public EventType getEventType()
    {
        return this.eventType;
    }


    public AuditRecord getAuditRecord()
    {
        return this.record;
    }


    public PK getPk()
    {
        return getAuditRecord().getPk();
    }


    public String getType()
    {
        return getAuditRecord().getType();
    }


    public Object getAttribute(String key)
    {
        if(EventType.DELETION.equals(getEventType()) || EventType.CURRENT.equals(getEventType()) || EventType.FIRST_OCCURENCE
                        .equals(getEventType()))
        {
            return getAuditRecord().getAttributeBeforeOperation(key);
        }
        return getAuditRecord().getAttributeAfterOperation(key);
    }


    public Object getAttribute(String key, String langIsoCode)
    {
        if(EventType.DELETION.equals(getEventType()) || EventType.CURRENT.equals(getEventType()) || EventType.FIRST_OCCURENCE
                        .equals(getEventType()))
        {
            return getAuditRecord().getAttributeBeforeOperation(key, langIsoCode);
        }
        return getAuditRecord().getAttributeAfterOperation(key, langIsoCode);
    }


    public Object getAttributeBeforeOperation(String key)
    {
        return getAuditRecord().getAttributeBeforeOperation(key);
    }


    public Object getAttributeBeforeOperation(String key, String langIsoCode)
    {
        return getAuditRecord().getAttributeBeforeOperation(key, langIsoCode);
    }


    public Object getAttributeAfterOperation(String key)
    {
        return getAuditRecord().getAttributeAfterOperation(key);
    }


    public Object getAttributeAfterOperation(String key, String langIsoCode)
    {
        return getAuditRecord().getAttributeAfterOperation(key, langIsoCode);
    }
}
