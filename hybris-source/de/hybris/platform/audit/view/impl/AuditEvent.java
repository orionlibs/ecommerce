package de.hybris.platform.audit.view.impl;

import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.internal.AuditRecordInternal;
import java.util.Date;

public interface AuditEvent extends AuditRecordInternal
{
    Date getTimestamp();


    EventType getEventType();


    AuditRecord getAuditRecord();
}
