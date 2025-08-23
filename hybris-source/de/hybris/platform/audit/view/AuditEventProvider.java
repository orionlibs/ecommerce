package de.hybris.platform.audit.view;

import de.hybris.platform.audit.view.impl.AuditEvent;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import java.util.stream.Stream;

public interface AuditEventProvider
{
    Stream<AuditEvent> getEventsForAuditRows(Stream<AuditRecord> paramStream);
}
