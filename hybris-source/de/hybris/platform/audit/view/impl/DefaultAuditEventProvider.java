package de.hybris.platform.audit.view.impl;

import de.hybris.platform.audit.view.AuditEventProvider;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.audit.AuditType;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.internal.AuditEnablementService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAuditEventProvider implements AuditEventProvider
{
    private AuditEnablementService auditEnablementService;


    public Stream<AuditEvent> getEventsForAuditRows(Stream<AuditRecord> records)
    {
        Set<PK> mentionedItems = new HashSet<>();
        return records.<AuditEvent>flatMap(record -> getAuditEvents(record, mentionedItems).stream())
                        .sorted(Comparator.comparing(AuditEvent::getEventType, DefaultAuditEventProvider::compareEventType)
                                        .thenComparing(AuditEvent::getTimestamp)
                                        .thenComparing(auditEvent -> auditEvent.getAuditRecord().getType()));
    }


    private List<AuditEvent> getAuditEvents(AuditRecord record, Set<PK> mentionedItems)
    {
        List<AuditEvent> auditEvents = new ArrayList<>();
        if(record.getAuditType() == AuditType.CREATION)
        {
            mentionedItems.add(record.getPk());
        }
        else if(!mentionedItems.contains(record.getPk()))
        {
            mentionedItems.add(record.getPk());
            auditEvents.add(createVirtualEventForFirstOccurrence(record));
        }
        auditEvents.add(createEventFromRecord(record));
        return auditEvents;
    }


    private AuditEvent createVirtualEventForFirstOccurrence(AuditRecord record)
    {
        return (AuditEvent)new DefaultAuditEvent(record, getTimestampForRecord(record), EventType.FIRST_OCCURENCE);
    }


    private Date getTimestampForRecord(AuditRecord record)
    {
        if(this.auditEnablementService.isAuditEnabledForType(record.getType()))
        {
            return (Date)record.getAttributeBeforeOperation("creationtime");
        }
        return (Date)record.getAttributeBeforeOperation("modifiedtime");
    }


    private AuditEvent createEventFromRecord(AuditRecord record)
    {
        switch(null.$SwitchMap$de$hybris$platform$persistence$audit$AuditType[record.getAuditType().ordinal()])
        {
            case 1:
                return (AuditEvent)new DefaultAuditEvent(record, Date.from(Instant.now()), EventType.CURRENT);
            case 2:
                return (AuditEvent)new DefaultAuditEvent(record, record.getTimestamp(), EventType.CREATION);
            case 3:
                return (AuditEvent)new DefaultAuditEvent(record, record.getCurrentTimestamp(), EventType.DELETION);
            case 4:
                return (AuditEvent)new DefaultAuditEvent(record, record.getTimestamp(), EventType.MODIFICATION);
        }
        throw new IllegalArgumentException(String.format("unnexpected value of audit type %s", new Object[] {record.getAuditType()}));
    }


    private static int compareEventType(EventType o1, EventType o2)
    {
        if(o1 != o2)
        {
            if(o1 == EventType.CURRENT)
            {
                return 1;
            }
            if(o2 == EventType.CURRENT)
            {
                return -1;
            }
        }
        return 0;
    }


    @Required
    public void setAuditEnablementService(AuditEnablementService auditEnablementService)
    {
        this.auditEnablementService = auditEnablementService;
    }
}
